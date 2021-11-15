package dev.keva.web.core.annotation;

import dev.keva.web.core.HttpRouter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AnnotatedRouter {
    private String path;
    private HttpRouter router;
}
