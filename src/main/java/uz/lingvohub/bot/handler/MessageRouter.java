package uz.lingvohub.bot.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.lingvohub.bot.config.TelegramProperties;
import uz.lingvohub.bot.controller.BotGateway;
import uz.lingvohub.bot.keyboard.KeyboardFactory;
import uz.lingvohub.bot.service.*;
import uz.lingvohub.bot.state.AdminSession;
import uz.lingvohub.bot.state.AdminState;

@Component
@RequiredArgsConstructor
public class MessageRouter {

    private final TelegramProperties telegramProperties;
    private final UserService userService;
    private final CatalogService catalogService;
    private final AdminService adminService;
    private final SessionService sessionService;
    private final KeyboardFactory keyboardFactory;

    public void route(Update update, BotGateway botGateway) {
        Message message = update.getMessage();
        Long telegramId = message.getFrom().getId();
        Long chatId = message.getChatId();
        String text = message.getText().trim();

        userService.getOrCreate(telegramId, message.getFrom().getUserName());

        if ("/start".equals(text) || text.startsWith("/start@")) {
            var languages = catalogService.languages();
            if (languages.isEmpty()) {
                botGateway.sendText(chatId,
                        "LingvoHub botiga xush kelibsiz!\n\nHozircha tillar yo'q. Admin /admin orqali til qo'shsin.",
                        null);
            } else {
                botGateway.sendText(chatId, "LingvoHub botiga xush kelibsiz! Tilni tanlang:",
                        keyboardFactory.languageKeyboard(languages));
            }
            return;
        }

        if ("/admin".equals(text)) {
            if (!telegramProperties.getAdminIds().contains(telegramId)) {
                botGateway.sendText(chatId, "Sizda admin huquqi yo'q.", null);
                return;
            }
            botGateway.sendText(chatId, "Admin menyu:", keyboardFactory.adminMenuKeyboard());
            return;
        }

        if ("/stats".equals(text) && telegramProperties.getAdminIds().contains(telegramId)) {
            String stats = "📊 Statistikalar:\nFoydalanuvchilar: " + userService.count() + "\nDarslar: " + adminService.totalLessons();
            botGateway.sendText(chatId, stats, null);
            return;
        }

        AdminSession adminSession = sessionService.adminSession(telegramId);
        if (adminSession.getState() != AdminState.NONE && telegramProperties.getAdminIds().contains(telegramId)) {
            handleAdminState(chatId, text, adminSession, botGateway);
        }
    }

    private void handleAdminState(Long chatId, String text, AdminSession session, BotGateway botGateway) {
        switch (session.getState()) {
            case ADD_LANGUAGE_NAME -> {
                adminService.addLanguage(text);
                session.setState(AdminState.NONE);
                botGateway.sendText(chatId, "Til saqlandi.", keyboardFactory.adminMenuKeyboard());
            }
            case ADD_TEACHER_NAME -> {
                session.setPendingTeacherName(text);
                session.setState(AdminState.ADD_TEACHER_BIO);
                botGateway.sendText(chatId, "Teacher bio kiriting:", null);
            }
            case ADD_TEACHER_BIO -> {
                session.setPendingTeacherBio(text);
                session.setState(AdminState.ADD_TEACHER_RATING);
                botGateway.sendText(chatId, "Teacher reytingini kiriting (masalan 4.8):", null);
            }
            case ADD_TEACHER_RATING -> {
                adminService.addTeacher(session.getPendingTeacherName(), session.getPendingTeacherBio(), Double.parseDouble(text));
                session.setState(AdminState.NONE);
                botGateway.sendText(chatId, "Teacher saqlandi.", keyboardFactory.adminMenuKeyboard());
            }
            case ADD_COURSE_TITLE -> {
                session.setPendingCourseTitle(text);
                session.setState(AdminState.ADD_COURSE_DESCRIPTION);
                botGateway.sendText(chatId, "Kurs tavsifini kiriting:", null);
            }
            case ADD_COURSE_DESCRIPTION -> {
                adminService.addCourse(session.getSelectedLanguageId(), session.getSelectedTeacherId(), session.getPendingCourseTitle(), text);
                session.setState(AdminState.NONE);
                botGateway.sendText(chatId, "Kurs saqlandi.", keyboardFactory.adminMenuKeyboard());
            }
            case ADD_LESSON_TITLE -> {
                session.setPendingLessonTitle(text);
                session.setState(AdminState.ADD_LESSON_ORDER);
                botGateway.sendText(chatId, "Dars tartib raqamini kiriting:", null);
            }
            case ADD_LESSON_ORDER -> {
                session.setPendingLessonOrder(Integer.parseInt(text));
                session.setState(AdminState.ADD_LESSON_CHANNEL_ID);
                botGateway.sendText(chatId, "channel_id kiriting:", null);
            }
            case ADD_LESSON_CHANNEL_ID -> {
                session.setPendingLessonChannelId(text);
                session.setState(AdminState.ADD_LESSON_MESSAGE_ID);
                botGateway.sendText(chatId, "message_id kiriting:", null);
            }
            case ADD_LESSON_MESSAGE_ID -> {
                session.setPendingLessonMessageId(Integer.parseInt(text));
                botGateway.sendText(chatId, "Bu dars premiummi?", keyboardFactory.premiumToggleKeyboard());
            }
            default -> botGateway.sendText(chatId, "Noto'g'ri holat.", null);
        }
    }

}
