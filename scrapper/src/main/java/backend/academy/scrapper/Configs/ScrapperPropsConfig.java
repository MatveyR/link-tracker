package backend.academy.scrapper.Configs;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ScrapperPropsConfig(GithubCredentials github,
                                  StackOverflowCredentials stackOverflow) {
    public record StackOverflowCredentials(@NotEmpty int timeout, @NotEmpty String api_url) {
    }

    public record GithubCredentials(@NotEmpty int timeout, @NotEmpty String api_url, @NotEmpty int update_threshold) {
    }
}
