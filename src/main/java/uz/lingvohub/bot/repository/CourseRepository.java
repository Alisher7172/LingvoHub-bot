package uz.lingvohub.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.lingvohub.bot.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByLanguageId(Long languageId);
    List<Course> findByLanguageIdAndTeacherId(Long languageId, Long teacherId);
}
