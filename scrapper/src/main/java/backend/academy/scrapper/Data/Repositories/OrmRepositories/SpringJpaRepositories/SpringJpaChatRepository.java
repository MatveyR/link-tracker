package backend.academy.scrapper.Data.Repositories.OrmRepositories.SpringJpaRepositories;

import backend.academy.scrapper.Data.Models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaChatRepository extends JpaRepository<Chat, Long> {
}
