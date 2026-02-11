package az.gaming_cafe.repository;

import az.gaming_cafe.model.entity.UserHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistoryEntity, Long> {

    Optional<UserHistoryEntity> findFirstByUserIdAndActionAndStatusOrderByCreatedAtDesc(Long userId, String action, String status);
}
