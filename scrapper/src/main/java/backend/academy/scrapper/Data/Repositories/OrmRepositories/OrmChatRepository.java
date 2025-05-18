package backend.academy.scrapper.Data.Repositories.OrmRepositories;

import backend.academy.scrapper.Data.Models.Chat;
import backend.academy.scrapper.Data.Repositories.ChatRepository;
import java.util.Collection;
import java.util.Optional;
import backend.academy.scrapper.Data.Repositories.OrmRepositories.SpringJpaRepositories.SpringJpaChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("orm")
@AllArgsConstructor
public class OrmChatRepository implements ChatRepository {
    private final SpringJpaChatRepository repository;

    @Override
    public Chat save(Chat entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<Chat> findById(Long aLong) {
        return repository.findById(aLong);
    }

    @Override
    public Collection<Chat> findAll() {
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
