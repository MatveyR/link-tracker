package backend.academy.scrapper;

import backend.academy.scrapper.Data.Repositories.JdbcRepositories.JdbcChatRepository;
import backend.academy.scrapper.Data.Repositories.JdbcRepositories.JdbcLinkRepository;
import backend.academy.scrapper.Data.Repositories.JdbcRepositories.JdbcSubscriptionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "data.access-type=SQL")
public class JdbcRepositoryProfileTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testJdbcChatRepositoryIsUsed() {
        Object chatRepo = applicationContext.getBean("jdbcChatRepository");
        Assertions.assertNotNull(chatRepo);
        Assertions.assertInstanceOf(JdbcChatRepository.class, chatRepo);
    }

    @Test
    void testJdbcLinkRepositoryIsUsed() {
        Object linkRepo = applicationContext.getBean("jdbcLinkRepository");
        Assertions.assertNotNull(linkRepo);
        Assertions.assertInstanceOf(JdbcLinkRepository.class, linkRepo);
    }

    @Test
    void testJdbcSubscriptionRepositryIsUsed() {
        Object subscriptionRepo = applicationContext.getBean("jdbcSubscriptionRepository");
        Assertions.assertNotNull(subscriptionRepo);
        Assertions.assertInstanceOf(JdbcSubscriptionRepository.class, subscriptionRepo);
    }
}
