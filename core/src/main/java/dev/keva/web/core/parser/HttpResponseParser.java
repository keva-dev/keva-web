package dev.keva.web.core.parser;

import dev.keva.web.core.HttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
public class HttpResponseParser {
    public static void parse(HttpResponse response, OutputStream out) throws IOException {
        String responseLine = String.format("HTTP/1.1 %d %s\r\n", response.getHttpResponseStatus().getCode(), response.getHttpResponseStatus().getMessage());
        out.write(responseLine.getBytes());
        Map<String, String> headers = constructHttpHeaders(response);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            String headerLine = String.format("%s: %s\r\n", header.getKey(), header.getValue());
            out.write(headerLine.getBytes());
        }
        out.write("\r\n".getBytes());
        out.write(response.getBody());
        out.flush();
    }

    private static Map<String, String> constructHttpHeaders(HttpResponse response) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        Date date = new Date();
        Map<String, String> headers = response.getHeaders();
        headers.put("Server", "Keva");
        headers.put("Connection:", "Keep-Alive");
        headers.put("Keep-Alive:", "timeout=5, max=1000");
        headers.put("Date", dateFormat.format(date));
        headers.put("Content-Type", response.getContentType());
        headers.put("Content-Length", String.valueOf(response.getBody().length));
        return headers;
    }
}
