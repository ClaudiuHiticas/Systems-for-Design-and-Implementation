package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ubb.catalog.core.model.Client;
import ro.ubb.catalog.core.repository.ClientRepository;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ClientServiceImpl implements ClientService{
    public static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        log.trace("getAllClients --- method entered");
        List<Client> result = clientRepository.findAll();
        log.trace("getAllClients: result={}", result);
        return result;
    }

    @Override
    public Optional<Client> getClientById(BigInteger clientId) {
        log.trace("getClientById --- method entered");
        Optional<Client> result = clientRepository.findById(clientId);
        log.trace("getClientById: result={}", result);
        return result;

    }

    @Override
    public Client saveClient(Client client) {
        log.trace("saveClient --- method entered");
        Client result = clientRepository.save(client);
        log.trace("saveClient: result={}", result);
        return result;
    }

    @Override
    public Client updateClient(BigInteger id, Client client) {
        log.trace("update client with id: {}, with client: {}", client.getId(), client);
        Client update = clientRepository.findById(id).orElse(client);
        update.setName(client.getName());
        log.trace("updateClient - method finished");
        return update;
    }

    @Override
    public void deleteById(BigInteger id) {
        log.trace("deleteById --- method entered");
        clientRepository.deleteById(id);
    }

    @Override
    public Set<Client> filtredClientsByName(String name) {
        log.trace("filteredClientsByTitle - method entered");
        Iterable<Client> clients = clientRepository.findAll();
        Set<Client> filteredClients = new HashSet<>();
        clients.forEach(filteredClients::add);
        filteredClients.removeIf(client -> !client.getName().contains(name));
        return filteredClients;
    }
}
