package dev.keva.web.example;

import dev.keva.web.core.HttpServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer.builder()
                .host("localhost").port(1234).build()
                .annotated(new AnnotatedController())
                .run();
    }
}
