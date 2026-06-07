package uz.lingvohub.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.lingvohub.bot.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class KeyboardFactory {

    public InlineKeyboardMarkup languageKeyboard(List<Language> languages) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (Language language : languages) {
            rows.add(List.of(button(language.getName(), "lang_" + language.getId())));
        }
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    public InlineKeyboardMarkup teacherKeyboard(List<Teacher> teachers, Long languageId) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (Teacher teacher : teachers) {
            rows.add(List.of(button(teacher.getName(), "teacher_" + teacher.getId() + "_" + languageId)));
        }
        rows.add(List.of(button("⬅️ Orqaga", "back_lang")));
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    public InlineKeyboardMarkup courseKeyboard(List<Course> courses, Long languageId) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (Course course : courses) {
            rows.add(List.of(button(course.getTitle(), "course_" + course.getId())));
        }
        rows.add(List.of(button("⬅️ Orqaga", "back_teacher_" + languageId)));
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    public InlineKeyboardMarkup lessonsKeyboard(List<Lesson> lessons, Set<Long> completed, boolean premiumActive, Long courseId) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        int index = 1;
        int total = lessons.size();
        for (Lesson lesson : lessons) {
            boolean locked = lesson.isPremium() && !premiumActive;
            boolean done = completed.contains(lesson.getId());
            String title = String.format("%d/%d %s %s%s", index, total, lesson.getTitle(), locked ? "🔒 " : "", done ? "✅" : "");
            rows.add(List.of(button(title.trim(), "lesson_" + lesson.getId())));
            index++;
        }
        rows.add(List.of(button("⬅️ Orqaga", "back_course")));
        rows.add(List.of(button("🏠 Bosh menyu", "back_start")));
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    public InlineKeyboardMarkup nextLessonKeyboard(Long courseId, Integer lessonOrder) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(button("⏭ Keyingi dars", "next_" + courseId + "_" + lessonOrder)));
        rows.add(List.of(button("📚 Darslar ro'yxati", "back_lesson_list_" + courseId)));
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    public InlineKeyboardMarkup adminMenuKeyboard() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(button("➕ Til qo'shish", "admin_add_language")));
        rows.add(List.of(button("➕ O'qituvchi qo'shish", "admin_add_teacher")));
        rows.add(List.of(button("➕ Kurs qo'shish", "admin_add_course")));
        rows.add(List.of(button("➕ Dars qo'shish", "admin_add_lesson")));
        rows.add(List.of(button("📊 Stats", "admin_stats")));
        rows.add(List.of(button("👥 Users", "admin_users")));
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    public InlineKeyboardMarkup selectLanguageForAdmin(List<Language> languages, String action) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (Language language : languages) {
            rows.add(List.of(button(language.getName(), "admin_lang_" + action + "_" + language.getId())));
        }
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    public InlineKeyboardMarkup selectTeacherForAdmin(List<Teacher> teachers) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (Teacher teacher : teachers) {
            rows.add(List.of(button(teacher.getName(), "admin_teacher_" + teacher.getId())));
        }
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    public InlineKeyboardMarkup selectCourseForAdmin(List<Course> courses) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (Course course : courses) {
            rows.add(List.of(button(course.getTitle(), "admin_course_" + course.getId())));
        }
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    public InlineKeyboardMarkup premiumToggleKeyboard() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(button("Bepul", "admin_lesson_premium_false"), button("Premium", "admin_lesson_premium_true")));
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    private InlineKeyboardButton button(String text, String callbackData) {
        return InlineKeyboardButton.builder().text(text).callbackData(callbackData).build();
    }
}
