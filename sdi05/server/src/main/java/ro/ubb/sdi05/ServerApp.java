package ro.ubb.sdi05;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    public static void main(String[] args) {
        System.out.println("server started");
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        TcpServer tcpServer = new TcpServer(executorService);

        tcpServer.startServer();

        executorService.shutdown();
    }
}
