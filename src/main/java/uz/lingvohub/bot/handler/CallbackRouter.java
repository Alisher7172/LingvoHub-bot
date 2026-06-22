package uz.lingvohub.bot.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.lingvohub.bot.config.TelegramProperties;
import uz.lingvohub.bot.controller.BotGateway;
import uz.lingvohub.bot.entity.Lesson;
import uz.lingvohub.bot.entity.User;
import uz.lingvohub.bot.keyboard.KeyboardFactory;
import uz.lingvohub.bot.service.*;
import uz.lingvohub.bot.state.AdminSession;
import uz.lingvohub.bot.state.AdminState;
import uz.lingvohub.bot.state.UserSession;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CallbackRouter {

    private final TelegramProperties telegramProperties;
    private final UserService userService;
    private final CatalogService catalogService;
    private final ProgressService progressService;
    private final AdminService adminService;
    private final SessionService sessionService;
    private final KeyboardFactory keyboardFactory;

    public void route(Update update, BotGateway botGateway) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String data = callbackQuery.getData();
        Long telegramId = callbackQuery.getFrom().getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        User user = userService.getOrCreate(telegramId, callbackQuery.getFrom().getUserName());
        UserSession userSession = sessionService.userSession(telegramId);
        AdminSession adminSession = sessionService.adminSession(telegramId);

        try {
            if (data.startsWith("lang_")) {
                Long languageId = Long.parseLong(data.split("_")[1]);
                userSession.setSelectedLanguageId(languageId);
                user.setLanguage(
                        catalogService.languages().stream().filter(l -> l.getId().equals(languageId)).findFirst().map(l -> l.getName()).orElse("English")
                );
                userService.save(user);
                botGateway.editText(chatId, messageId, "O'qituvchini tanlang:", keyboardFactory.teacherKeyboard(catalogService.teachers(), languageId));
            } else if (data.startsWith("teacher_")) {
                String[] parts = data.split("_");
                Long teacherId = Long.parseLong(parts[1]);
                Long languageId = Long.parseLong(parts[2]);
                userSession.setSelectedTeacherId(teacherId);
                userSession.setSelectedLanguageId(languageId);
                botGateway.editText(chatId, messageId, "Kursni tanlang:", keyboardFactory.courseKeyboard(catalogService.courses(languageId, teacherId), languageId));
            } else if (data.startsWith("course_")) {
                Long courseId = Long.parseLong(data.split("_")[1]);
                userSession.setSelectedCourseId(courseId);
                List<Lesson> lessons = catalogService.lessons(courseId);
                Set<Long> completed = progressService.completedLessonIds(user);
                botGateway.editText(chatId, messageId, "Darslar ro'yxati:", keyboardFactory.lessonsKeyboard(lessons, completed, user.isPremiumActive(), courseId));
            } else if (data.startsWith("lesson_")) {
                Long lessonId = Long.parseLong(data.split("_")[1]);
                Lesson lesson = catalogService.lesson(lessonId).orElseThrow();
                if (lesson.isPremium() && !user.isPremiumActive()) {
                    botGateway.answerCallback(callbackQuery.getId(), "Bu premium dars. Avval obuna oling.");
                    return;
                }
                if (!botGateway.copyLesson(chatId, lesson.getChannelId(), lesson.getMessageId())) {
                    botGateway.sendText(chatId, "Darsni yuborib bo'lmadi. Admin channel_id va message_id ni tekshirsin.", null);
                    return;
                }
                progressService.markCompleted(user, lesson);
                botGateway.sendText(chatId, "Dars yuborildi.", keyboardFactory.nextLessonKeyboard(lesson.getCourseId(), lesson.getLessonOrder()));
            } else if (data.startsWith("next_")) {
                String[] parts = data.split("_");
                Long courseId = Long.parseLong(parts[1]);
                Integer order = Integer.parseInt(parts[2]);
                Lesson next = catalogService.nextLesson(courseId, order).orElse(null);
                if (next == null) {
                    botGateway.answerCallback(callbackQuery.getId(), "Oxirgi darsga yetdingiz.");
                    return;
                }
                if (next.isPremium() && !user.isPremiumActive()) {
                    botGateway.answerCallback(callbackQuery.getId(), "Keyingi dars premium.");
                    return;
                }
                if (!botGateway.copyLesson(chatId, next.getChannelId(), next.getMessageId())) {
                    botGateway.sendText(chatId, "Darsni yuborib bo'lmadi. Admin channel_id va message_id ni tekshirsin.", null);
                    return;
                }
                progressService.markCompleted(user, next);
                botGateway.sendText(chatId, "Keyingi dars yuborildi.", keyboardFactory.nextLessonKeyboard(next.getCourseId(), next.getLessonOrder()));
            } else if ("back_start".equals(data) || "back_lang".equals(data)) {
                botGateway.editText(chatId, messageId, "Tilni tanlang:", keyboardFactory.languageKeyboard(catalogService.languages()));
            } else if (data.startsWith("back_teacher_")) {
                Long languageId = Long.parseLong(data.split("_")[2]);
                botGateway.editText(chatId, messageId, "O'qituvchini tanlang:", keyboardFactory.teacherKeyboard(catalogService.teachers(), languageId));
            } else if (data.startsWith("back_lesson_list_")) {
                Long courseId = Long.parseLong(data.split("_")[3]);
                List<Lesson> lessons = catalogService.lessons(courseId);
                Set<Long> completed = progressService.completedLessonIds(user);
                botGateway.sendText(chatId, "Darslar ro'yxati:", keyboardFactory.lessonsKeyboard(lessons, completed, user.isPremiumActive(), courseId));
            } else if ("admin_add_language".equals(data) && isAdmin(telegramId)) {
                adminSession.setState(AdminState.ADD_LANGUAGE_NAME);
                botGateway.sendText(chatId, "Til nomini kiriting", null);
            } else if ("admin_add_teacher".equals(data) && isAdmin(telegramId)) {
                adminSession.setState(AdminState.ADD_TEACHER_NAME);
                botGateway.sendText(chatId, "Teacher nomini kiriting:", null);
            } else if ("admin_add_course".equals(data) && isAdmin(telegramId)) {
                botGateway.sendText(chatId, "Tilni tanlang:", keyboardFactory.selectLanguageForAdmin(catalogService.languages(), "course"));
            } else if ("back_course".equals(data)) {
                if (userSession.getSelectedLanguageId() != null && userSession.getSelectedTeacherId() != null) {
                    botGateway.editText(chatId, messageId, "Kurs tanlang:",
                            keyboardFactory.courseKeyboard(
                                    catalogService.courses(userSession.getSelectedLanguageId(), userSession.getSelectedTeacherId()),
                                    userSession.getSelectedLanguageId()
                            ));
                }
            } else if ("admin_add_lesson".equals(data) && isAdmin(telegramId)) {
                botGateway.sendText(chatId, "Kursni tanlang:", keyboardFactory.selectCourseForAdmin(catalogService.allCourses()));
            } else if (data.startsWith("admin_lang_course_") && isAdmin(telegramId)) {
                Long languageId = Long.parseLong(data.split("_")[3]);
                adminSession.setSelectedLanguageId(languageId);
                botGateway.sendText(chatId, "Teacherni tanlang:", keyboardFactory.selectTeacherForAdmin(catalogService.teachers()));
            } else if (data.startsWith("admin_teacher_") && isAdmin(telegramId)) {
                Long teacherId = Long.parseLong(data.split("_")[2]);
                adminSession.setSelectedTeacherId(teacherId);
                adminSession.setState(AdminState.ADD_COURSE_TITLE);
                botGateway.sendText(chatId, "Kurs nomini kiriting:", null);
            } else if (data.startsWith("admin_course_") && isAdmin(telegramId)) {
                Long courseId = Long.parseLong(data.split("_")[2]);
                adminSession.setSelectedCourseId(courseId);
                adminSession.setState(AdminState.ADD_LESSON_TITLE);
                botGateway.sendText(chatId, "Dars nomini kiriting:", null);
            } else if (data.startsWith("admin_lesson_premium_") && isAdmin(telegramId)) {
                boolean premium = Boolean.parseBoolean(data.split("_")[3]);
                adminSession.setPendingLessonPremium(premium);
                adminService.addLesson(
                        adminSession.getSelectedCourseId(),
                        adminSession.getPendingLessonTitle(),
                        adminSession.getPendingLessonOrder(),
                        adminSession.getPendingLessonChannelId(),
                        adminSession.getPendingLessonMessageId(),
                        adminSession.isPendingLessonPremium()
                );
                adminSession.setState(AdminState.NONE);
                botGateway.sendText(chatId, "Dars saqlandi.", keyboardFactory.adminMenuKeyboard());
            } else if ("admin_edit".equals(data) && isAdmin(telegramId)) {
                botGateway.sendText(chatId, "Nimani tahrirlaysiz?", keyboardFactory.adminEditMenuKeyboard());
            } else if ("admin_edit_language".equals(data) && isAdmin(telegramId)) {
                botGateway.sendText(chatId, "Tilni tanlang:", keyboardFactory.selectLanguageForEdit(catalogService.languages()));
            } else if (data.startsWith("admin_edit_language_item_") && isAdmin(telegramId)) {
                Long languageId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedLanguageId(languageId);
                botGateway.sendText(chatId, "Qaysi maydon tahrirlansin?", keyboardFactory.languageEditFieldsKeyboard(languageId));
            } else if (data.startsWith("admin_edit_language_name_") && isAdmin(telegramId)) {
                Long languageId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedLanguageId(languageId);
                adminSession.setState(AdminState.EDIT_LANGUAGE_NAME);
                botGateway.sendText(chatId, "Yangi til nomini kiriting:", null);
            } else if ("admin_edit_teacher".equals(data) && isAdmin(telegramId)) {
                botGateway.sendText(chatId, "Teacherni tanlang:", keyboardFactory.selectTeacherForEdit(catalogService.teachers()));
            } else if (data.startsWith("admin_edit_teacher_item_") && isAdmin(telegramId)) {
                Long teacherId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedTeacherId(teacherId);
                botGateway.sendText(chatId, "Qaysi maydon tahrirlansin?", keyboardFactory.teacherEditFieldsKeyboard(teacherId));
            } else if (data.startsWith("admin_edit_teacher_name_") && isAdmin(telegramId)) {
                Long teacherId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedTeacherId(teacherId);
                adminSession.setState(AdminState.EDIT_TEACHER_NAME);
                botGateway.sendText(chatId, "Yangi teacher nomini kiriting:", null);
            } else if (data.startsWith("admin_edit_teacher_bio_") && isAdmin(telegramId)) {
                Long teacherId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedTeacherId(teacherId);
                adminSession.setState(AdminState.EDIT_TEACHER_BIO);
                botGateway.sendText(chatId, "Yangi teacher bio kiriting:", null);
            } else if (data.startsWith("admin_edit_teacher_rating_") && isAdmin(telegramId)) {
                Long teacherId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedTeacherId(teacherId);
                adminSession.setState(AdminState.EDIT_TEACHER_RATING);
                botGateway.sendText(chatId, "Yangi teacher reytingini kiriting (masalan 4.8):", null);
            } else if ("admin_edit_course".equals(data) && isAdmin(telegramId)) {
                botGateway.sendText(chatId, "Kursni tanlang:", keyboardFactory.selectCourseForEdit(catalogService.allCourses()));
            } else if (data.startsWith("admin_edit_course_item_") && isAdmin(telegramId)) {
                Long courseId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedCourseId(courseId);
                botGateway.sendText(chatId, "Qaysi maydon tahrirlansin?", keyboardFactory.courseEditFieldsKeyboard(courseId));
            } else if (data.startsWith("admin_edit_course_title_") && isAdmin(telegramId)) {
                Long courseId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedCourseId(courseId);
                adminSession.setState(AdminState.EDIT_COURSE_TITLE);
                botGateway.sendText(chatId, "Yangi kurs nomini kiriting:", null);
            } else if (data.startsWith("admin_edit_course_description_") && isAdmin(telegramId)) {
                Long courseId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedCourseId(courseId);
                adminSession.setState(AdminState.EDIT_COURSE_DESCRIPTION);
                botGateway.sendText(chatId, "Yangi kurs tavsifini kiriting:", null);
            } else if ("admin_edit_lesson".equals(data) && isAdmin(telegramId)) {
                botGateway.sendText(chatId, "Kursni tanlang:", keyboardFactory.selectLessonCourseForEdit(catalogService.allCourses()));
            } else if (data.startsWith("admin_edit_lesson_course_") && isAdmin(telegramId)) {
                Long courseId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedCourseId(courseId);
                botGateway.sendText(chatId, "Darsni tanlang:", keyboardFactory.selectLessonForEdit(catalogService.lessons(courseId)));
            } else if (data.startsWith("admin_edit_lesson_item_") && isAdmin(telegramId)) {
                Long lessonId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedLessonId(lessonId);
                botGateway.sendText(chatId, "Qaysi maydon tahrirlansin?", keyboardFactory.lessonEditFieldsKeyboard(lessonId));
            } else if (data.startsWith("admin_edit_lesson_title_") && isAdmin(telegramId)) {
                Long lessonId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedLessonId(lessonId);
                adminSession.setState(AdminState.EDIT_LESSON_TITLE);
                botGateway.sendText(chatId, "Yangi dars nomini kiriting:", null);
            } else if (data.startsWith("admin_edit_lesson_order_") && isAdmin(telegramId)) {
                Long lessonId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedLessonId(lessonId);
                adminSession.setState(AdminState.EDIT_LESSON_ORDER);
                botGateway.sendText(chatId, "Yangi dars tartib raqamini kiriting:", null);
            } else if (data.startsWith("admin_edit_lesson_channel_") && isAdmin(telegramId)) {
                Long lessonId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedLessonId(lessonId);
                adminSession.setState(AdminState.EDIT_LESSON_CHANNEL_ID);
                botGateway.sendText(chatId, "Yangi channel_id kiriting:", null);
            } else if (data.startsWith("admin_edit_lesson_message_") && isAdmin(telegramId)) {
                Long lessonId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedLessonId(lessonId);
                adminSession.setState(AdminState.EDIT_LESSON_MESSAGE_ID);
                botGateway.sendText(chatId, "Yangi message_id kiriting:", null);
            } else if (data.startsWith("admin_edit_lesson_premium_true_") && isAdmin(telegramId)) {
                Long lessonId = Long.parseLong(data.split("_")[5]);
                adminService.updateLessonPremium(lessonId, true);
                adminSession.setState(AdminState.NONE);
                botGateway.sendText(chatId, "Dars yangilandi.", keyboardFactory.adminMenuKeyboard());
            } else if (data.startsWith("admin_edit_lesson_premium_false_") && isAdmin(telegramId)) {
                Long lessonId = Long.parseLong(data.split("_")[5]);
                adminService.updateLessonPremium(lessonId, false);
                adminSession.setState(AdminState.NONE);
                botGateway.sendText(chatId, "Dars yangilandi.", keyboardFactory.adminMenuKeyboard());
            } else if (data.startsWith("admin_edit_lesson_premium_") && isAdmin(telegramId)) {
                Long lessonId = Long.parseLong(data.split("_")[4]);
                adminSession.setSelectedLessonId(lessonId);
                botGateway.sendText(chatId, "Bu dars premiummi?", keyboardFactory.lessonPremiumEditKeyboard(lessonId));
            } else if ("admin_stats".equals(data) && isAdmin(telegramId)) {
                String stats = "📊 Statistikalar:\\nFoydalanuvchilar: " + userService.count() + "\\nDarslar: " + adminService.totalLessons();
                botGateway.sendText(chatId, stats, keyboardFactory.adminMenuKeyboard());
            } else if ("admin_users".equals(data) && isAdmin(telegramId)) {
                botGateway.sendText(chatId, "Jami foydalanuvchilar: " + userService.count(), keyboardFactory.adminMenuKeyboard());
            }
        } catch (Exception e) {
            botGateway.sendText(chatId, "Xatolik yuz berdi. Qayta urinib ko'ring.", null);
        }
    }

    private boolean isAdmin(Long telegramId) {
        return telegramProperties.getAdminIds().contains(telegramId);
    }
}
