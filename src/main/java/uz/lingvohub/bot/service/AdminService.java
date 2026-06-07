package uz.lingvohub.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.lingvohub.bot.entity.*;
import uz.lingvohub.bot.repository.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final LanguageRepository languageRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public Language addLanguage(String name) {
        Language language = new Language();
        language.setName(name);
        return languageRepository.save(language);
    }

    public Teacher addTeacher(String name, String bio, Double rating) {
        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setBio(bio);
        teacher.setRating(rating);
        return teacherRepository.save(teacher);
    }

    public Course addCourse(Long languageId, Long teacherId, String title, String description) {
        Course course = new Course();
        course.setLanguageId(languageId);
        course.setTeacherId(teacherId);
        course.setTitle(title);
        course.setDescription(description);
        return courseRepository.save(course);
    }

    public Lesson addLesson(Long courseId, String title, Integer order, String channelId, Integer messageId, boolean premium) {
        Lesson lesson = new Lesson();
        lesson.setCourseId(courseId);
        lesson.setTitle(title);
        lesson.setLessonOrder(order);
        lesson.setChannelId(channelId);
        lesson.setMessageId(messageId);
        lesson.setPremium(premium);
        lesson.setDescription("");
        return lessonRepository.save(lesson);
    }

    public long totalLessons() {
        return lessonRepository.count();
    }
}
