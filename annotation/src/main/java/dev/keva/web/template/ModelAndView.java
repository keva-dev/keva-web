package dev.keva.web.template;

import dev.keva.web.core.HttpResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelAndView {
    private String view;
    private Object object;
    private HttpResponseStatus status = HttpResponseStatus.OK;

    public ModelAndView(String view) {
        this.view = view;
    }

    public ModelAndView(String view, Object object) {
        this.view = view;
        this.object = object;
    }

    public ModelAndView(String view, Object object, HttpResponseStatus status) {
        this.view = view;
        this.object = object;
        this.status = status;
    }
}
