package ro.ubb.catalog.core.service;

import ro.ubb.catalog.core.model.Ordered;

import java.math.BigInteger;
import java.util.List;

public interface OrderedService {
    List<Ordered> getAllOrders();

    BigInteger getBestSeller();

    BigInteger getBestClient();

    Ordered buyBook(Ordered ordered);

    void deleteById(BigInteger id);
}
