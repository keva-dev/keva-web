package dev.keva.web.core.factory;

import dev.keva.web.core.annotation.AnnotationProcessor;
import dev.keva.web.core.json.JsonProcessor;
import dev.keva.web.core.template.TemplateEngine;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CoreFactory {
    @Setter
    @Getter
    private static AnnotationProcessor annotationProcessor;

    @Setter
    @Getter
    private static JsonProcessor jsonProcessor;

    @Setter
    @Getter
    private static TemplateEngine templateEngine;

    public static void loadDefault() {
        if (annotationProcessor == null) {
            String annotationProcessorClazzName = "dev.keva.web.annotation.processor.AnnotationProcessorImpl";
            try {
                Class<?> annotationProcessorClazz = Class.forName(annotationProcessorClazzName);
                annotationProcessor = (AnnotationProcessor) annotationProcessorClazz.getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException e) {
                log.error("Class " + annotationProcessorClazzName + " not found, set to null");
            } catch (Exception e) {
                log.error("Error while creating " + annotationProcessorClazzName + " instance, set to null");
            }
        }

        if (jsonProcessor == null) {
            String gsonClazzName = "dev.keva.web.json.gson.GoogleGson";
            try {
                Class<?> gsonClazz = Class.forName(gsonClazzName);
                jsonProcessor = (JsonProcessor) gsonClazz.getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException e) {
                log.error("Class " + gsonClazzName + " not found, set to null");
            } catch (Exception e) {
                log.error("Error while creating " + gsonClazzName + " instance, set to null");
            }
        }

        if (templateEngine == null) {
            String handlebarsClazzName = "dev.keva.web.template.handlebars.HandlebarsEngine";
            try {
                Class<?> handlebarsClazz = Class.forName(handlebarsClazzName);
                templateEngine = (TemplateEngine) handlebarsClazz.getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException e) {
                log.error("Class " + handlebarsClazzName + " not found, set to null");
            } catch (Exception e) {
                log.error("Error while creating " + handlebarsClazzName + " instance, set to null");
            }
        }
    }
}
