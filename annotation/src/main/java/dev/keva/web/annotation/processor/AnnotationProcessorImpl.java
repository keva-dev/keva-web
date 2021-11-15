package dev.keva.web.annotation.processor;

import dev.keva.web.annotation.*;
import dev.keva.web.core.annotation.AnnotatedRouter;
import dev.keva.web.core.annotation.AnnotationProcessor;
import dev.keva.web.template.ModelAndView;
import dev.keva.web.core.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Slf4j
public class AnnotationProcessorImpl implements AnnotationProcessor {
    public AnnotatedRouter process(Object annotatedObject) {
        String objectPath = null;
        if (annotatedObject.getClass().isAnnotationPresent(Path.class)) {
            objectPath = annotatedObject.getClass().getAnnotation(Path.class).value();
        }
        HttpRouter router = new HttpRouter();
        for (Method method : annotatedObject.getClass().getDeclaredMethods()) {
            HttpRequestMethod m = null;
            String p = null;
            if (method.isAnnotationPresent(Get.class)) {
                p = method.getAnnotation(Get.class).value();
                m = Get.method;
            } else if (method.isAnnotationPresent(Post.class)) {
                p = method.getAnnotation(Post.class).value();
                m = Post.method;
            } else if (method.isAnnotationPresent(Put.class)) {
                p = method.getAnnotation(Put.class).value();
                m = Put.method;
            } else if (method.isAnnotationPresent(Delete.class)) {
                p = method.getAnnotation(Delete.class).value();
                m = Delete.method;
            } else if (method.isAnnotationPresent(Patch.class)) {
                p = method.getAnnotation(Patch.class).value();
                m = Patch.method;
            }

            if (m != null) {
                if (p == null) {
                    p = "/";
                }
                String path = p;
                router.addHandler(path, m, context -> {
                    try {
                        Object[] parameterObjs = new Object[method.getParameterCount()];
                        for (int i = 0; i < parameterObjs.length; i++) {
                            Parameter[] parameters = method.getParameters();
                            if (parameters[i].isAnnotationPresent(PathParam.class)) {
                                String pathParamStr = parameters[i].getAnnotation(PathParam.class).value();
                                String pathParam = context.getRequest().getPaths().get(pathParamStr);
                                parameterObjs[i] = pathParam;
                            } else if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                                String requestParamStr = parameters[i].getAnnotation(RequestParam.class).value();
                                String requestParam = context.getRequest().getQueries().get(requestParamStr);
                                parameterObjs[i] = requestParam;
                            } else if (parameters[i].isAnnotationPresent(RequestHeader.class)) {
                                String headerName = parameters[i].getAnnotation(RequestHeader.class).value();
                                parameterObjs[i] = context.getRequest().getHeaders().get(headerName);
                            } else if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                                parameterObjs[i] = context.getRequest().getBody(parameters[i].getType());
                            } else if (parameters[i].isAnnotationPresent(ModelAttribute.class)) {
                                parameterObjs[i] = context.getRequest().getBodyFormData(parameters[i].getType());
                            } else if (parameters[i].getType().equals(Context.class)) {
                                parameterObjs[i] = context;
                            } else {
                                parameterObjs[i] = null;
                            }
                        }
                        Object result = method.invoke(annotatedObject, parameterObjs);
                        if (result == null) {
                            return HttpResponse.defaultInternalServerErrorJson(new RuntimeException("Handler return null"));
                        }
                        if (result instanceof String) {
                            return HttpResponse.okText((String) result);
                        } else if (result instanceof Integer) {
                            return HttpResponse.okText(((Integer) result).toString());
                        } else if (result instanceof Long) {
                            return HttpResponse.okText(((Long) result).toString());
                        } else if (result instanceof Double) {
                            return HttpResponse.okText(result.toString());
                        } else if (result instanceof Float) {
                            return HttpResponse.okText(result.toString());
                        } else if (result instanceof Boolean) {
                            return HttpResponse.okText(((Boolean) result).toString());
                        } else if (result instanceof ModelAndView) {
                            ModelAndView modelAndView = (ModelAndView) result;
                            return HttpResponse.template(modelAndView.getStatus(),
                                    modelAndView.getView(),
                                    modelAndView.getObject());
                        }
                        return HttpResponse.okJson(result);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return HttpResponse.defaultInternalServerErrorJson(e);
                    }
                });
            } else {
                if (method.isAnnotationPresent(FilterBefore.class) || method.isAnnotationPresent(PreHandler.class)
                        || method.isAnnotationPresent(FilterAfter.class) || method.isAnnotationPresent(PostHandler.class)) {
                    if (method.getReturnType().equals(HttpHandler.class)) {
                        try {
                            if (method.isAnnotationPresent(FilterBefore.class) || method.isAnnotationPresent(PreHandler.class)) {
                                router.before((HttpHandler) method.invoke(annotatedObject));
                            } else {
                                router.after((HttpHandler) method.invoke(annotatedObject));
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    } else if (method.getReturnType().equals(boolean.class) ||
                            method.getReturnType().equals(void.class) ||
                            method.getReturnType().equals(HttpResponse.class)) {
                        HttpHandler handler = (context) -> {
                            try {
                                Object[] parameterObjs = new Object[method.getParameterCount()];
                                for (int i = 0; i < parameterObjs.length; i++) {
                                    Parameter[] parameters = method.getParameters();
                                    if (parameters[i].getType().equals(Context.class)) {
                                        parameterObjs[i] = context;
                                    } else {
                                        parameterObjs[i] = null;
                                    }
                                }
                                if (method.getReturnType().equals(boolean.class)) {
                                    boolean canNext = (boolean) method.invoke(annotatedObject, parameterObjs);
                                    return canNext ? HttpResponse.next() : HttpResponse.abort();
                                } else if (method.getReturnType().equals(void.class)) {
                                    method.invoke(annotatedObject, parameterObjs);
                                    return HttpResponse.next();
                                } else {
                                    return (HttpResponse) method.invoke(annotatedObject, parameterObjs);
                                }
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                                return HttpResponse.defaultInternalServerErrorJson(e);
                            }
                        };
                        if (method.isAnnotationPresent(FilterBefore.class) || method.isAnnotationPresent(PreHandler.class)) {
                            router.before(handler);
                        } else {
                            router.after(handler);
                        }
                    }
                }
            }
        }
        return new AnnotatedRouter(objectPath, router);
    }
}
