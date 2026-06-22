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

    public Language updateLanguageName(Long languageId, String name) {
        Language language = languageRepository.findById(languageId).orElseThrow();
        language.setName(name);
        return languageRepository.save(language);
    }

    public Teacher updateTeacherName(Long teacherId, String name) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow();
        teacher.setName(name);
        return teacherRepository.save(teacher);
    }

    public Teacher updateTeacherBio(Long teacherId, String bio) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow();
        teacher.setBio(bio);
        return teacherRepository.save(teacher);
    }

    public Teacher updateTeacherRating(Long teacherId, Double rating) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow();
        teacher.setRating(rating);
        return teacherRepository.save(teacher);
    }

    public Course updateCourseTitle(Long courseId, String title) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        course.setTitle(title);
        return courseRepository.save(course);
    }

    public Course updateCourseDescription(Long courseId, String description) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        course.setDescription(description);
        return courseRepository.save(course);
    }

    public Lesson updateLessonTitle(Long lessonId, String title) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        lesson.setTitle(title);
        return lessonRepository.save(lesson);
    }

    public Lesson updateLessonOrder(Long lessonId, Integer order) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        lesson.setLessonOrder(order);
        return lessonRepository.save(lesson);
    }

    public Lesson updateLessonChannelId(Long lessonId, String channelId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        lesson.setChannelId(channelId);
        return lessonRepository.save(lesson);
    }

    public Lesson updateLessonMessageId(Long lessonId, Integer messageId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        lesson.setMessageId(messageId);
        return lessonRepository.save(lesson);
    }

    public Lesson updateLessonPremium(Long lessonId, boolean premium) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        lesson.setPremium(premium);
        return lessonRepository.save(lesson);
    }

    public long totalLessons() {
        return lessonRepository.count();
    }
}
