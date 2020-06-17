package ro.ubb.sdi05.service;

import ro.ubb.sdi05.Message;
import ro.ubb.sdi05.domain.TotalOrder;
import ro.ubb.sdi05.tcp.TcpClient;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static ro.ubb.sdi05.CommonHelper.*;

public class OrderServiceClt implements OrderService {
    private TcpClient tcpClient;
    private ExecutorService executorService;

    public OrderServiceClt(final TcpClient tcpClient, final ExecutorService executorService) {
        this.tcpClient = tcpClient;
        this.executorService = executorService;
    }

    @Override
    public CompletableFuture<String> buyBook(BigInteger clientId, BigInteger bookId) {
        final Message message = new Message(BUY_BOOK, clientId, bookId);
        return CompletableFuture.supplyAsync(
                () -> (String) tcpClient.sendAndReceive(message),
                executorService);
    }

    @Override
    public CompletableFuture<List<TotalOrder>> getTopOrders() {
        return CompletableFuture.supplyAsync(
                () -> (List<TotalOrder>) tcpClient.sendAndReceive(new Message(SHOW_TOP_CLIENTS)),
                executorService);
    }
}
