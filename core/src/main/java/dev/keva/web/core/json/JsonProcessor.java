package dev.keva.web.core.json;

public interface JsonProcessor {
    <T> T fromJson(String json, Class<T> clazz);

    String toJson(Object src);
}
