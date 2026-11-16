package uz.lingvohub.bot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.junit.jupiter.api.Assertions.assertNull;

class LingvoHubApplicationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(LingvoHubApplication.class)
            .withPropertyValues(
                    "spring.datasource.url=jdbc:sqlite::memory:",
                    "telegram.bot-token=test-token",
                    "telegram.bot-username=test_bot",
                    "telegram.admin-ids[0]=123456789"
            );

    @Test
    void applicationContextStartsWithLocalSqliteAndTelegramConfiguration() {
        contextRunner.run(context -> assertNull(context.getStartupFailure()));
    }
}
