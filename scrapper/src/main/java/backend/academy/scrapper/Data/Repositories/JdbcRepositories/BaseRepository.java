package backend.academy.scrapper.Data.Repositories.JdbcRepositories;

import java.util.Collection;
import java.util.Optional;

public interface BaseRepository<T, ID> {
    T save(T entity);

    Optional<T> findById(ID id);

    Collection<T> findAll();

    void deleteById(ID id);

    boolean existsById(ID id);

    long count();
}
