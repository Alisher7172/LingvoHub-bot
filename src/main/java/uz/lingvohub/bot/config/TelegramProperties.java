package uz.lingvohub.bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {

    private String botToken;
    private String botUsername;
    private List<Long> adminIds = new ArrayList<>();
}
