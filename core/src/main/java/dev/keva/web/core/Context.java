package dev.keva.web.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class Context {
    @Getter
    private final HttpRequest request;
    private final Map<Object, Object> attributes = new HashMap<>(0);
    @Getter
    @Setter
    private HttpResponse response;

    public void setAttribute(Object key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(Object key) {
        return attributes.get(key);
    }
}
