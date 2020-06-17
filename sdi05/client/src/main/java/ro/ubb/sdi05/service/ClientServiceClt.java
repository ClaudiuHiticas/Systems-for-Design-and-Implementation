package ro.ubb.sdi05.service;

import ro.ubb.sdi05.Message;
import ro.ubb.sdi05.domain.Book;
import ro.ubb.sdi05.domain.Client;
import ro.ubb.sdi05.domain.Sort;
import ro.ubb.sdi05.tcp.TcpClient;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static ro.ubb.sdi05.CommonHelper.*;

public class ClientServiceClt implements ClientService {
    private TcpClient tcpClient;
    private ExecutorService executorService;

    public ClientServiceClt(final TcpClient tcpClient,final ExecutorService executorService) {
        this.tcpClient = tcpClient;
        this.executorService = executorService;
    }

    @Override
    public CompletableFuture<String> addClient(BigInteger id, Client client) {
        final Message message = new Message(ADD_CLIENT, id, client);
        return CompletableFuture.supplyAsync(
                () -> (String) tcpClient.sendAndReceive(message),
                executorService);
    }

    @Override
    public CompletableFuture<Set<Client>> getAllClients() {
        return CompletableFuture.supplyAsync(
                () -> (Set<Client>) tcpClient.sendAndReceive(new Message(SHOW_ALL_CLIENTS)),
                executorService);
    }

    @Override
    public CompletableFuture<Message> getClientById(BigInteger id) {
        return CompletableFuture.supplyAsync(
                () -> (Message) tcpClient.sendAndReceive(new Message(FIND_CLIENT, id)),
                executorService);
    }

    @Override
    public CompletableFuture<String> updateClient(BigInteger id, Client client) {
        return CompletableFuture.supplyAsync(
                () -> (String) tcpClient.sendAndReceive(new Message(UPDATE_CLIENT, id, client)),
                executorService);
    }

    @Override
    public CompletableFuture<String> deleteClient(BigInteger id) {
        return CompletableFuture.supplyAsync(
                () -> (String) tcpClient.sendAndReceive(new Message(DELETE_CLIENT, id)),
                executorService);
    }

}
