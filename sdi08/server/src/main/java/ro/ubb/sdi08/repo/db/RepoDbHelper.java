package ro.ubb.sdi08.repo.db;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class RepoDbHelper {
    private static JdbcOperations jdbcOperations;

    public RepoDbHelper(final JdbcOperations jdbcOperations) {
        RepoDbHelper.jdbcOperations = jdbcOperations;
    }

    static <T> T query(final String sqlQuery,
                       final ResultSetExtractor<T> resultSetExtractor) {
        return jdbcOperations.query(sqlQuery, resultSetExtractor);
    }

    static <T> List<T> query(final String sqlQuery,
                             final RowMapper<T> rowMapper) {
        return jdbcOperations.query(sqlQuery, rowMapper);
    }

    static void execute(final String sqlQuery) {
        jdbcOperations.execute(sqlQuery);
    }


    static int executeUpdate(final String sqlQuery,
                             final Object... objects) {
        return jdbcOperations.update(sqlQuery, objects);
    }

    static boolean next(final ResultSet resultSet) {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new SqlExceptionUnchecked(e);
        }
    }

    public static void cleanAllTables() {
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
