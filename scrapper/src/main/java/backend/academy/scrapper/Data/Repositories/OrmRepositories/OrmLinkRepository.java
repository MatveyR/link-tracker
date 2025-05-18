package backend.academy.scrapper.Data.Repositories.OrmRepositories;

import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Data.Repositories.OrmRepositories.SpringJpaRepositories.SpringJpaLinkRepository;
import java.util.Collection;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("orm")
@AllArgsConstructor
public class OrmLinkRepository implements LinkRepository {
    private final SpringJpaLinkRepository repository;

    @Override
    public Link findByUrl(String url) {
        return repository.findByLinkUrl(url);
    }

    @Override
    public Link save(Link entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<Link> findById(Long aLong) {
        return repository.findById(aLong);
    }

    @Override
    public Collection<Link> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long aLong) {
        repository.deleteById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return repository.existsById(aLong);
    }

    @Override
    public long count() {
        return repository.count();
    }
}
