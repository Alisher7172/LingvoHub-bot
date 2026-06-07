package uz.lingvohub.bot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
public class TelegramBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean
    public ApplicationRunner registerTelegramBot(TelegramBotsApi telegramBotsApi, LingvoHubTelegramBot bot) {
        return args -> {
            try {
                BotSession session = telegramBotsApi.registerBot(bot);
                log.info("Telegram long polling ishga tushdi. Bot: @{}, session running: {}",
                        bot.getBotUsername(), session.isRunning());
            } catch (TelegramApiException e) {
                log.error("Botni Telegramga ulab bo'lmadi. Token va internetni tekshiring.", e);
                throw new IllegalStateException("Telegram bot registration failed", e);
            }
        };
    }
}
