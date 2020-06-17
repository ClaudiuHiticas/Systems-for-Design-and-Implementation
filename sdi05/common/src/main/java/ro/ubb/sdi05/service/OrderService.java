package ro.ubb.sdi05.service;

import ro.ubb.sdi05.domain.TotalOrder;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<String> buyBook(BigInteger clientId, BigInteger bookId);
    CompletableFuture<List<TotalOrder>> getTopOrders();
}
