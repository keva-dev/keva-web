package dev.keva.web.core;

import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HttpRouter {
    @Getter
    private final Set<HttpHandler> preHandlers = new HashSet<>();
    @Getter
    private final Set<HttpHandler> postHandlers = new HashSet<>();
    @Getter
    private final Map<String, Map<HttpRequestMethod, HttpHandler>> handlers = new HashMap<>();

    public HttpRouter use(HttpHandler handler) {
        preHandlers.add(handler);
        return this;
    }

    public HttpRouter before(HttpHandler handler) {
        return use(handler);
    }

    public HttpRouter after(HttpHandler handler) {
        postHandlers.add(handler);
        return this;
    }

    public HttpRouter addHandler(String path, HttpRequestMethod method, HttpHandler handler) {
        this.handlers.computeIfAbsent(path, k -> new HashMap<>());
        this.handlers.get(path).put(method, handler);
        return this;
    }

    public HttpRouter get(String path, HttpHandler handler) {
        this.handlers.computeIfAbsent(path, k -> new HashMap<>());
        this.handlers.get(path).put(HttpRequestMethod.GET, handler);
        return this;
    }

    public HttpRouter post(String path, HttpHandler handler) {
        this.handlers.computeIfAbsent(path, k -> new HashMap<>());
        this.handlers.get(path).put(HttpRequestMethod.POST, handler);
        return this;
    }

    public HttpRouter put(String path, HttpHandler handler) {
        this.handlers.computeIfAbsent(path, k -> new HashMap<>());
        this.handlers.get(path).put(HttpRequestMethod.PUT, handler);
        return this;
    }

    public HttpRouter patch(String path, HttpHandler handler) {
        this.handlers.computeIfAbsent(path, k -> new HashMap<>());
        this.handlers.get(path).put(HttpRequestMethod.PATCH, handler);
        return this;
    }

    public HttpRouter delete(String path, HttpHandler handler) {
        this.handlers.computeIfAbsent(path, k -> new HashMap<>());
        this.handlers.get(path).put(HttpRequestMethod.DELETE, handler);
        return this;
    }
}
