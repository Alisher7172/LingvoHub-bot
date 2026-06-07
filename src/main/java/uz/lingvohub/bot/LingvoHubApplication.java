package uz.lingvohub.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import uz.lingvohub.bot.config.TelegramProperties;

@SpringBootApplication
@EnableConfigurationProperties(TelegramProperties.class)
public class LingvoHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(LingvoHubApplication.class, args);
    }
}
