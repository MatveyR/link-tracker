package backend.academy.scrapper.Services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import org.springframework.stereotype.Service;

@Service
public class LiquibaseRunnerService {

    private final DataSource dataSource;

    public LiquibaseRunnerService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void runMigrations() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                "db.changelog-master.yaml",
                new DirectoryResourceAccessor(Paths.get("").toAbsolutePath()
                    .getParent()
                    .resolve("migrations")),
                database
            );

            liquibase.update("");
            System.out.println("Миграции успешно выполнены");
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении миграций: " + e.getMessage());
            throw e;
        }
    }
}
