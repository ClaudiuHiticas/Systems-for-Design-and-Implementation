package ro.ubb.catalog.client;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.client.RestTemplate;
import ro.ubb.catalog.core.model.Book;
import ro.ubb.catalog.core.model.Client;
import ro.ubb.catalog.web.dto.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.Double.parseDouble;

public class ClientApp {
    public static final String URLBooks = "http://localhost:8080/api/books";
    public static final String URLClients = "http://localhost:8080/api/clients";
    public static final String URLOrders = "http://localhost:8080/api/orders";
    public static final Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        "ro.ubb.catalog.client.config"
                );

        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        runConsole(restTemplate);

        System.out.println("bye ");
    }

    private static void demoBook(RestTemplate restTemplate){
        BookDto savedBook = restTemplate.postForObject(
                URLBooks,
                new BookDto("HP", "JK", BigDecimal.valueOf(22.4)),
                BookDto.class);
        System.out.println("savedBook: " + savedBook);

        System.out.println("update:");
        savedBook.setTitle("HP2");
        restTemplate.put(
                URLBooks + "/{id}",
                savedBook,
                savedBook.getId());
        printAllBooks(restTemplate);

        System.out.println("Delete");
        restTemplate.delete(URLBooks+"/{id}", savedBook.getId());
        printAllBooks(restTemplate);
    }

    private static void demoClient(RestTemplate restTemplate) {
        ClientDto savedClient = restTemplate.postForObject(
                URLClients,
                new ClientDto("Johnny"),
                ClientDto.class);
        System.out.println("savedClient: " + savedClient);

        System.out.println("update:");
        savedClient.setName("Jhon");
        restTemplate.put(
                URLClients + "/{id}",
                savedClient,
                savedClient.getId());
        printAllClients(restTemplate);

        System.out.println("Delete");
        restTemplate.delete(URLClients + "/{id}", savedClient.getId());
        printAllClients(restTemplate);
    }

    private static void demoOrder(RestTemplate restTemplate){
        OrderedDto savedOrder = restTemplate.postForObject(
                URLOrders,
                new OrderedDto(BigInteger.valueOf(13), BigInteger.valueOf(14)),
                OrderedDto.class);
        System.out.println("savedOrder: " + savedOrder);


    }

    public static String mainMenu() {
        return "[Main Menu]\n" +
                "1. Book\n" +
                "2. Client\n" +
                "0. Exit\n" +
                "Enter your option:";
    }

    public static String booksMenu() {
        return "[Book Menu]\n" +
                "1. Add book\n" +
                "2. Update book\n" +
                "3. Delete book\n" +
                "4. Show all books\n" +
                "0. Exit\n" +
                "Enter your option: ";
    }

    public static String clientsMenu() {
        return "[Client Menu]\n" +
                "1. Add client\n" +
                "2. Update client\n" +
                "3. Delete client\n" +
                "4. Show all clients\n" +
                "5. I want to buy a book\n" +
                "6. Show all orders\n" +
                "0. Exit\n" +
                "Enter your option: ";
    }

    public static void runConsole(RestTemplate restTemplate) {

        try {
            Stream.iterate(0, i -> i + 1)
                    .forEach(i -> {
                        System.out.println(mainMenu());
                        final String mainOption = scanner.next();
                        switch (mainOption) {
                            case "1":
                                //Books
                                System.out.println(booksMenu());
                                final String bookOption = scanner.next();
                                switch (bookOption) {
                                    case "1":
                                        addBook(restTemplate);
                                        break;
                                    case "2":
                                        updateBook(restTemplate);
                                        break;
                                    case "3":
                                        deleteBook(restTemplate);
                                        break;
                                    case "4":
                                        printAllBooks(restTemplate);
                                        break;
                                    case "5":
                                        demoBook(restTemplate);
                                        break;
                                    case "0":
                                        throw new CloseException("Bye!");
                                }
                                break;
                            case "2":
                                //Clients
                                System.out.println(clientsMenu());
                                final String clientOption = scanner.next();
                                switch (clientOption) {
                                    case "1":
                                        addClient(restTemplate);
                                        break;
                                    case "2":
                                        updateClient(restTemplate);
                                        break;
                                    case "3":
                                        deleteClient(restTemplate);
                                        break;
                                    case "4":
                                        printAllClients(restTemplate);
                                        break;
                                    case "5":
                                        buyBook(restTemplate);
                                        break;
                                    case "6":
                                        printAllOrders(restTemplate);
                                        break;
                                    case "0":
                                        throw new CloseException("Bye!");
                                }
                                break;
                            case "0":
                                throw new CloseException("Bye!");
                            default:
                                System.out.println("You choose a wrong option!");
                                break;
                        }
                    });
        } catch (CloseException ex) {
            System.out.println("Bye!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void getClientByName(RestTemplate restTemplate) {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String substring = "";
        try {
            System.out.println("Enter the substring for the title:");
            substring = bufferRead.readLine();
        } catch( IOException exception ){
            System.out.println(exception.toString());
        }
        ClientsDto allClients = restTemplate.getForObject(
                URLClients + "/{filter}",
                ClientsDto.class,
                substring);
        System.out.println(allClients);
    }

    private static void findClientById(RestTemplate restTemplate) {
        BigInteger id = readId();
        ClientsDto allClients = restTemplate.getForObject(
                URLClients + "/{id}",
                ClientsDto.class,
                id);
        System.out.println(allClients);
    }


    private static void getBookByTitle(RestTemplate restTemplate) {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String substring = "";
        try {
            System.out.println("Enter the substring for the title:");
            substring = bufferRead.readLine();
        } catch( IOException exception ){
            System.out.println(exception.toString());
        }
        BooksDto allBooks = restTemplate.getForObject(
                URLBooks + "/filterTitle/{filter}",
                BooksDto.class,
                substring);
        System.out.println(allBooks);
    }

    private static void findBookById(RestTemplate restTemplate) {
        BigInteger id = readId();
        BooksDto allBooks = restTemplate.getForObject(
                URLBooks + "/{id}",
                BooksDto.class,
                id);
        System.out.println(allBooks);
    }

    private static void deleteBook(RestTemplate restTemplate) {
        printAllBooks(restTemplate);
        BigInteger id = readId();
        restTemplate.delete(URLBooks+"/{id}", id);
    }

    private static void deleteClient(RestTemplate restTemplate) {
        printAllClients(restTemplate);
        BigInteger id = readId();
        restTemplate.delete(URLClients+"/{id}", id);
    }


    private static void updateBook(RestTemplate restTemplate) {
        printAllBooks(restTemplate);

        BigInteger id = readId();
        Book book = readBook();
        book.setId(id);
        restTemplate.put(
                URLBooks + "/{id}",
                book,
                book.getId());

        printAllBooks(restTemplate);
    }

    private static void updateClient(RestTemplate restTemplate) {
        printAllClients(restTemplate);

        BigInteger id = readId();
        Client client = readClient();
        client.setId(id);
        restTemplate.put(
                URLClients + "/{id}",
                client,
                client.getId());

        printAllClients(restTemplate);
    }

    private static void waitForUserInput() {
        System.out.println("Press Enter key to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static Book readBook() {
        System.out.println("Enter title: ");
        final String title = scanner.next();
        System.out.println("Enter author: ");
        final String author = scanner.next();
        System.out.println("Enter price: ");
        final BigDecimal price = BigDecimal.valueOf(parseDouble(scanner.next()));
        return new Book(title, author, price);
    }

    private static Client readClient() {
        System.out.println("Enter name:");
        final String name = scanner.next();
        return new Client(name);
    }

    private static BigInteger readId() {
        System.out.println("Enter id: ");
        try {
            return BigInteger.valueOf(Long.parseLong(scanner.next()));
        } catch (NumberFormatException ex) {
            System.out.println("Please use an integer for ID!");
        }
        return readId();
    }

    private static void addBook(RestTemplate restTemplate) {
        System.out.println("Enter title: ");
        final String title = scanner.next();
        System.out.println("Enter author: ");
        final String author = scanner.next();
        System.out.println("Enter price: ");
        final BigDecimal price = BigDecimal.valueOf(parseDouble(scanner.next()));

        restTemplate.postForObject(
                URLBooks,
                new BookDto(title, author, price),
                BookDto.class);
    }

    private static void addClient(RestTemplate restTemplate) {
        System.out.println("Enter name:");
        final String name = scanner.next();
        restTemplate.postForObject(
                URLClients,
                new ClientDto(name),
                ClientDto.class);
    }

    private static void clientBuyBook(final BigInteger clientId, final BigInteger bookId,
                                      final RestTemplate restTemplate) {
        OrderedDto savedOrder = restTemplate.postForObject(
                URLOrders,
                new OrderedDto(bookId, clientId),
                OrderedDto.class);
        System.out.println("savedOrder: " + savedOrder);
    }

    private static void buyBook(RestTemplate restTemplate) {
        printAllClients(restTemplate);
        System.out.println("What's your id?");
        BigInteger clientId = readId();
        printAllBooks(restTemplate);
        System.out.println("Which book you want to buy?");
        BigInteger bookId = readId();
        clientBuyBook(clientId, bookId, restTemplate);
        System.out.println("Book bought successfully!");
    }




    static class CloseException extends RuntimeException {
        public CloseException(String message) {
            super(message);
        }
    }

    private static void printAllBooks(RestTemplate restTemplate) {
        BooksDto allBooks = restTemplate.getForObject(URLBooks, BooksDto.class);
        System.out.println(allBooks);
    }

    private static void printAllClients(RestTemplate restTemplate) {
        ClientsDto allClients = restTemplate.getForObject(URLClients, ClientsDto.class);
        System.out.println(allClients);
    }

    private static void printAllOrders(RestTemplate restTemplate) {
        OrdersDto allOrders = restTemplate.getForObject(URLOrders, OrdersDto.class);
        System.out.println(allOrders);
    }



}

