package dev.keva.web.core.template;

import java.io.IOException;

public interface TemplateEngine {
    String process(String templateName, Object model) throws IOException;
}
