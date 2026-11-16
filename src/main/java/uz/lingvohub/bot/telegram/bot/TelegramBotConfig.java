package uz.lingvohub.bot.telegram.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PreDestroy;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
public class TelegramBotConfig {

    private volatile BotSession botSession;

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean
    public ApplicationRunner registerTelegramBot(TelegramBotsApi telegramBotsApi, LingvoHubTelegramBot bot) {
        return args -> {
            try {
                BotSession session = telegramBotsApi.registerBot(bot);
                botSession = session;
                log.info("Telegram long polling ishga tushdi. Bot: @{}, session running: {}",
                        bot.getBotUsername(), session.isRunning());
            } catch (TelegramApiException e) {
                log.error("Botni Telegramda topib bo'lmadi. Token va internetni tekshiring.", e);
                throw new IllegalStateException("Telegram bot registration failed", e);
            }
        };
    }

    @PreDestroy
    public void stopTelegramBot() {
        BotSession session = botSession;
        if (session != null && session.isRunning()) {
            session.stop();
            log.info("Telegram long polling stopped.");
        }
    }
}
