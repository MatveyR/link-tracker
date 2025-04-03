package backend.academy.scrapper.Data.Repositories;

import backend.academy.scrapper.Data.Models.Link;
import org.springframework.stereotype.Repository;

@Repository
public class LinkRepository extends BaseMapRepository<Link> {
    @Override
    protected Long getId(Link entity) {
        return entity.id();
    }
}
