package ro.ubb.sdi08.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.sdi08.domain.Client;
import ro.ubb.sdi08.domain.Sort;
import ro.ubb.sdi08.repo.db.SortingRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toSet;


public class ClientServiceSrv implements ClientService {
    private SortingRepository clientRepo;

    @Autowired
    public ClientServiceSrv(final SortingRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public Set<Client> getAll() {
        final Iterable<Client> clients = clientRepo.findAll();
        return StreamSupport.stream(
                clients.spliterator(),
                false)
                .collect(toSet());
    }

    @Override
    public Client getById(final BigInteger clientId) throws Throwable {
        return (Client) clientRepo.findOne(clientId).orElseThrow(() -> new RuntimeException("could not find client with id: "+ clientId));
    }

    @Override
    public String save(final Client client) {
        return clientRepo.save(client).isPresent() ?
                "client with same id already in db!" : "client saved! " + client;
    }

    @Override
    public String update(final Client client) {
        return clientRepo.update(client).isPresent() ?
                "client with given id not found in db!"
                : "client updated!" + client;
    }

    @Override
    public String delete(final BigInteger clientId) {
        return clientRepo.delete(clientId).isPresent() ?
                "client with id " + clientId + " was removed from the db!"
                : "no client with the given id found in the db!";

    }

    @Override
    public List<Client> findAllSorted(final Sort sort) {
        final Iterable<Client> books = clientRepo.findAll(sort);
        return StreamSupport.stream(
                books.spliterator(),
                false)
                .collect(Collectors.toList());
    }
}

