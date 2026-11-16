package uz.lingvohub.bot.catalog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.lingvohub.bot.course.entity.Course;
import uz.lingvohub.bot.course.repository.CourseRepository;
import uz.lingvohub.bot.language.entity.Language;
import uz.lingvohub.bot.language.repository.LanguageRepository;
import uz.lingvohub.bot.lesson.entity.Lesson;
import uz.lingvohub.bot.lesson.repository.LessonRepository;
import uz.lingvohub.bot.teacher.entity.Teacher;
import uz.lingvohub.bot.teacher.repository.TeacherRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final LanguageRepository languageRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public List<Language> languages() {
        return languageRepository.findAll();
    }

    public List<Teacher> teachers() {
        return teacherRepository.findAll();
    }

    public List<Course> courses(Long languageId, Long teacherId) {
        return courseRepository.findByLanguageIdAndTeacherId(languageId, teacherId);
    }

    public List<Course> coursesByLanguage(Long languageId) {
        return courseRepository.findByLanguageId(languageId);
    }

    public List<Course> allCourses() {
        return courseRepository.findAll();
    }

    public List<Lesson> lessons(Long courseId) {
        return lessonRepository.findByCourseIdOrderByLessonOrderAsc(courseId);
    }

    public Optional<Lesson> lesson(Long lessonId) {
        return lessonRepository.findById(lessonId);
    }

    public Optional<Course> course(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public Optional<Lesson> nextLesson(Long courseId, Integer lessonOrder) {
        return lessonRepository.findByCourseIdAndLessonOrder(courseId, lessonOrder + 1);
    }
}
