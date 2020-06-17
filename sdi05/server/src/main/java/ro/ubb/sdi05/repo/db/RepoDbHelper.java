package ro.ubb.sdi05.repo.db;

import ro.ubb.sdi05.domain.Book;
import ro.ubb.sdi05.domain.Client;
import ro.ubb.sdi05.domain.Order;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.Optional;
import java.util.function.Consumer;

public final class RepoDbHelper {
    public static final String URL = "jdbc:postgresql://localhost:5432/sdi";
    public static final String USER = System.getProperty("pgUsername");
    public static final String PASSWORD = System.getProperty("pgPassword");

    public static void deleteAllTables() {
        execute("DROP SCHEMA public CASCADE");
        execute("CREATE SCHEMA public");
    }

    static boolean execute(final String sqlQuery) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            return connection
                    .prepareStatement(sqlQuery)
                    .execute();
        } catch (SQLException e) {
            throw new SqlExceptionUnchecked(e);
        }
    }

    static ResultSet executeQuery(final String sqlQuery) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            return connection
                    .prepareStatement(sqlQuery)
                    .executeQuery();
        } catch (SQLException e) {
            throw new SqlExceptionUnchecked(e);
        }
    }

    static int executeUpdate(final String sqlQuery,
                             final Consumer<PreparedStatement> setSqlQuery) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            setSqlQuery.accept(preparedStatement);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SqlExceptionUnchecked(e);
        }
    }

    static boolean next(final ResultSet resultSet) {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new SqlExceptionUnchecked(e);
        }
    }

    static void setString(final PreparedStatement prepStmt,
                          final int index,
                          final String str) {
        try {
            prepStmt.setString(index, str);
        } catch (SQLException e) {
            throw new SqlExceptionUnchecked(e);
        }
    }

    static void setLong(final PreparedStatement prepStmt,
                        final int index,
                        final Long lng) {
        try {
            prepStmt.setLong(index, lng);
        } catch (SQLException e) {
            throw new SqlExceptionUnchecked(e);
        }
    }

    static void setBigDecimal(final PreparedStatement prepStmt,
                              final int index,
                              final BigDecimal bigDecimal) {
        try {
            prepStmt.setBigDecimal(index, bigDecimal);
        } catch (SQLException e) {
            throw new SqlExceptionUnchecked(e);
        }
    }

    static Client getClient(final ResultSet resultSet) {
        try {
            final Client client = new Client(resultSet.getString("name"));
            client.setId(BigInteger.valueOf(resultSet.getLong("id")));
            return client;
        } catch (SQLException e) {
            throw new SqlExceptionUnchecked(e);
        }
    }

    static Book getBook(final ResultSet resultSet) {
        try {
            final Book book = new Book(
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getBigDecimal("price"));
            book.setId(BigInteger.valueOf(resultSet.getLong("id")));
            return book;
        } catch (SQLException e) {
            throw new SqlExceptionUnchecked(e);
        }
    }

    static Order getOrder(final ResultSet resultSet) {
        try {
            final Book book = new Book(resultSet.getString("bookTitle"),
                    resultSet.getString("bookAuthor"),
                    resultSet.getBigDecimal("bookPrice"));
            book.setId(BigInteger.valueOf(resultSet.getLong("bookId")));

            final ResultSet clientResultSet = executeQuery("select * from client where id = " + resultSet.getLong("clientId"));
            final Optional<Client> optionalClient = next(clientResultSet) ?
                    Optional.of(getClient(clientResultSet))
                    : Optional.empty();
            final Order order = new Order(
                    optionalClient
                            .orElseThrow(
                                    () -> new RuntimeException("Given client id no longer in client table!")),
                    book);
            order.setId(BigInteger.valueOf(resultSet.getLong("id")));
            return order;
        } catch (SQLException e) {
            throw new SqlExceptionUnchecked(e);
        }
    }

    public static void cleanAllDbs() {
        execute("delete from orderbook where id > -999");
        execute("delete from client where id > -999");
        execute("delete from book where id > -999");
    }

    static class SqlExceptionUnchecked extends RuntimeException {
        public SqlExceptionUnchecked(Throwable cause) {
            super(cause);
        }
    }
}
