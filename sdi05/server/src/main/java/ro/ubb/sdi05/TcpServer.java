package ro.ubb.sdi05;

import ro.ubb.sdi05.domain.Book;
import ro.ubb.sdi05.domain.Client;
import ro.ubb.sdi05.domain.Order;
import ro.ubb.sdi05.domain.Sort;
import ro.ubb.sdi05.domain.validators.ValidatorException;
import ro.ubb.sdi05.repo.db.RepoDbHelper;
import ro.ubb.sdi05.service.BookServiceSrv;
import ro.ubb.sdi05.service.ClientServiceSrv;
import ro.ubb.sdi05.service.OrderServiceSrv;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static ro.ubb.sdi05.CommonHelper.*;

public class TcpServer {
    private Map<String, Function<Message, Object>> methodHandlers;
    private ExecutorService executorService;
    private BookServiceSrv bookService;
    private ClientServiceSrv clientService;
    private OrderServiceSrv orderServiceSrv;

    public TcpServer(ExecutorService executorService) {
        this.executorService = executorService;
        this.methodHandlers = new HashMap<>();
        bookService = new BookServiceSrv();
        clientService = new ClientServiceSrv();
        orderServiceSrv = new OrderServiceSrv(bookService, clientService);
        initHandlers();
    }

    public void addHandler(String methodName, Function<Message, Object> handler) {
        methodHandlers.put(methodName, handler);
    }

    public void startServer() {
        try (var serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket client = serverSocket.accept();
                executorService.submit(() -> {
                    try (var objectInputStream = new ObjectInputStream(client.getInputStream());
                         var objectOutputStream = new ObjectOutputStream(client.getOutputStream())) {
                        final Message request = (Message) objectInputStream.readObject();
                        System.out.println("received request: " + request);
                        Object response = methodHandlers.get(request.getHeader()).apply(request);
                        objectOutputStream.writeObject(response);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initHandlers() {
        /*
        BOOK
         */
        addHandler(ADD_BOOK, request -> {
            try {
                final Optional<Book> optionalBook = bookService.addBook(
                        (BigInteger) request.getBody()[0],
                        (Book) request.getBody()[1]);
                return optionalBook.isPresent() ?
                        "book with the same id already in the db!"
                        : "book saved!";
            } catch (ValidatorException ve) {
                return String.valueOf(ve);
            }
        });
        addHandler(UPDATE_BOOK, request -> {
            try {
                final Optional<Book> optionalBook = bookService.updateBook(
                        (BigInteger) request.getBody()[0],
                        (Book) request.getBody()[1]);
                return optionalBook.isPresent() ?
                        "a book with the given id was not found in the db!"
                        : "book was updated!";
            } catch (ValidatorException ve) {
                return String.valueOf(ve);
            }

        });
        addHandler(DELETE_BOOK, request -> {
            final Optional<Book> optionalBook = bookService.deleteBook((BigInteger) request.getBody()[0]);
            return optionalBook.isPresent() ?
                    "book removed from db!"
                    : "book with given id not found!";
        });
        addHandler(FIND_BOOK, request -> {
            final Optional<Book> optionalBook = bookService.getBookById((BigInteger) request.getBody()[0]);
            return optionalBook.isPresent() ?
                    new Message(OK, optionalBook.get())
                    : new Message(BAD_MSG, "could not find book with given id!");
        });
        addHandler(SHOW_ALL_BOOKS, request -> bookService.getAllBooks());

        addHandler(SHOW_ALL_BOOKS_SORTED, request ->
                bookService
                        .findAllSorted((Sort) request.getBody()[0]));
        /*
        CLIENT
         */
        addHandler(ADD_CLIENT, request -> {
           try{
               final Optional<Client> optionalClient = clientService.addClient(
                       (BigInteger) request.getBody()[0],
                       (Client) request.getBody()[1]);
               return optionalClient.isPresent() ?
                       "client with the same id already in the db!"
                       : "client saved!";
           }catch(ValidatorException ve){
               return String.valueOf(ve);
           }
        });


        addHandler(UPDATE_CLIENT, request -> {
            try{
                final Optional<Client> optionalClient = clientService.updateClient(
                        (BigInteger) request.getBody()[0],
                        (Client) request.getBody()[1]);
                return optionalClient.isPresent() ?
                        "client with the same id was not found in the db!"
                        : "client saved!";
            } catch (ValidatorException ve) {
                return String.valueOf(ve);
            }
        });
        addHandler(DELETE_CLIENT, request -> {
            final Optional<Client> optionalClient = clientService.deleteClient((BigInteger) request.getBody()[0]);
            return optionalClient.isPresent() ?
                    "client removed from db!"
                    : "client with given id was not found!";
        });
        addHandler(FIND_CLIENT, (request) -> {
            final Optional<Client> optionalClient = clientService.getClientById((BigInteger) request.getBody()[0]);
            return optionalClient.map(client -> new Message(OK, client)).orElseGet(() -> new Message(
                    BAD_MSG, "could not find client with given id!"));
        });

        addHandler(SHOW_ALL_CLIENTS, (request) -> clientService.getAllClients());

        /*
        ORDER
         */

        addHandler(BUY_BOOK, request -> {
            final Optional<Order> optionalOrder = orderServiceSrv.buyBook(
                    (BigInteger) request.getBody()[0],
                    (BigInteger) request.getBody()[1]);
            return optionalOrder.isPresent() ?
                    "order with given id already in the db"
                    :"order was placed! thank you for buying from us <3";
        });

        addHandler(SHOW_TOP_CLIENTS, request ->  orderServiceSrv.getTopOrders());

        /*
        bonus
         */
        addHandler(CLEAN_ALL_DBS, request -> {
            RepoDbHelper.cleanAllDbs();
            return "how did you found this secret option? :-O";

        });
    }
}

