package dev.keva.web.core;

import dev.keva.web.core.exception.UnsupportedContentTypeException;
import dev.keva.web.core.factory.CoreFactory;
import dev.keva.web.core.json.JsonProcessor;
import dev.keva.web.core.parser.QueryStringParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.Map;

import static dev.keva.web.core.HttpContentType.*;

@AllArgsConstructor
@Getter
@ToString
public class HttpRequest {
    private static final JsonProcessor jsonProcessor = CoreFactory.getJsonProcessor();

    HttpRequestMethod method;
    String path;
    String httpVersion;
    Map<String, String> headers;
    Map<String, String> paths;
    Map<String, String> queries;
    byte[] body;

    public Object getBody() {
        return getBody(Object.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getBodyFormData() {
        return getBody(Map.class);
    }

    public <T> T getBodyFormData(Class<T> clazz) {
        T object = null;
        try {
            object = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {
        }
        Map<String, String> formData = getBodyFormData();
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            if (formData.containsKey(fieldName)) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                Object obj = null;
                if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
                    obj = Integer.parseInt(formData.get(fieldName));
                } else if (type.equals(Short.class) || type.equals(Short.TYPE)) {
                    obj = Short.parseShort(formData.get(fieldName));
                } else if (type.equals(Byte.class) || type.equals(Byte.TYPE)) {
                    obj = Byte.parseByte(formData.get(fieldName));
                } else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
                    obj = Long.parseLong(formData.get(fieldName));
                } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
                    obj = Double.parseDouble(formData.get(fieldName));
                } else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
                    obj = Float.parseFloat(formData.get(fieldName));
                } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
                    obj = Boolean.parseBoolean(formData.get(fieldName));
                } else if (type.equals(String.class)) {
                    obj = formData.get(fieldName);
                } else if (type.equals(Character.class) || type.equals(Character.TYPE)) {
                    obj = formData.get(fieldName).charAt(0);
                }
                try {
                    field.set(object, obj);
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBody(Class<T> clazz) {
        if (body.length == 0) {
            return null;
        }
        String contentType = headers.get("content-type");
        if (contentType.equals(APPLICATION_JSON.getValue())) {
            return jsonProcessor.fromJson(new String(body), clazz);
        } else if (contentType.equals(TEXT_HTML.getValue())) {
            throw new UnsupportedContentTypeException("Content-Type " + TEXT_HTML.getValue() + " is not supported");
        } else if (contentType.equals(APPLICATION_X_WWW_FORM_URLENCODED.getValue())) {
            String bodyString = new String(body);
            Map<String, String> formData = QueryStringParser.parse(bodyString);
            return (T) formData;
        } else if (contentType.equals(MULTIPART_FORM_DATA.getValue())) {
            throw new UnsupportedContentTypeException("Content-Type " + MULTIPART_FORM_DATA.getValue() + " is not supported");
        } else {
            // Default text/plain
            return (T) new String(body);
        }
    }
}
