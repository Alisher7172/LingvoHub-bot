package uz.lingvohub.bot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.lingvohub.bot.controller.BotGateway;
import uz.lingvohub.bot.controller.UpdateController;

@Slf4j
@Component
public class LingvoHubTelegramBot extends TelegramLongPollingBot implements BotGateway {

    private final TelegramProperties properties;
    private final UpdateController updateController;

    public LingvoHubTelegramBot(TelegramProperties properties, UpdateController updateController) {
        super(properties.getBotToken());
        this.properties = properties;
        this.updateController = updateController;
    }

    @Override
    public String getBotUsername() {
        String username = properties.getBotUsername();
        if (username != null && username.startsWith("@")) {
            return username.substring(1);
        }
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Update id={}", update.getUpdateId());
        try {
            updateController.handle(update, this);
        } catch (Exception e) {
            log.error("Update qayta ishlashda xatolik", e);
        }
    }

    @Override
    public void sendText(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .replyMarkup(keyboard)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Xabar yuborishda xatolik", e);
        }
    }

    @Override
    public void editText(Long chatId, Integer messageId, String text, InlineKeyboardMarkup keyboard) {
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .text(text)
                .replyMarkup(keyboard)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Xabarni tahrirlashda xatolik", e);
        }
    }

    @Override
    public void copyLesson(Long chatId, String channelId, Integer messageId) {
        CopyMessage copyMessage = CopyMessage.builder()
                .chatId(chatId.toString())
                .fromChatId(channelId)
                .messageId(messageId)
                .build();
        try {
            execute(copyMessage);
        } catch (TelegramApiException e) {
            log.error("Darsni copyMessage bilan yuborishda xatolik", e);
        }
    }

    @Override
    public void answerCallback(String callbackQueryId, String text) {
        AnswerCallbackQuery answer = AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId)
                .text(text)
                .showAlert(false)
                .build();
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.error("Callback javobida xatolik", e);
        }
    }
}
