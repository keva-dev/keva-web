package dev.keva.web.core;

import dev.keva.web.core.parser.HttpRequestParser;
import dev.keva.web.core.parser.HttpResponseParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class SocketHandler implements Runnable {
    private final Socket socket;
    private final Map<String, Set<HttpHandler>> preHandlers;
    private final Map<String, Map<HttpRequestMethod, HttpHandler>> handler;
    private final Map<String, Set<HttpHandler>> postHandlers;

    @SneakyThrows
    @Override
    public void run() {
        socket.setSoTimeout(5_000);
        try {
            int count = 0;
            while (!Thread.currentThread().isInterrupted() && count < 1_000) {
                HttpRequest request = HttpRequestParser.parse(socket.getInputStream());
                HttpResponse response = Dispatcher.dispatchRequest(request, preHandlers, handler, postHandlers);
                HttpResponseParser.parse(response, socket.getOutputStream());
                count++;
            }
        } catch (SocketTimeoutException ignored) {
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            socket.close();
        }
    }
}
