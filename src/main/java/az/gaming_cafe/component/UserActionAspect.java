package az.gaming_cafe.component;

import az.gaming_cafe.component.dto.RequestContext;
import az.gaming_cafe.listener.event.UserActionEvent;
import az.gaming_cafe.model.enums.UserActionStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class UserActionAspect {

    private final ApplicationEventPublisher publisher;

    public UserActionAspect(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Around("@annotation(az.gaming_cafe.TrackUserAction)")
    public Object handleUserAction(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String actionName = methodName.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();

        RequestContext context = findContext(joinPoint.getArgs());
        Object result = joinPoint.proceed();
        Long userId = extractUserId(result);

        UserActionEvent event = UserActionEvent.builder()
                .userId(userId)
                .action(actionName)
                .status(UserActionStatus.SUCCESS.name())
                .errorMessage(null)
                .ipAddress(context.getIpAddress())
                .userAgent(context.getUserAgent())
                .build();
        publisher.publishEvent(event);
        return result;
    }

    private RequestContext findContext(Object[] args) {
        return Arrays.stream(args)
                .filter(arg -> arg instanceof RequestContext)
                .map(arg -> (RequestContext) arg)
                .findFirst()
                .orElse(null);
    }

    private Long extractUserId(Object result) {
        try {
            return (Long) result.getClass().getMethod("getId").invoke(result);
            //CHECKSTYLE:OFF
        } catch (Exception e) {
            return null;
        }//CHECKSTYLE:ON
    }
}
