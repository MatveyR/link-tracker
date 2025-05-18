package backend.academy.scrapper.Configs;

import backend.academy.scrapper.Data.Repositories.ChatRepository;
import backend.academy.scrapper.Data.Repositories.JdbcRepositories.JdbcChatRepository;
import backend.academy.scrapper.Data.Repositories.JdbcRepositories.JdbcLinkRepository;
import backend.academy.scrapper.Data.Repositories.JdbcRepositories.JdbcSubscriptionRepository;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Data.Repositories.OrmRepositories.OrmChatRepository;
import backend.academy.scrapper.Data.Repositories.OrmRepositories.OrmLinkRepository;
import backend.academy.scrapper.Data.Repositories.OrmRepositories.OrmSubscriptionRepository;
import backend.academy.scrapper.Data.Repositories.OrmRepositories.SpringJpaRepositories.SpringJpaChatRepository;
import backend.academy.scrapper.Data.Repositories.OrmRepositories.SpringJpaRepositories.SpringJpaLinkRepository;
import backend.academy.scrapper.Data.Repositories.OrmRepositories.SpringJpaRepositories.SpringJpaSubscriptionRepository;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class RepositoryConfig {
    private static final String ACCESS_TYPE_PROP = "data.access-type";

    @Bean
    @ConditionalOnProperty(name = ACCESS_TYPE_PROP, havingValue = "ORM")
    public ChatRepository ormChatRepository(SpringJpaChatRepository repository) {
        return new OrmChatRepository(repository);
    }

    @Bean
    @ConditionalOnProperty(name = ACCESS_TYPE_PROP, havingValue = "SQL")
    public ChatRepository jdbcChatRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    @ConditionalOnProperty(name = ACCESS_TYPE_PROP, havingValue = "ORM")
    public LinkRepository ormLinkRepository(SpringJpaLinkRepository repository) {
        return new OrmLinkRepository(repository);
    }

    @Bean
    @ConditionalOnProperty(name = ACCESS_TYPE_PROP, havingValue = "SQL")
    public LinkRepository jdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcLinkRepository(jdbcTemplate);
    }

    @Bean
    @ConditionalOnProperty(name = ACCESS_TYPE_PROP, havingValue = "ORM")
    public SubscriptionRepository ormSubscriptionRepository(SpringJpaSubscriptionRepository repository) {
        return new OrmSubscriptionRepository(repository);
    }

    @Bean
    @ConditionalOnProperty(name = ACCESS_TYPE_PROP, havingValue = "SQL")
    public SubscriptionRepository jdbcSubscriptionRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcSubscriptionRepository(jdbcTemplate);
    }
}
