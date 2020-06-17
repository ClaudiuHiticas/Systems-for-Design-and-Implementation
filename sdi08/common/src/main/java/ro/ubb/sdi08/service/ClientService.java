package ro.ubb.sdi08.service;

import ro.ubb.sdi08.domain.Client;
import ro.ubb.sdi08.domain.Sort;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public interface ClientService {

    Set<Client> getAll();
    
    Client getById(final BigInteger clientId) throws Throwable;

    String save(final Client client);

    String update(final Client client);

    String delete(final BigInteger clientId);

    List<Client> findAllSorted(final Sort sort);
}
