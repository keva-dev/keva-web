package dev.keva.web.json.gson;

import com.google.gson.Gson;
import dev.keva.web.core.json.JsonProcessor;

public class GoogleGson implements JsonProcessor {
    private final Gson gson;

    public GoogleGson() {
        this.gson = new Gson();
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    @Override
    public String toJson(Object src) {
        return gson.toJson(src);
    }
}
