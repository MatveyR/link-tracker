package backend.academy.scrapper;

import backend.academy.scrapper.Services.LiquibaseRunnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class LiquibaseRunnerTest {

    @Autowired
    private LiquibaseRunnerService liquibaseRunner;

    @Test
    void testMigrations() throws Exception {
        assertDoesNotThrow(() -> liquibaseRunner.runMigrations());
    }
}
