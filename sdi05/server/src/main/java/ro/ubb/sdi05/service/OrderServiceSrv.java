package ro.ubb.sdi05.service;

import ro.ubb.sdi05.domain.*;
import ro.ubb.sdi05.domain.validators.ValidatorException;
import ro.ubb.sdi05.repo.db.RepoOrderDb;
import ro.ubb.sdi05.repo.db.SortingRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class OrderServiceSrv {
    private final SortingRepository<BigInteger, Order> repoOrder;
    private final BookServiceSrv bookService;
    private final ClientServiceSrv clientService;
    private BigInteger index;

    public OrderServiceSrv(final BookServiceSrv bookService,
                           final ClientServiceSrv clientService) {
        this.bookService = bookService;
        this.clientService = clientService;
        index = BigInteger.ZERO;
        repoOrder = new RepoOrderDb();
    }


    public Optional<Order> buyBook(final BigInteger clientId,
                                   final BigInteger bookId) {
        final Client client = clientService.getClientById(clientId)
                .orElseThrow(() -> new ValidatorException("Given client id could not be found!"));
        final Book book = bookService.getBookById(bookId)
                .orElseThrow(() -> new ValidatorException("Given book id could not be found!"));

        final Order order = new Order(client, book);
        index = index.add(BigInteger.ONE);
        order.setId(index);
        return repoOrder.save(order);

    }

    public List<TotalOrder> getTopOrders() {
        final ArrayList<TotalOrder> totalOrders = new ArrayList<>();
        StreamSupport.stream(repoOrder.findAll().spliterator(), true)
                .collect(Collectors.groupingBy(order -> order.getClient()))
                .forEach((client, orders) ->
                        totalOrders.add(new TotalOrder(
                                client,
                                orders.stream()
                                        .map(order -> order.getBook().getPrice())
                                        .reduce(BigDecimal.ZERO, BigDecimal::add))));
        return totalOrders.stream()
                .sorted(Comparator.comparing(TotalOrder::getTotalSpent))
                .collect(Collectors.toList());
    }

    public List<Order> findAllSorted(final Sort sort) {
        return StreamSupport
                .stream(
                        repoOrder
                                .findAll(sort)
                                .spliterator(),
                        false)
                .collect(toList());
    }
}
