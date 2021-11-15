package dev.keva.web.example;

import dev.keva.web.core.HttpResponse;
import dev.keva.web.core.HttpRouter;
import dev.keva.web.core.HttpServer;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer.builder()
                .host("localhost").port(1234).build()
                .use(new HttpRouter()
                        .use(new RequestLoggerMiddleware())
                        .get("/hello", context -> HttpResponse.okJson(new SampleResponseEntity("Hello World!")))
                        .get("/query", context -> {
                            String queryString = context.getRequest().getQueries().get("hello");
                            return HttpResponse.okText(queryString);
                        })
                        .get("/set/header", context -> HttpResponse.okText("Header set!")
                                .withHeader("Foo", "Bar")
                                .withHeader("Key", "Value"))
                        .post("/data/json", context -> {
                            SampleRequestJson requestBody = context.getRequest().getBody(SampleRequestJson.class);
                            return HttpResponse.okJson(requestBody);
                        })
                        .post("/data/form", context -> {
                            FormDataRequest requestBody = context.getRequest().getBodyFormData(FormDataRequest.class);
                            return HttpResponse.okText("Info: " + requestBody.name + ", " + requestBody.age);
                        })
                        .get("/users/:id/details/:name", context -> {
                            String id = context.getRequest().getPaths().get("id");
                            String name = context.getRequest().getPaths().get("name");
                            return HttpResponse.okText("Hello user " + id + ", your name is " + name);
                        })
                        .get("/template", context -> {
                            Map<String, String> object = new HashMap<>();
                            object.put("name", "John");
                            return HttpResponse.okTemplate("hello", object);
                        })
                        .get("/error", context -> {
                            throw new RuntimeException("Error!");
                        })
                )
                .use("/sub", new HttpRouter()
                        .get("/test", context -> HttpResponse.okText("This is Sub Router")))
                .run();
    }

    @AllArgsConstructor
    public static class SampleRequestJson {
        public String name;
    }

    @AllArgsConstructor
    public static class SampleResponseEntity {
        public String message;
    }

    public static class FormDataRequest {
        public String name;
        public Integer age;
    }
}
