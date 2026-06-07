package uz.lingvohub.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.lingvohub.bot.entity.Progress;

import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Optional<Progress> findByUserIdAndLessonId(Long userId, Long lessonId);
    List<Progress> findByUserIdAndCompletedTrue(Long userId);
}
