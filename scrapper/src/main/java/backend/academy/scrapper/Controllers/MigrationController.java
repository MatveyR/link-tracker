package backend.academy.scrapper.Controllers;

import backend.academy.scrapper.Services.LiquibaseRunnerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/migrations")
public class MigrationController {

    private static final String ACCESS_TYPE_PROP = "data.access-type";

    private final LiquibaseRunnerService liquibaseRunner;

    public MigrationController(LiquibaseRunnerService liquibaseRunner) {
        this.liquibaseRunner = liquibaseRunner;
    }

    @PostMapping("/run")
    public String runMigrations() {
        try {
            liquibaseRunner.runMigrations();
            return "Миграции успешно выполнены";
        } catch (Exception e) {
            return "Ошибка при выполнении миграций: " + e.getMessage();
        }
    }
}
