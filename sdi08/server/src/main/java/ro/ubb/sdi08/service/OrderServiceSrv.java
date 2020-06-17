package ro.ubb.sdi08.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.sdi08.domain.Book;
import ro.ubb.sdi08.domain.Order;
import ro.ubb.sdi08.domain.TotalOrder;
import ro.ubb.sdi08.repo.db.SortingRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OrderServiceSrv implements OrderService {
    private final SortingRepository repoOrder;
    private final BookService bookService;
    private final ClientService clientService;
    private BigInteger index = BigInteger.ZERO;

    @Autowired
    public OrderServiceSrv(final BookService bookService,
                           final ClientService clientService,
                           final SortingRepository repoOrder) {

        this.bookService = bookService;
        this.clientService = clientService;
        this.repoOrder = repoOrder;
    }


    public List<TotalOrder> getTopOrders() {
        final ArrayList<TotalOrder> totalOrders = new ArrayList<>();
        final Iterable<Order> allOrders = repoOrder.findAll();
        StreamSupport.stream(allOrders.spliterator(), true)
                .collect(Collectors.groupingBy(order -> order.getClientId()))
                .forEach((clientId, orders) ->
                        totalOrders.add(new TotalOrder(
                                clientId,
                                orders.stream()
                                        .map(order -> order.getBook().getPrice())
                                        .reduce(BigDecimal.ZERO, BigDecimal::add))));
        return totalOrders.stream()
                .sorted(Comparator.comparing(TotalOrder::getTotalSpent))
                .collect(Collectors.toList());
    }


    @Override
    public String buyBook(final BigInteger clientId,
                          final BigInteger bookId) throws Throwable {

        final Book book = bookService.getById(bookId);
        index = index.add(BigInteger.ONE);
        final Order order = new Order(index, clientId, book);
        return repoOrder.save(order).isPresent() ?
                "strange. order with same id already in db!"
                : "thank you for buying from us!";
    }
}
