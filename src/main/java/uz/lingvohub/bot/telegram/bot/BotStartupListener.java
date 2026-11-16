package uz.lingvohub.bot.telegram.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    private final LingvoHubTelegramBot bot;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("LingvoHub tayyor. Bot: @{}", bot.getBotUsername());
    }
}
