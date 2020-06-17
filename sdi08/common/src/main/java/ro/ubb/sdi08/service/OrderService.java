package ro.ubb.sdi08.service;

import ro.ubb.sdi08.domain.TotalOrder;

import java.math.BigInteger;
import java.util.List;

public interface OrderService {
    List<TotalOrder> getTopOrders();

    String buyBook(final BigInteger idClient, final BigInteger idBook) throws Throwable;
}
