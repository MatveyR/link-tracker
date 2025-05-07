package backend.academy.scrapper.Data.Repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseMapRepository<T> implements Repository<T, Long> {
    protected final Map<Long, T> storage = new HashMap<>();

    @Override
    public T save(T entity) {
        Long id = getId(entity);

        storage.put(id, entity);
        return entity;
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    @Override
    public long count() {
        return storage.size();
    }

    protected abstract Long getId(T entity);
}
