package az.gaming_cafe.component;

import az.gaming_cafe.component.dto.RequestContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class RequestContextResolver implements HandlerMethodArgumentResolver {

    private static final String UNKNOWN = "unknown";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(RequestContext.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            return RequestContext.builder()
                    .ipAddress(UNKNOWN)
                    .userAgent(UNKNOWN)
                    .build();
        }

        return RequestContext.builder()
                .ipAddress(extractClientIp(request))
                .userAgent(extractUserAgent(request))
                .build();
    }

    private String extractClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    private String extractUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent : UNKNOWN;
    }
}
