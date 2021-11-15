package dev.keva.web.template.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import dev.keva.web.core.template.TemplateEngine;

import java.io.IOException;

public class HandlebarsEngine implements TemplateEngine {
    private final Handlebars handlebars;

    public HandlebarsEngine() {
        handlebars = new Handlebars();
        handlebars.prettyPrint(false);
    }

    @Override
    public String process(String templateName, Object model) throws IOException {
        Template template = handlebars.compile(templateName);
        return template.apply(model);
    }
}
