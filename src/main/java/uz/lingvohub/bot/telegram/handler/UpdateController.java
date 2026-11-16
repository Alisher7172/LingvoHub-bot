package uz.lingvohub.bot.telegram.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.lingvohub.bot.telegram.bot.BotGateway;

@Component
@RequiredArgsConstructor
public class UpdateController {

    private final MessageRouter messageRouter;
    private final CallbackRouter callbackRouter;

    public void handle(Update update, BotGateway botGateway) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            messageRouter.route(update, botGateway);
            return;
        }

        if (update.hasCallbackQuery()) {
            callbackRouter.route(update, botGateway);
        }
    }
}
