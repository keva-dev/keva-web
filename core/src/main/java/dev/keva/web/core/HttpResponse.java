package dev.keva.web.core;

import dev.keva.web.core.factory.CoreFactory;
import dev.keva.web.core.json.JsonProcessor;
import dev.keva.web.core.template.TemplateEngine;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static dev.keva.web.core.HttpContentType.*;

public class HttpResponse {
    private static final JsonProcessor jsonProcessor = CoreFactory.getJsonProcessor();
    private static final TemplateEngine templateEngine = CoreFactory.getTemplateEngine();

    @Getter
    private final Map<String, String> headers = new HashMap<>(10);
    @Getter
    @Setter
    private Boolean allowNext = null;
    @Getter
    @Setter
    private HttpResponseStatus httpResponseStatus = null;
    @Getter
    @Setter
    private String contentType = null;
    @Getter
    private byte[] body = null;

    private HttpResponse(boolean allowNext) {
        this.allowNext = allowNext;
    }

    private HttpResponse(HttpResponseStatus httpResponseStatus, String contentType, byte[] body) {
        this.httpResponseStatus = httpResponseStatus;
        this.contentType = contentType;
        this.body = body;
    }

    public static HttpResponse defaultNotFound() {
        return new HttpResponse(HttpResponseStatus.NOT_FOUND, TEXT_PLAIN.getValue(), "Not found".getBytes());
    }

    public static HttpResponse defaultNotFoundHtml() {
        return new HttpResponse(HttpResponseStatus.NOT_FOUND, TEXT_HTML.getValue(), "<h1>Not found</h1>".getBytes());
    }

    public static HttpResponse defaultNotFoundJson() {
        String body = jsonProcessor.toJson("{\"error\":\"not found\"}");
        return new HttpResponse(HttpResponseStatus.NOT_FOUND, APPLICATION_JSON.getValue(), body.getBytes());
    }

    public static HttpResponse defaultBadRequestHtml() {
        return new HttpResponse(HttpResponseStatus.BAD_REQUEST, TEXT_HTML.getValue(), "<h1>Bad request</h1>".getBytes());
    }

    public static HttpResponse defaultBadRequestJson() {
        String body = jsonProcessor.toJson("{\"error\":\"bad request\"}");
        return new HttpResponse(HttpResponseStatus.BAD_REQUEST, TEXT_HTML.getValue(), body.getBytes());
    }

    public static HttpResponse defaultInternalServerErrorJson(@NonNull Exception e) {
        String body = jsonProcessor.toJson(e);
        return new HttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, APPLICATION_JSON.getValue(), body.getBytes());
    }

    public static HttpResponse next() {
        return new HttpResponse(true);
    }

    public static HttpResponse abort() {
        return new HttpResponse(false)
                .withStatus(HttpResponseStatus.BAD_REQUEST)
                .withContentType(TEXT_PLAIN.getValue())
                .withBody("Bad request");
    }

    public static HttpResponse of(@NonNull HttpResponseStatus status, @NonNull String contentType, @NonNull byte[] body) {
        return new HttpResponse(status, contentType, body);
    }

    public static HttpResponse text(@NonNull HttpResponseStatus status, @NonNull String body) {
        return new HttpResponse(HttpResponseStatus.OK, TEXT_PLAIN.getValue(), body.getBytes(StandardCharsets.UTF_8));
    }

    public static HttpResponse json(@NonNull HttpResponseStatus status, @NonNull Object json) {
        String body = jsonProcessor.toJson(json);
        return new HttpResponse(HttpResponseStatus.OK, TEXT_PLAIN.getValue(), body.getBytes(StandardCharsets.UTF_8));
    }

    public static HttpResponse template(@NonNull HttpResponseStatus status, @NonNull String templateName, @NonNull Object object) {
        try {
            String templateString = templateEngine.process(templateName, object);
            return new HttpResponse(status, TEXT_HTML.getValue(), templateString.getBytes());
        } catch (IOException e) {
            return defaultInternalServerErrorJson(e);
        }
    }

    public static HttpResponse okText(@NonNull String body) {
        return new HttpResponse(HttpResponseStatus.OK, TEXT_PLAIN.getValue(), body.getBytes(StandardCharsets.UTF_8));
    }

    public static HttpResponse okJson(@NonNull Object json) {
        String body = jsonProcessor.toJson(json);
        return new HttpResponse(HttpResponseStatus.OK, APPLICATION_JSON.getValue(), body.getBytes(StandardCharsets.UTF_8));
    }

    public static HttpResponse okTemplate(@NonNull String templateName, @NonNull Object object) {
        return template(HttpResponseStatus.OK, templateName, object);
    }

    public static HttpResponse badRequestText(@NonNull String body) {
        return new HttpResponse(HttpResponseStatus.BAD_REQUEST, TEXT_PLAIN.getValue(), body.getBytes(StandardCharsets.UTF_8));
    }

    public static HttpResponse badRequestJson(@NonNull Object json) {
        String body = jsonProcessor.toJson(json);
        return new HttpResponse(HttpResponseStatus.BAD_REQUEST, APPLICATION_JSON.getValue(), body.getBytes(StandardCharsets.UTF_8));
    }

    public static HttpResponse badRequestTemplate(@NonNull String templateName, @NonNull Object object) {
        return template(HttpResponseStatus.BAD_REQUEST, templateName, object);
    }

    public static HttpResponse notFoundText(@NonNull String body) {
        return new HttpResponse(HttpResponseStatus.NOT_FOUND, TEXT_PLAIN.getValue(), body.getBytes(StandardCharsets.UTF_8));
    }

    public static HttpResponse notFoundJson(@NonNull Object json) {
        String body = jsonProcessor.toJson(json);
        return new HttpResponse(HttpResponseStatus.NOT_FOUND, APPLICATION_JSON.getValue(), body.getBytes(StandardCharsets.UTF_8));
    }

    public static HttpResponse notFoundTemplate(@NonNull String templateName, @NonNull Object object) {
        return template(HttpResponseStatus.NOT_FOUND, templateName, object);
    }

    public HttpResponse withStatus(HttpResponseStatus status) {
        this.httpResponseStatus = status;
        return this;
    }

    public HttpResponse withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public HttpResponse withBody(String body) {
        this.body = body.getBytes(StandardCharsets.UTF_8);
        return this;
    }

    public HttpResponse withBody(byte[] body) {
        this.body = body;
        return this;
    }

    public HttpResponse withHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }
}
