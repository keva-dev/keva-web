package dev.keva.web.core.parser;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class QueryStringParser {
    public static Map<String, String> parse(String url) {
        if (url == null || url.isEmpty()) {
            return new HashMap<>();
        }
        String[] urlArr = url.split("&");
        Map<String, String> hashMap = new HashMap<>();
        for (String urlElement : urlArr) {
            Map.Entry<String, String> keyVal = splitQueryParameter(urlElement);
            if (keyVal != null) {
                hashMap.put(keyVal.getKey(), keyVal.getValue());
            }
        }
        return hashMap;
    }

    private static Map.Entry<String, String> splitQueryParameter(String it) {
        int idx = it.indexOf('=');
        String key = idx > 0 ? it.substring(0, idx) : it;
        String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : "";
        try {
            return new SimpleImmutableEntry<>(
                    URLDecoder.decode(key, "UTF-8"),
                    URLDecoder.decode(value, "UTF-8")
            );
        } catch (UnsupportedEncodingException e) {
            log.error("Unsupported encoding", e);
        }
        return null;
    }
}
