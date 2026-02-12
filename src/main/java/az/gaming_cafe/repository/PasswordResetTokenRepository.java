package az.gaming_cafe.repository;

import az.gaming_cafe.model.entity.PasswordResetTokenEntity;
import az.gaming_cafe.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    Optional<PasswordResetTokenEntity> findByUser(UserEntity user);

    @Query("SELECT t FROM PasswordResetTokenEntity t JOIN FETCH t.user WHERE t.token = :token")
    Optional<PasswordResetTokenEntity> findByTokenWithUser(@Param("token") String token);
}
