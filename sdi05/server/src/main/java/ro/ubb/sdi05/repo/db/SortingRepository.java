package ro.ubb.sdi05.repo.db;

import ro.ubb.sdi05.domain.BaseEntity;
import ro.ubb.sdi05.domain.Sort;
import ro.ubb.sdi05.repo.Repository;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by radu.
 */
public interface SortingRepository<ID extends Serializable, T extends BaseEntity<ID>> extends Repository<ID, T> {

    Iterable<T> findAll(Sort sort);

    default Iterable<T> findAllSorted(final Sort sort,
                                      final Class<? extends BaseEntity> aClass) {
        final Optional<Comparator<T>> optionalChainedComp = sort.getFieldsOrder().stream()
//                converting fieldOrders to comparators
                .map(fieldOrder -> {
                    final MethodHandle getter = getGetter(fieldOrder.getFieldName(), aClass);
                    return Objects.equals(fieldOrder.getDirection(), Sort.Direction.ASC) ?
                            (Comparator<T>) Comparator.comparing((T q) -> invoke(getter, q)) :
                            (Comparator<T>) Comparator.comparing((T q) -> invoke(getter, q)).reversed();
                })
//                chaining comparators
                .reduce(Comparator::thenComparing);

        return StreamSupport.stream(
                findAll().spliterator(),
                false)
                .sorted(optionalChainedComp
                        .orElseThrow(() -> new RuntimeException("No sort options provided!")))
                .collect(Collectors.toList());

    }

    private MethodHandle getGetter(final String fieldName,
                                   final Class<? extends BaseEntity> aClass) {
        try {
            return MethodHandles
                    .privateLookupIn(
                            aClass,
                            MethodHandles.lookup())
                    .findGetter(aClass, fieldName, aClass.getDeclaredField(fieldName).getType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Comparable invoke(final MethodHandle getter,
                              final T t) {
        try {
            return (Comparable) getter.invoke(t);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}