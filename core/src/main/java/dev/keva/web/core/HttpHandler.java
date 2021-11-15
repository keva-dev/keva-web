package dev.keva.web.core;

@FunctionalInterface
public interface HttpHandler {
    HttpResponse handle(Context context);
}
