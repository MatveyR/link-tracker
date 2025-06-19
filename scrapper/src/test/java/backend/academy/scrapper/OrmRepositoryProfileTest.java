package backend.academy.scrapper;

import backend.academy.scrapper.Data.Repositories.OrmRepositories.OrmChatRepository;
import backend.academy.scrapper.Data.Repositories.OrmRepositories.OrmLinkRepository;
import backend.academy.scrapper.Data.Repositories.OrmRepositories.OrmSubscriptionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "data.access-type=ORM")
public class OrmRepositoryProfileTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testOrmChatRepositoryIsUsed() {
        Object chatRepo = applicationContext.getBean("ormChatRepository");
        Assertions.assertNotNull(chatRepo);
        Assertions.assertInstanceOf(OrmChatRepository.class, chatRepo);
    }

    @Test
    void testOrmLinkRepositoryIsUsed() {
        Object linkRepo = applicationContext.getBean("ormLinkRepository");
        Assertions.assertNotNull(linkRepo);
        Assertions.assertInstanceOf(OrmLinkRepository.class, linkRepo);
    }

    @Test
    void testOrmSubscriptionRepositryIsUsed() {
        Object subscriptionRepo = applicationContext.getBean("ormSubscriptionRepository");
        Assertions.assertNotNull(subscriptionRepo);
        Assertions.assertInstanceOf(OrmSubscriptionRepository.class, subscriptionRepo);
    }
}
