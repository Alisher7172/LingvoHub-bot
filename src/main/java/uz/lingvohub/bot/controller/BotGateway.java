package uz.lingvohub.bot.controller;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface BotGateway {

    void sendText(Long chatId, String text, InlineKeyboardMarkup keyboard);

    void editText(Long chatId, Integer messageId, String text, InlineKeyboardMarkup keyboard);

    boolean copyLesson(Long chatId, String channelId, Integer messageId);

    void answerCallback(String callbackQueryId, String text);
}
