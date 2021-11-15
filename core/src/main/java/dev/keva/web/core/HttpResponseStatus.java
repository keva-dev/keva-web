package dev.keva.web.core;

import lombok.Getter;

public enum HttpResponseStatus {
    OK(200, "OK"),
    CREATED(201, "Created"),
    NO_CONTENT(204, "No Content"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    CONFLICT(409, "Conflict"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GONE(410, "Gone");

    @Getter
    private final int code;
    @Getter
    private final String message;

    HttpResponseStatus(int i, String ok) {
        this.code = i;
        this.message = ok;
    }
}
