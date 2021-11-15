package dev.keva.web.example;

import dev.keva.web.annotation.*;
import dev.keva.web.core.Context;
import dev.keva.web.template.ModelAndView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Path("/annotated")
public class AnnotatedController {
    @FilterBefore
    public boolean before(Context context) {
        log.info("This is before middleware");
        return true;
    }

    @Post("/hello/:name")
    public HelloResponse testAnnotations(@PathParam("name") String name,
                                         @RequestBody HelloRequest request,
                                         @RequestParam("qa") String qa) {
        String email = request.email;
        return new HelloResponse("Hello " + name + "! Your email is " + email + " and qa is " + qa);
    }

    @Get("/template/:name")
    public ModelAndView helloTemplate(@PathParam("name") String name) {
        Map<String, String> object = new HashMap<>();
        object.put("name", name);
        return new ModelAndView("hello", object);
    }

    @Post("/register")
    public ModelAndView register(@ModelAttribute FormDataRequest request) {
        return new ModelAndView("register", request);
    }

    @FilterAfter
    public void after(Context context) {
        log.info("This is after middleware");
    }

    @Getter
    public static class FormDataRequest {
        public String name;
        public int age;
    }

    public static class HelloRequest {
        public String email;
    }

    @AllArgsConstructor
    public static class HelloResponse {
        public String message;
    }
}
