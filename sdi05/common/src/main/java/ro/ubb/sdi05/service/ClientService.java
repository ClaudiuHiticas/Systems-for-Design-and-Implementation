package ro.ubb.sdi05.service;

import ro.ubb.sdi05.Message;
import ro.ubb.sdi05.domain.Client;
import ro.ubb.sdi05.domain.Sort;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface ClientService {
    CompletableFuture<String> addClient(BigInteger id,
                                      Client client);

    CompletableFuture<Set<Client>> getAllClients();

    CompletableFuture<Message> getClientById(BigInteger clientId);

    CompletableFuture<String> updateClient(BigInteger clientId,
                                         Client client);

    CompletableFuture<String> deleteClient(BigInteger clientId);
}
