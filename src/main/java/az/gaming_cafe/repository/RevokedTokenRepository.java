package az.gaming_cafe.repository;

import az.gaming_cafe.model.entity.RevokedTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RevokedTokenRepository extends JpaRepository<RevokedTokenEntity, Long> {

    boolean existsByJti(String jti);

    @Modifying
    @Query("DELETE FROM RevokedTokenEntity rt WHERE rt.expiryDate < :now")
    int deleteExpiredTokens(LocalDateTime now);
}
