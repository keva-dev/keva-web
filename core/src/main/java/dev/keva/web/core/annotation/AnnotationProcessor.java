package dev.keva.web.core.annotation;

public interface AnnotationProcessor {
    AnnotatedRouter process(Object annotatedObject);
}
