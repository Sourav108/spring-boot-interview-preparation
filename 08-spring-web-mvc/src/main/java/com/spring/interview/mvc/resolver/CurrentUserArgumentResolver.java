package com.spring.interview.mvc.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Custom HandlerMethodArgumentResolver resolving authenticated UserPrincipal from HTTP headers.
 */
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    public record UserPrincipal(String userId, String email, String role) {}

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
            && parameter.getParameterType().equals(UserPrincipal.class);
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        String userId = webRequest.getHeader("X-User-Id");
        String email = webRequest.getHeader("X-User-Email");
        String role = webRequest.getHeader("X-User-Role");

        if (userId == null) {
            userId = "anonymous";
            email = "anonymous@system.local";
            role = "ROLE_ANONYMOUS";
        }

        return new UserPrincipal(userId, email, role);
    }
}
