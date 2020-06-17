package ro.ubb.sdi05.repo.db;

import ro.ubb.sdi05.domain.Order;
import ro.ubb.sdi05.domain.Sort;
import ro.ubb.sdi05.domain.validators.*;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ro.ubb.sdi05.repo.db.RepoDbHelper.*;

public class RepoOrderDb implements SortingRepository<BigInteger, Order> {
    private final Validator<Order> validator = new OrderValidator();
    private final IdValidator<BigInteger> idValidator = new IdValidatorImpl();

    public RepoOrderDb() {
        execute("CREATE TABLE IF NOT EXISTS orderBook" +
                "(id bigint primary key not null," +
                "clientId bigint references client(id) not null," +
                "bookId bigint references book(id) not null," +
                "bookTitle varchar(256) not null," +
                "bookAuthor varchar(256) not null," +
                "bookPrice decimal not null)");
    }

    @Override
    public Iterable<Order> findAll(Sort sort) {
        return findAllSorted(sort, Order.class);
    }

    /**
     * Find the entity with the given {@code id}.
     *
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Order> findOne(final BigInteger id) {
        idValidator.validate(id);
        final ResultSet resultSet = executeQuery("select * from orderBook where id = " + id);
        return next(resultSet) ? Optional.of(getOrder(resultSet)) : Optional.empty();
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Order> findAll() {
        final List<Order> orders = new ArrayList<>();
        final ResultSet resultSet = executeQuery("select * from orderBook");
        while (next(resultSet)) {
            orders.add(getOrder(resultSet));
        }
        return orders;
    }

    /**
     * Saves the given entity.
     *
     * @param order must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Order> save(final Order order) throws ValidatorException {
        validator.validate(order);
        final Optional<Order> optionalOrder = findOne(order.getId());
        optionalOrder
                .ifPresentOrElse(
                        ord -> {
                        },
                        () -> executeUpdate(
                                "insert into orderBook (id, clientId, bookId, bookTitle, bookAuthor, bookPrice) values (?, ?, ?, ?, ?, ?)",
                                prepStmt -> {
                                    setLong(prepStmt, 1, order.getId().longValue());
                                    setLong(prepStmt, 2, order.getClient().getId().longValue());
                                    setLong(prepStmt, 3, order.getBook().getId().longValue());
                                    setString(prepStmt, 4, order.getBook().getTitle());
                                    setString(prepStmt, 5, order.getBook().getAuthor());
                                    setBigDecimal(prepStmt, 6, order.getBook().getPrice());
                                }));
        return optionalOrder;
    }

    /**
     * Removes the entity with the given id.
     *
     * @param id must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Order> delete(final BigInteger id) {
        idValidator.validate(id);
        final Optional<Order> optionalOrder = findOne(id);
        optionalOrder
                .ifPresent(
                        bk ->
                                executeUpdate(
                                        "delete from orderBook where id = ?",
                                        prepStmt -> setLong(prepStmt, 1, id.longValue())));
        return optionalOrder;
    }

    /**
     * Updates the given entity.
     *
     * @param order must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     * entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Order> update(final Order order) throws ValidatorException {
        validator.validate(order);
        final Optional<Order> optionalReturn = findOne(order.getId()).isEmpty() ? Optional.of(order) : Optional.empty();
        optionalReturn.ifPresentOrElse(
                bk -> {
                },
                () -> executeUpdate(
                        "update orderBook set clientId = ?, bookId = ?, bookTitle = ?, bookAuthor = ?, bookPrice = ?, where id = ?",
                        prepStmt -> {
                            setLong(prepStmt, 1, order.getClient().getId().longValue());
                            setLong(prepStmt, 2, order.getBook().getId().longValue());
                            setString(prepStmt, 3, order.getBook().getTitle());
                            setString(prepStmt, 4, order.getBook().getAuthor());
                            setBigDecimal(prepStmt, 5, order.getBook().getPrice());
                            setLong(prepStmt, 6, order.getId().longValue());
                        }));
        return optionalReturn;
    }
}
