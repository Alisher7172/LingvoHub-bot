package uz.lingvohub.bot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.lingvohub.bot.handler.CallbackRouter;
import uz.lingvohub.bot.handler.MessageRouter;

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
