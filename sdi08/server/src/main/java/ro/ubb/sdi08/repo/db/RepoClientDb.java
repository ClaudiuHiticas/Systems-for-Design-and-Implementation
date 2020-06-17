package ro.ubb.sdi08.repo.db;

import ro.ubb.sdi08.domain.Client;
import ro.ubb.sdi08.domain.Sort;
import ro.ubb.sdi08.domain.validators.*;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ro.ubb.sdi08.repo.db.RepoDbHelper.*;

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
        return query("select * from client where id = " + id, resultSet ->{
            if(!next(resultSet)){
                return Optional.empty();
            }else{
                return Optional.of(new Client(
                   BigInteger.valueOf(resultSet.getLong("id")),
                   resultSet.getString("name")));
            }
        });
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Client> findAll() {
        return RepoDbHelper.query("select * from client", (resultSet, rowNum) -> new Client(
                BigInteger.valueOf(resultSet.getLong("id")),
                resultSet.getString("name")
        ));
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
                                client.getId().longValue(),
                                client.getName()
                                ));
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
                                        id.longValue()));
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
                        client.getName(),
                        client.getId().longValue()));
        return optionalReturn;
    }
}
