package dev.keva.web.core;

import lombok.Getter;

public enum HttpContentType {
    TEXT_HTML("text/html"),
    TEXT_PLAIN("text/plain"),
    APPLICATION_JSON("application/json"),
    APPLICATION_XML("application/xml"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_FORM_URLENCODED("application/form-urlencoded"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    TEXT_XML("text/xml"),
    TEXT_CSS("text/css"),
    TEXT_CSV("text/csv"),
    TEXT_JAVASCRIPT("text/javascript"),
    TEXT_JAVA("text/java");

    @Getter
    private final String value;

    HttpContentType(String contentType) {
        this.value = contentType;
    }
}
