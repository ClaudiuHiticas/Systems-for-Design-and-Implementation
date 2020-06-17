package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ubb.catalog.core.model.Ordered;
import ro.ubb.catalog.core.repository.OrderedRepository;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;


@Service
public class OrderedServiceImpl implements OrderedService {
    public static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private OrderedRepository orderedRepository;

    @Override
    public List<Ordered> getAllOrders() {
        log.trace("getAllBooks --- method entered");
        List<Ordered> result = orderedRepository.findAll();
        log.trace("getAllBooks: result={}", result);
        return result;
    }

    @Override
    public BigInteger getBestSeller() {
        log.trace("getBestSeller - method entered");
        List<Ordered> orderList = orderedRepository.findAll();
        Set<Ordered> order = new HashSet<>(orderList);
        Map<BigInteger, Long> countForId = order.stream()
                .collect(Collectors.groupingBy(Ordered::getBookId, Collectors.counting()));

        Map<BigInteger, Long> sortedByValue = countForId.entrySet()
                .stream()
                .sorted(Map.Entry.<BigInteger, Long> comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Optional<Map.Entry<BigInteger, Long>> Book = sortedByValue.entrySet().stream().findFirst();
        log.trace("getBestSeller - method finished");
        return Book.get().getKey();
    }

    @Override
    public BigInteger getBestClient() {
        log.trace("getBestClient - method entered");
        List<Ordered> orderList = orderedRepository.findAll();
        Set<Ordered> order = new HashSet<>(orderList);
        Map<BigInteger, Long> countForId = order.stream()
                .collect(Collectors.groupingBy(Ordered::getClientId, Collectors.counting()));

        Map<BigInteger, Long> sortedByValue = countForId.entrySet()
                .stream()
                .sorted(Map.Entry.<BigInteger, Long> comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Optional<Map.Entry<BigInteger, Long>> Client = sortedByValue.entrySet().stream().findFirst();
        log.trace("getBestSeller - method finished");
        return Client.get().getKey();
    }


    @Override
    public Ordered buyBook(Ordered ordered) {
        log.trace("buyBook --- method entered");
        return orderedRepository.save(ordered);
    }


    @Override
    public void deleteById(BigInteger id) {
        log.trace("deleteById --- method entered");
        orderedRepository.deleteById(id);

    }

}
