package ro.ubb.sdi05.repo.db;

import ro.ubb.sdi05.domain.Client;
import ro.ubb.sdi05.domain.Sort;
import ro.ubb.sdi05.domain.validators.*;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ro.ubb.sdi05.repo.db.RepoDbHelper.*;

public class RepoClientDb implements SortingRepository<BigInteger, Client> {
    private final Validator<Client> validator = new ClientValidator();
    private final IdValidator<BigInteger> idValidator = new IdValidatorImpl();

    public RepoClientDb() {
        execute("CREATE TABLE IF NOT EXISTS client" +
                "(id bigint primary key not null," +
                "name varchar(256) not null)");
    }

    @Override
    public Iterable<Client> findAll(Sort sort) {
        return findAllSorted(sort, Client.class);
    }

    /**
     * Find the entity with the given {@code id}.
     *
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Client> findOne(final BigInteger id) {
        idValidator.validate(id);
        final ResultSet resultSet = executeQuery("select * from client where id = " + id);
        return next(resultSet) ? Optional.of(getClient(resultSet)) : Optional.empty();
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Client> findAll() {
        final List<Client> clients = new ArrayList<>();
        final ResultSet resultSet = executeQuery("select * from client");
        while (next(resultSet)) {
            clients.add(getClient(resultSet));
        }
        return clients;
    }

    /**
     * Saves the given entity.
     *
     * @param client must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Client> save(final Client client) throws ValidatorException {
        validator.validate(client);
        final Optional<Client> optionalClient = findOne(client.getId());
        optionalClient
                .ifPresentOrElse(
                        bk -> {
                        },
                        () -> executeUpdate(
                                "insert into client (id, name) values (?, ?)",
                                prepStmt -> {
                                    setLong(prepStmt, 1, client.getId().longValue());
                                    setString(prepStmt, 2, client.getName());
                                }));
        return optionalClient;
    }

    /**
     * Removes the entity with the given id.
     *
     * @param id must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Client> delete(final BigInteger id) {
        idValidator.validate(id);
        final Optional<Client> optionalClient = findOne(id);
        optionalClient
                .ifPresent(
                        bk ->
                                executeUpdate(
                                        "delete from client where id = ?",
                                        prepStmt -> setLong(prepStmt, 1, id.longValue())));
        return optionalClient;
    }

    /**
     * Updates the given entity.
     *
     * @param client must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     * entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Client> update(final Client client) throws ValidatorException {
        validator.validate(client);
        final Optional<Client> optionalReturn = findOne(client.getId()).isEmpty() ? Optional.of(client) : Optional.empty();
        optionalReturn.ifPresentOrElse(
                bk -> {
                },
                () -> executeUpdate(
                        "update client set name = ? where id = ?",
                        prepStmt -> {
                            setString(prepStmt, 1, client.getName());
                            setLong(prepStmt, 2, client.getId().longValue());
                        }));
        return optionalReturn;
    }
}
