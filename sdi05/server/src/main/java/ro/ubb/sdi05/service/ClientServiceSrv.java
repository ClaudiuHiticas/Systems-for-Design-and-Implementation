package ro.ubb.sdi05.service;

import ro.ubb.sdi05.domain.Client;
import ro.ubb.sdi05.domain.Sort;
import ro.ubb.sdi05.domain.validators.ValidatorException;
import ro.ubb.sdi05.repo.db.RepoClientDb;
import ro.ubb.sdi05.repo.db.SortingRepository;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;


public class ClientServiceSrv {
    private SortingRepository<BigInteger, Client> repoClient;

    public ClientServiceSrv() {
        repoClient = new RepoClientDb();
    }

    public Optional<Client> addClient(final BigInteger id,
                                      final Client client) throws ValidatorException {
        client.setId(id);
        return repoClient.save(client);
    }

    public Set<Client> getAllClients() {
        Iterable<Client> clients = repoClient.findAll();
        return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
    }

    public Set<Client> filteredClientsByName(String name) {
        Iterable<Client> clients = repoClient.findAll();
        Set<Client> filteredClients = new HashSet<>();
        clients.forEach(filteredClients::add);
        filteredClients.removeIf(client -> !client.getName().contains(name));
        return filteredClients;
    }

    public Optional<Client> getClientById(final BigInteger clientId) {
        return repoClient.findOne(clientId);
    }

    public Optional<Client> updateClient(final BigInteger clientId,
                                         final Client client) {
        client.setId(clientId);
        return repoClient.update(client);
    }

    public Optional<Client> deleteClient(final BigInteger clientId) {
        return repoClient.delete(clientId);
    }

    public List<Client> findAllSorted(final Sort sort) {
        return StreamSupport
                .stream(
                        repoClient
                                .findAll(sort)
                                .spliterator(),
                        false)
                .collect(toList());
    }
}

