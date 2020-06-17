package ro.ubb.sdi08.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import ro.ubb.sdi08.service.*;
import ro.ubb.sdi08.ui.Console;

@Configuration
public class AppConfig {

    @Bean
    RmiProxyFactoryBean rmiBookProxyFactoryBean() {
        final var rmiBookProxyFactoryBean = new RmiProxyFactoryBean();
        rmiBookProxyFactoryBean.setServiceInterface(BookService.class);
        rmiBookProxyFactoryBean.setServiceUrl("rmi://localhost:1099/BookService");
        return rmiBookProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiClientProxyFactoryBean() {
        final var rmiClientProxyFactoryBean = new RmiProxyFactoryBean();
        rmiClientProxyFactoryBean.setServiceInterface(ClientService.class);
        rmiClientProxyFactoryBean.setServiceUrl("rmi://localhost:1099/ClientService");
        return rmiClientProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiOrderProxyFactoryBean() {
        final var rmiOrderProxyFactoryBean = new RmiProxyFactoryBean();
        rmiOrderProxyFactoryBean.setServiceInterface(OrderService.class);
        rmiOrderProxyFactoryBean.setServiceUrl("rmi://localhost:1099/OrderService");
        return rmiOrderProxyFactoryBean;
    }


    //    SERVICES
    @Bean
    BookService bookService() {
        return new BookServiceClt();
    }

    @Bean
    ClientService clientService() {
        return new ClientServiceClt();
    }

    @Bean
    OrderService orderService() {
        return new OrderServiceClt();
    }

    //    console
    @Bean
    Console console() {
        return new Console(
                bookService(),
                clientService(),
                orderService());
    }
}
