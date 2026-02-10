package az.gaming_cafe.scheduler;

import az.gaming_cafe.repository.RevokedTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final RevokedTokenRepository revokedTokenRepository;

    @Scheduled(cron = "0 0 0 15 * ?")
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("ActionLog.cleanupExpiredTokens.start");

        int deletedCount = revokedTokenRepository.deleteExpiredTokens(LocalDateTime.now());

        log.info("ActionLog.cleanupExpiredTokens.end deletedCount: {}", deletedCount);
    }
}
