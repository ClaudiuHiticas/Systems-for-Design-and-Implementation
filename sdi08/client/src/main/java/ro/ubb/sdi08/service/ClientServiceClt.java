package ro.ubb.sdi08.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.sdi08.domain.Client;
import ro.ubb.sdi08.domain.Sort;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public class ClientServiceClt implements ClientService {

    @Autowired
    private ClientService clientService;

    @Override
    public Set<Client> getAll() {
        return clientService.getAll();
    }

    @Override
    public Client getById(BigInteger clientId) throws Throwable {
        return clientService.getById(clientId);
    }

    @Override
    public String save(Client client) {
        return clientService.save(client);
    }

    @Override
    public String update(Client client) {
        return clientService.update(client);
    }

    @Override
    public String delete(BigInteger clientId) {
        return clientService.delete(clientId);
    }

    @Override
    public List<Client> findAllSorted(Sort sort) {
        return findAllSorted(sort);
    }
}
