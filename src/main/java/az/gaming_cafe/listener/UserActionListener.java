package az.gaming_cafe.listener;

import az.gaming_cafe.listener.event.UserActionEvent;
import az.gaming_cafe.model.entity.UserHistoryEntity;
import az.gaming_cafe.repository.UserHistoryRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserActionListener {

    private final UserHistoryRepository historyRepository;

    public UserActionListener(UserHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Async
    @EventListener
    public void handleUserAction(UserActionEvent event) {
        UserHistoryEntity history = new UserHistoryEntity();

        if (event.getUserId() != null) {
            history.setUserId(event.getUserId());
        }

        history.setAction(event.getAction());
        history.setStatus(event.getStatus());
        history.setErrorMessage(event.getErrorMessage());
        history.setIpAddress(event.getIpAddress());
        history.setUserAgent(event.getUserAgent());

        historyRepository.save(history);
    }
}
