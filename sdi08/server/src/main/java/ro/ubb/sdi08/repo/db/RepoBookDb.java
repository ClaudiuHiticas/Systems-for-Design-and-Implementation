package ro.ubb.sdi08.repo.db;

import ro.ubb.sdi08.domain.Book;
import ro.ubb.sdi08.domain.Sort;
import ro.ubb.sdi08.domain.validators.*;

import java.math.BigInteger;
import java.util.Optional;

import static ro.ubb.sdi08.repo.db.RepoDbHelper.*;

public class RepoBookDb implements SortingRepository<BigInteger, Book> {

    private final Validator<Book> validator = new BookValidator();
    private final IdValidator<BigInteger> idValidator = new IdValidatorImpl();

    public RepoBookDb() {
        execute("CREATE TABLE IF NOT EXISTS book" +
                "(id bigint primary key not null," +
                "title varchar(256) not null," +
                "author varchar(256) not null," +
                "price decimal not null)");
    }

    @Override
    public Iterable<Book> findAll(Sort sort) {
        return findAllSorted(sort, Book.class);
    }

    /**
     * Find the entity with the given {@code id}.
     *
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Book> findOne(final BigInteger id) {
        idValidator.validate(id);
        return query("select * from book where id = " + id, resultSet -> {
            if (!next(resultSet)) {
                return Optional.empty();
            } else {
                return Optional.of(new Book(
                        BigInteger.valueOf(resultSet.getLong("id")),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getBigDecimal("price")));
            }
        });
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Book> findAll() {
        return RepoDbHelper.query("select * from book", (resultSet, rowNum) -> new Book(
                BigInteger.valueOf(resultSet.getLong("id")),
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getBigDecimal("price")));
    }

    /**
     * Saves the given entity.
     *
     * @param book must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Book> save(final Book book) throws ValidatorException {
        validator.validate(book);
        final Optional<Book> optionalBook = findOne(book.getId());
        optionalBook
                .ifPresentOrElse(
                        bk -> {
                        },
                        () -> executeUpdate(
                                "insert into book (id, title, author, price) values (?, ?, ?, ?)",
                                book.getId().longValue(),
                                book.getTitle(),
                                book.getAuthor(),
                                book.getPrice()));
        return optionalBook;
    }

    /**
     * Removes the entity with the given id.
     *
     * @param id must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Book> delete(final BigInteger id) {
        idValidator.validate(id);
        final Optional<Book> optionalBook = findOne(id);
        optionalBook
                .ifPresent(
                        bk ->
                                executeUpdate(
                                        "delete from book where id = ?",
                                        id.longValue()));
        return optionalBook;
    }

    /**
     * Updates the given entity.
     *
     * @param book must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     * entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Book> update(final Book book) throws ValidatorException {
        validator.validate(book);
        final Optional<Book> optionalReturn = findOne(book.getId()).isEmpty() ? Optional.of(book) : Optional.empty();
        optionalReturn.ifPresentOrElse(
                bk -> {
                },
                () -> executeUpdate(
                        "update book set title = ?, author = ?, price = ? where id = ?",
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPrice(),
                        book.getId().longValue()));

        return optionalReturn;
    }
}
