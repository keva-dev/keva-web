package dev.keva.web.core.parser;

import dev.keva.web.core.HttpRequest;
import dev.keva.web.core.HttpRequestMethod;
import dev.keva.web.core.exception.RequestParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class HttpRequestParser {
    public static HttpRequest parse(InputStream is) throws IOException {
        HttpRequestMethod method;
        String path, protocol;
        Map<String, String> headers = new HashMap<>(10);
        Map<String, String> queryParams;
        Map<String, String> pathParams = new HashMap<>(0);
        byte[] requestBody = null;

        String requestLine = parseHttpLine(is);
        StringTokenizer requestLineTokens = new StringTokenizer(requestLine);
        String methodStr = requestLineTokens.nextToken().toUpperCase();
        method = HttpRequestMethod.valueOf(methodStr);
        path = requestLineTokens.nextToken();
        protocol = requestLineTokens.nextToken().toUpperCase();
        if (path == null) {
            throw new RequestParserException("Cannot parse HTTP request path");
        }

        String header = parseHttpLine(is);
        while (header.length() > 0) {
            int idx = header.indexOf(":");
            if (idx != -1) {
                headers.put(header.substring(0, idx).trim().toLowerCase(), header.substring(idx + 1).trim().toLowerCase());
            }
            header = parseHttpLine(is);
        }
        int contentLength = 0;
        String contentLengthStr = headers.get("content-length");
        if (contentLengthStr != null && contentLengthStr.length() > 0) {
            contentLength = Integer.parseInt(contentLengthStr);
        }
        if (contentLength > 0) {
            requestBody = new byte[contentLength];
            int numOfReadBytes = is.read(requestBody, 0, contentLength);
            if (numOfReadBytes == -1) {
                throw new RequestParserException("Cannot read request body");
            }
        }
        int indexOfQuestionMark = path.indexOf('?');
        String queryParamsString = indexOfQuestionMark == -1 ? null : path.substring(indexOfQuestionMark + 1);
        queryParams = QueryStringParser.parse(queryParamsString);
        path = path.replaceFirst("\\?.*$", ""); // Remove query string from path
        while (path.endsWith("/")) { // Remove trailing slash from path
            path = path.substring(0, path.length() - 1);
        }
        return new HttpRequest(method, path, protocol, headers, pathParams, queryParams, requestBody);
    }

    private static String parseHttpLine(InputStream is) throws IOException {
        int c;
        StringBuilder s = new StringBuilder();
        do {
            c = is.read();
            if (c == '\n') {
                return s.toString().trim();
            }
            s.append((char) c);
        } while (c != -1);
        throw new RequestParserException("Cannot parse HTTP line");
    }
}
