package backend.academy.scrapper.Data.Repositories.OrmRepositories.SpringJpaRepositories;

import backend.academy.scrapper.Data.Models.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaLinkRepository extends JpaRepository<Link, Long> {
    Link findByLinkUrl(String url);
}
