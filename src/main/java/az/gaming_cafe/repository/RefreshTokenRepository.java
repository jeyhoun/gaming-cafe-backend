package az.gaming_cafe.repository;

import az.gaming_cafe.model.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByJti(String jti);

    @Modifying
    @Query("UPDATE RefreshTokenEntity rt SET rt.revoked = true WHERE rt.user.id = :userId AND rt.revoked = false")
    void revokeAllUserTokens(Long userId);

    @Modifying
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.user.id = :userId")
    void deleteAllUserTokens(Long userId);

    @Modifying
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.expiryDate < CURRENT_TIMESTAMP")
    void deleteExpiredTokens();

    @Query("SELECT rt FROM RefreshTokenEntity rt WHERE rt.user.id = :userId AND rt.revoked = false AND rt.expiryDate > CURRENT_TIMESTAMP")
    List<RefreshTokenEntity> findActiveTokensByUserId(Long userId);

    @Query("SELECT rt FROM RefreshTokenEntity rt WHERE rt.jti = :jti AND rt.useCount > :maxUseCount")
    Optional<RefreshTokenEntity> findSuspiciousToken(String jti, int maxUseCount);
}
