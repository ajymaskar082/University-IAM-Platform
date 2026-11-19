package kz.university.iam.repository;

import java.util.List;
import java.util.Optional;

// >>> [ТАЛАП: КОЛЛЕКЦИЯЛАР ЖӘНЕ GENERICS] БАСТАЛАДЫ — Generic интерфейс Repository<T, ID>
/**
 * Кез келген типке (T) жарамды жалпы репозиторий интерфейсі.
 *
 * @param <T>  сақталатын нысан типі
 * @param <ID> нысанның идентификатор типі
 */
public interface Repository<T, ID> {

    void save(T item);

    Optional<T> findById(ID id);

    List<T> findAll();

    boolean deleteById(ID id);

    int count();
}
