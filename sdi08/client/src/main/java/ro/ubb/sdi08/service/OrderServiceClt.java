package ro.ubb.sdi08.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.sdi08.domain.TotalOrder;

import java.math.BigInteger;
import java.util.List;

public class OrderServiceClt implements OrderService {
    @Autowired
    private OrderService orderService;

    @Override
    public List<TotalOrder> getTopOrders() {
        return orderService.getTopOrders();
    }

    @Override
    public String buyBook(BigInteger idClient, BigInteger idBook) throws Throwable {
        return orderService.buyBook(idClient, idBook);
    }
}
