package dev.keva.web.core;

import java.util.Map;
import java.util.Set;

import static dev.keva.web.core.HttpContentType.APPLICATION_JSON;
import static dev.keva.web.core.HttpContentType.TEXT_HTML;

public class Dispatcher {
    public static HttpResponse dispatchRequest(HttpRequest request,
                                               Map<String, Set<HttpHandler>> preHandlers,
                                               Map<String, Map<HttpRequestMethod,
                                                       HttpHandler>> handlers,
                                               Map<String, Set<HttpHandler>> postHandlers) {
        Context context = new Context(request);
        HttpResponse response;
        try {
            response = runHandlerChain(context, preHandlers);
            if (response != null) {
                return response;
            }
            response = matchingExact(context, handlers);
            if (response == null) {
                response = matchingWildcard(context, handlers);
                if (response == null) {
                    response = notFound(context);
                }
            }
            context.setResponse(response);
            response = runHandlerChain(context, postHandlers);
            if (response != null) {
                return response;
            }
            return context.getResponse();
        } catch (Exception e) {
            return HttpResponse.defaultInternalServerErrorJson(e);
        }
    }

    private static HttpResponse runHandlerChain(Context context, Map<String, Set<HttpHandler>> handlerChain) {
        for (String handlerPath : handlerChain.keySet()) {
            if (handlerPath.equals("") || context.getRequest().getPath().startsWith(handlerPath)) {
                Set<HttpHandler> handlers = handlerChain.get(handlerPath);
                for (HttpHandler handler : handlers) {
                    HttpResponse response = handler.handle(context);
                    if (response.getAllowNext() != null) {
                        if (response.getAllowNext()) {
                            context.setResponse(context.getResponse());
                        } else {
                            return response;
                        }
                    } else {
                        context.setResponse(response);
                    }
                }
            }
        }
        return null;
    }

    private static HttpResponse matchingExact(Context context, Map<String, Map<HttpRequestMethod, HttpHandler>> handlers) {
        Map<HttpRequestMethod, HttpHandler> exactHandlerMap = handlers.get(context.getRequest().getPath());
        if (exactHandlerMap != null) {
            HttpHandler handler = exactHandlerMap.get(context.getRequest().getMethod());
            if (handler != null) {
                return handler.handle(context);
            }
        }
        return null;
    }

    private static HttpResponse matchingWildcard(Context context, Map<String, Map<HttpRequestMethod, HttpHandler>> handlers) {
        String[] pathSplit = context.getRequest().path.split("/");
        for (String routePath : handlers.keySet()) {
            if (!routePath.contains(":")) {
                continue;
            }
            String[] routePathSplit = routePath.split("/");
            if (routePathSplit.length != pathSplit.length) {
                continue;
            }
            boolean isMatch = true;
            for (int i = 0; i < routePathSplit.length; i++) {
                if (routePathSplit[i].startsWith(":")) {
                    context.getRequest().getPaths().put(routePathSplit[i].substring(1), pathSplit[i]);
                } else {
                    if (!routePathSplit[i].equals(pathSplit[i])) {
                        isMatch = false;
                        break;
                    }
                }
            }
            if (isMatch) {
                Map<HttpRequestMethod, HttpHandler> wildcardHandlerMap = handlers.get(routePath);
                HttpHandler handler = wildcardHandlerMap.get(context.getRequest().getMethod());
                if (handler != null) {
                    return handler.handle(context);
                }
            }
        }
        return null;
    }

    private static HttpResponse notFound(Context context) {
        String accept = context.getRequest().getHeaders().get("accept");
        if (accept != null) {
            if (accept.contains(TEXT_HTML.getValue())) {
                return HttpResponse.defaultNotFoundHtml();
            } else if (accept.contains(APPLICATION_JSON.getValue())) {
                return HttpResponse.defaultNotFoundJson();
            }
        }
        return HttpResponse.defaultNotFound();
    }
}
