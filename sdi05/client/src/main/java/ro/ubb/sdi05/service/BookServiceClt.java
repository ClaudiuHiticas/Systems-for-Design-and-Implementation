package ro.ubb.sdi05.service;

import ro.ubb.sdi05.Message;
import ro.ubb.sdi05.domain.Book;
import ro.ubb.sdi05.domain.Sort;
import ro.ubb.sdi05.tcp.TcpClient;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static ro.ubb.sdi05.CommonHelper.*;

public class BookServiceClt implements BookService {
    private TcpClient tcpClient;
    private ExecutorService executorService;

    public BookServiceClt(final TcpClient tcpClient,
                          final ExecutorService executorService) {
        this.tcpClient = tcpClient;
        this.executorService = executorService;
    }

    @Override
    public CompletableFuture<String> addBook(BigInteger id, Book book) {
        final Message message = new Message(ADD_BOOK, id, book);
        return CompletableFuture.supplyAsync(
                () -> (String) tcpClient.sendAndReceive(message),
                executorService);
    }

    @Override
    public CompletableFuture<Set<Book>> getAllBooks() {
        return CompletableFuture.supplyAsync(
                () -> (Set<Book>) tcpClient.sendAndReceive(new Message(SHOW_ALL_BOOKS)),
                executorService);
    }


    @Override
    public CompletableFuture<Message> getBookById(BigInteger id) {
        return CompletableFuture.supplyAsync(
                () -> (Message) tcpClient.sendAndReceive(new Message(FIND_BOOK, id)),
                executorService);
    }

    @Override
    public CompletableFuture<String> updateBook(BigInteger id, Book book) {
        return CompletableFuture.supplyAsync(
                () -> (String) tcpClient.sendAndReceive(new Message(UPDATE_BOOK, id, book)),
                executorService);
    }

    @Override
    public CompletableFuture<String> deleteBook(BigInteger id) {
        return CompletableFuture.supplyAsync(
                () -> (String) tcpClient.sendAndReceive(new Message(DELETE_BOOK, id)),
                executorService);
    }

    @Override
    public CompletableFuture<List<Book>> findAllSorted(final Sort sort) {
        return CompletableFuture.supplyAsync(
                () -> (List<Book>) tcpClient.sendAndReceive(new Message(SHOW_ALL_BOOKS_SORTED, sort)),
                executorService);
    }

    public CompletableFuture<String> cleanAllTables() {
        return CompletableFuture.supplyAsync(
                () -> (String) tcpClient.sendAndReceive(new Message(CLEAN_ALL_DBS)),
                executorService);
    }
}
