package uz.lingvohub.bot.lesson.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.lingvohub.bot.lesson.entity.Lesson;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseIdOrderByLessonOrderAsc(Long courseId);
    Optional<Lesson> findByCourseIdAndLessonOrder(Long courseId, Integer lessonOrder);
}
