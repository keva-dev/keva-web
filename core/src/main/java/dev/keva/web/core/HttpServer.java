package dev.keva.web.core;

import dev.keva.web.core.annotation.AnnotatedRouter;
import dev.keva.web.core.factory.CoreFactory;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Builder
@Slf4j
public class HttpServer {
    static {
        CoreFactory.loadDefault();
    }

    private final Map<String, Set<HttpHandler>> preHandlers = new HashMap<>();
    private final Map<String, Set<HttpHandler>> postHandlers = new HashMap<>();
    private final Map<String, Map<HttpRequestMethod, HttpHandler>> handlers = new HashMap<>();

    private String host;
    private int port;
    private ExecutorService executor;

    public HttpServer use(HttpRouter router) {
        preHandlers.put("", router.getPreHandlers());
        postHandlers.put("", router.getPostHandlers());
        handlers.putAll(router.getHandlers());
        return this;
    }

    public HttpServer use(String routerPath, HttpRouter router) {
        preHandlers.put(routerPath, router.getPreHandlers());
        postHandlers.put(routerPath, router.getPostHandlers());
        router.getHandlers().forEach((path, handlerMap) -> {
            handlers.put(routerPath + path, handlerMap);
        });
        return this;
    }

    public HttpServer annotated(Object annotatedObject) {
        AnnotatedRouter annotatedRouter = CoreFactory.getAnnotationProcessor().process(annotatedObject);
        return use(annotatedRouter.getPath(), annotatedRouter.getRouter());
    }

    public void run() throws IOException {
        if (executor == null) {
            executor = Executors.newCachedThreadPool();
        }
        if (port == 0) {
            port = 8080;
        }
        SocketAddress endpoint = host == null ?
                new InetSocketAddress(InetAddress.getLoopbackAddress(), port)
                : new InetSocketAddress(host, port);
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(endpoint);
        while (!Thread.interrupted()) {
            Socket clientSocket = serverSocket.accept();
            executor.execute(new SocketHandler(clientSocket, preHandlers, handlers, postHandlers));
        }
    }
}
