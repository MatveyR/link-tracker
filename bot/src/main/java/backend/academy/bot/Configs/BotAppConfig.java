package backend.academy.bot.Configs;

import com.pengrad.telegrambot.TelegramBot;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record BotAppConfig(@NotEmpty String telegramToken) {
    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(telegramToken);
    }
}
