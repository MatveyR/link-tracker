package backend.academy.scrapper.Configs;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ScrapperAppConfig(@NotEmpty String githubToken, StackOverflowCredentials stackOverflow) {
    public record StackOverflowCredentials(@NotEmpty String key, @NotEmpty String accessToken) {}
}
