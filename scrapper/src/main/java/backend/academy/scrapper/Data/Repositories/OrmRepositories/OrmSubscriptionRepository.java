package backend.academy.scrapper.Data.Repositories.OrmRepositories;

import backend.academy.scrapper.Data.Models.Subscription;
import backend.academy.scrapper.Data.Repositories.OrmRepositories.SpringJpaRepositories.SpringJpaSubscriptionRepository;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("orm")
@AllArgsConstructor
public class OrmSubscriptionRepository implements SubscriptionRepository {
    private final SpringJpaSubscriptionRepository repository;

    @Override
    public List<Long> findLinksByUserId(Long userId) {
        return repository.findLinksByUserId(userId);
    }

    @Override
    public List<Long> findUsersByLinkId(Long linkId) {
        return repository.findUsersByLinkId(linkId);
    }

    @Override
    public Subscription save(Subscription entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<Subscription> findById(Long aLong) {
        return repository.findById(aLong);
    }

    @Override
    public Collection<Subscription> findAll() {
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
