package dev.keva.web.example;

import dev.keva.web.core.Context;
import dev.keva.web.core.HttpHandler;
import dev.keva.web.core.HttpResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestLoggerMiddleware implements HttpHandler {
    @Override
    public HttpResponse handle(Context context) {
        log.info("Request: {}", context.getRequest());
        return HttpResponse.next();
    }
}
