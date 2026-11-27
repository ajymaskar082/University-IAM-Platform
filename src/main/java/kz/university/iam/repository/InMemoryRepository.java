package kz.university.iam.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Generic класс: деректерді жедел жадта Map ішінде сақтайды.
 * Map — ID бойынша жылдам іздеу, List — барлық нысандарды қайтару үшін.
 */
public class InMemoryRepository<T, ID> implements Repository<T, ID> {

    private final Map<ID, T> storage = new LinkedHashMap<>();
    private final Function<T, ID> idExtractor;

    public InMemoryRepository(Function<T, ID> idExtractor) {
        this.idExtractor = idExtractor;
    }

    @Override
    public void save(T item) {
        storage.put(idExtractor.apply(item), item);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(ID id) {
        return storage.remove(id) != null;
    }

    @Override
    public int count() {
        return storage.size();
    }
}
// <<< [ТАЛАП: КОЛЛЕКЦИЯЛАР ЖӘНЕ GENERICS] АЯҚТАЛДЫ — Repository<T, ID>
