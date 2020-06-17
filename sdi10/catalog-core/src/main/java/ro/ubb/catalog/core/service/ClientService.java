package ro.ubb.catalog.core.service;

import ro.ubb.catalog.core.model.Client;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ClientService {
    List<Client> getAllClients();

    Optional<Client> getClientById(BigInteger clientId);

    Client saveClient(Client client);

    Client updateClient(BigInteger id, Client client);

    void deleteById(BigInteger id);

    Set<Client> filtredClientsByName(String name);

}
