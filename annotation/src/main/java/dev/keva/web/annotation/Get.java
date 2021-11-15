package dev.keva.web.annotation;

import dev.keva.web.core.HttpRequestMethod;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Get {
    HttpRequestMethod method = HttpRequestMethod.GET;

    String value();
}
