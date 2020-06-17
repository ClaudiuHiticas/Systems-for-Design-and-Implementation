package ro.ubb.sdi08.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.remoting.rmi.RmiServiceExporter;
import ro.ubb.sdi08.domain.Book;
import ro.ubb.sdi08.domain.Client;
import ro.ubb.sdi08.domain.Order;
import ro.ubb.sdi08.repo.db.*;
import ro.ubb.sdi08.service.*;

import javax.sql.DataSource;
import java.math.BigInteger;


@Configuration
public class AppConfig {
    @Bean
    DataSource dataSource() {
        final BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:postgresql://localhost:5432/sdi");
        basicDataSource.setUsername(System.getProperty("pgUsername"));
        basicDataSource.setPassword(System.getProperty("pgPassword"));
        basicDataSource.setInitialSize(4);
        return basicDataSource;
    }

    @Bean
    JdbcOperations jdbcOperations() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    @Bean
    RepoDbHelper repoDbHelper() {
        return new RepoDbHelper(jdbcOperations());
    }

    // REPOS
    @Bean
    SortingRepository<BigInteger, Book> bookRepo() {
        return new RepoBookDb();
    }

    @Bean
    SortingRepository<BigInteger, Client> clientRepo() {
        return new RepoClientDb();
    }

    @Bean
    SortingRepository<BigInteger, Order> orderRepo() {
        return new RepoOrderDb();
    }

    // SERVICES
    @Bean
    BookService bookService() {
        return new BookServiceSrv(bookRepo());
    }

    @Bean
    ClientService clientService() {
        return new ClientServiceSrv(clientRepo());
    }

    @Bean
    OrderService orderService() {
        return new OrderServiceSrv(
                bookService(),
                clientService(),
                orderRepo());
    }

    // RMI SERVICES
    @Bean
    RmiServiceExporter rmiBookService() {
        final RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("BookService");
        rmiServiceExporter.setServiceInterface(BookService.class);
        rmiServiceExporter.setService(bookService());
        return rmiServiceExporter;
    }

    @Bean
    RmiServiceExporter rmiClientService() {
        final RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("ClientService");
        rmiServiceExporter.setServiceInterface(ClientService.class);
        rmiServiceExporter.setService(clientService());
        return rmiServiceExporter;
    }

    @Bean
    RmiServiceExporter rmiOrderService() {
        final RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("OrderService");
        rmiServiceExporter.setServiceInterface(OrderService.class);
        rmiServiceExporter.setService(orderService());
        return rmiServiceExporter;
    }
}
