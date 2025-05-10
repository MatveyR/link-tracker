package backend.academy.scrapper.Configs;

import backend.academy.scrapper.Data.Repositories.ChatRepository;
import backend.academy.scrapper.Data.Repositories.JdbcRepositories.JdbcChatRepository;
import backend.academy.scrapper.Data.Repositories.JdbcRepositories.JdbcLinkRepository;
import backend.academy.scrapper.Data.Repositories.JdbcRepositories.JdbcSubscriptionRepository;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class RepositoryConfig {
    @Bean
    public ChatRepository jdbcChatRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    public LinkRepository jdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcLinkRepository(jdbcTemplate);
    }

    @Bean
    public SubscriptionRepository jdbcSubscriptionRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcSubscriptionRepository(jdbcTemplate);
    }
}
