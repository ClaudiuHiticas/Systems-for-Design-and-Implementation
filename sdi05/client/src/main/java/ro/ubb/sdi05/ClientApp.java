package ro.ubb.sdi05;

import ro.ubb.sdi05.service.BookServiceClt;
import ro.ubb.sdi05.service.ClientServiceClt;
import ro.ubb.sdi05.service.OrderServiceClt;
import ro.ubb.sdi05.tcp.TcpClient;
import ro.ubb.sdi05.ui.Console;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApp {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        TcpClient tcpClient = new TcpClient();
        final BookServiceClt bookServiceClt = new BookServiceClt(tcpClient, executorService);
        final ClientServiceClt clientServiceClt = new ClientServiceClt(tcpClient, executorService);
        final OrderServiceClt orderServiceClt = new OrderServiceClt(tcpClient, executorService);
        Console console = new Console(bookServiceClt, clientServiceClt, orderServiceClt);
        console.runConsole();

        executorService.shutdown();

        System.out.println("bye client");
    }
}
