package ro.ubb.sdi05.ui;

import ro.ubb.sdi05.domain.Book;
import ro.ubb.sdi05.domain.Client;
import ro.ubb.sdi05.domain.Sort;
import ro.ubb.sdi05.service.BookServiceClt;
import ro.ubb.sdi05.service.ClientServiceClt;
import ro.ubb.sdi05.service.OrderServiceClt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.lang.Double.parseDouble;
import static ro.ubb.sdi05.CommonHelper.OK;

public class Console {
    private BookServiceClt bookServiceClt;
    private ClientServiceClt clientServiceClt;
    private OrderServiceClt orderServiceClt;
    private final Scanner scanner;

    public Console(BookServiceClt bookServiceClt,
                   ClientServiceClt clientServiceClt,
                   OrderServiceClt orderServiceClt) {
        this.bookServiceClt = bookServiceClt;
        this.clientServiceClt = clientServiceClt;
        this.orderServiceClt = orderServiceClt;
        scanner = new Scanner(System.in);
    }

    public void runConsole() {
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
                                        addBook(
                                                readId(),
                                                readBook());
                                        break;
                                    case "2":
                                        showAllBooks();
                                        updateBook(
                                                readId(),
                                                readBook());
                                        break;
                                    case "3":
                                        showAllBooks();
                                        deleteBook(readId());
                                        break;
                                    case "4":
                                        findBookByID(readId());
                                        break;
                                    case "5":
                                        showAllBooks();
                                        break;
                                    case "6":
                                        final Sort sort = new Sort();
                                        String sortOption = "";
                                        do {
                                            sortOption = sortMainMenu();
                                            if (Objects.equals(sortOption, "1")) {
                                                sort.and(sortSecondaryMenu());
                                            }
                                        } while (!Objects.equals(sortOption, "2"));
                                        if (Objects.equals(sort.getFieldsOrder().size(), 0)) {
                                            System.out.println("No sorting criterion provided!");
                                        } else {
                                            showAllBooks(sort);
                                            bookServiceClt.findAllSorted(sort)
                                                    .thenAccept(books ->
                                                            books.stream()
                                                                    .forEach(System.out::println));
                                        }
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
                                        addClient(
                                                readId(),
                                                readClient());
                                        break;
                                    case "2":
                                        showAllClients();
                                        updateClient(
                                                readId(),
                                                readClient());
                                        break;
                                    case "3":
                                        showAllClients();
                                        deleteClient(readId());
                                        break;
                                    case "4":
                                    findClientByID(readId());
                                        break;
                                    case "5":
                                        showAllClients();
                                        break;
                                    case "6":
                                        showAllClients();
                                        System.out.println("For client:");
                                        BigInteger idClient = readId();
                                        showAllBooks();
                                        System.out.println("For book:");
                                        BigInteger idBook = readId();
                                        buyBook(
                                                idClient,
                                                idBook
                                        );
                                        break;
                                    case "7":
                                        showChart();
                                        break;
                                    case "0":
                                    throw new CloseException("Bye!");
                                }
                                break;
                            case "3":
                                demo();
                                break;
                            case "9":
                                cleanAllTables();
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

    public Sort sortSecondaryMenu() {
        System.out.println("Enter field name");
        final String fieldName = scanner.next();
        System.out.println("1. ASC\n2. DESC\nYour option: ");
        final String option = scanner.next();
        if (Objects.equals(option, "1")) {
            return new Sort(Sort.Direction.ASC, fieldName);
        } else {
            return new Sort(Sort.Direction.DESC, fieldName);
        }

    }

    public String sortMainMenu() {
        System.out.println("1. Add new sort criterion\n" +
                "2. Save and exit\n" +
                "Enter your option: ");
        return scanner.next();

    }

    public String mainMenu() {
        return "[Main Menu]\n" +
                "1. Book\n" +
                "2. Client\n" +
                "0. Exit\n" +
                "Enter your option:";
    }

    public String booksMenu() {
        return "[Book Menu]\n" +
                "1. Add book\n" +
                "2. Update book\n" +
                "3. Delete book\n" +
                "4. Find a book\n" +
                "5. Show all books\n" +
                "6. Show all books sorted in a certain way\n" +
                "0. Exit\n" +
                "Enter your option: ";
    }

    public String clientsMenu() {
        return "[Client Menu]\n" +
                "1. Add client\n" +
                "2. Update client\n" +
                "3. Delete client\n" +
                "4. Find a client\n" +
                "5. Show all clients\n" +
                "6. I want to buy a book\n" +
                "7. Sort clients based on the spent amount of money\n" +
                "0. Exit\n" +
                "Enter your option: ";
    }


    private void addClient(final BigInteger id,
                           final Client client){
        clientServiceClt.addClient(
                id,
                client
        ).thenAccept(System.out::println);
    }

    private void updateClient(final BigInteger id,
                              final Client client){
        clientServiceClt.updateClient(
                id,
                client
        ).thenAccept(System.out::println);
    }

    private void deleteClient(BigInteger id){
       clientServiceClt
               .deleteClient(id)
               .thenAccept(System.out::println);
    }

    private void findClientByID(final BigInteger id){
        clientServiceClt.getClientById(id)
                .thenAccept(message -> {
                    if (Objects.equals(message.getHeader(), OK)){
                        final Client client = (Client) message.getBody()[0];
                        System.out.println(client);
                    } else {
                        final String msg = (String) message.getBody()[0];
                        System.out.println(msg);
                    }
                });
    }

    private void showAllClients(){
        clientServiceClt.getAllClients()
                .thenAccept(clients -> clients.forEach(System.out::println));
    }

    private void buyBook(BigInteger idClient, BigInteger idBook) {
        orderServiceClt.buyBook(idClient, idBook)
                .thenAccept(System.out::println);

    }

    private void showChart() {
        orderServiceClt.getTopOrders().
                thenAccept(orders -> orders.forEach(System.out::println));
    }

    private void addBook(final BigInteger id,
                         final Book book) {
        bookServiceClt
                .addBook(
                        id,
                        book)
                .thenAccept(System.out::println);
    }


    private void updateBook(final BigInteger id,
                            final Book book) {
        bookServiceClt
                .updateBook(
                        id,
                        book)
                .thenAccept(System.out::println);
    }

    private void deleteBook(final BigInteger id) {
        bookServiceClt
                .deleteBook(id)
                .thenAccept(System.out::println);

    }


    private void findBookByID(final BigInteger id) {
        bookServiceClt.getBookById(id)
                .thenAccept(message -> {
                    if (Objects.equals(message.getHeader(), OK)) {
                        final Book book = (Book) message.getBody()[0];
                        System.out.println(book);
                    } else {
                        final String msg = (String) message.getBody()[0];
                        System.out.println(msg);
                    }
                });
    }

    private void showAllBooks() {
        bookServiceClt.getAllBooks()
                .thenAccept(books ->
                        books.forEach(System.out::println));
    }

    private void showAllBooks(final Sort sort) {
        bookServiceClt.findAllSorted(sort)
                .thenAccept(books ->
                        books.forEach(System.out::println));
    }

    private void demo() {
        addBook(
                BigInteger.valueOf(1),
                new Book(
                        "title1",
                        "authorD",
                        BigDecimal.valueOf(1.1)));
        addBook(
                BigInteger.valueOf(2),
                new Book(
                        "title2",
                        "authorM",
                        BigDecimal.valueOf(2.2)));
        addBook(
                BigInteger.valueOf(3),
                new Book(
                        "title3",
                        "authorZ",
                        BigDecimal.valueOf(1.1)));
        addBook(
                BigInteger.valueOf(4),
                new Book(
                        "title4",
                        "authorA",
                        BigDecimal.valueOf(1.1)));
        showAllBooks(
                new Sort(
                        Sort.Direction.DESC,
                        "price")
                        .and(new Sort(
                                Sort.Direction.ASC,
                                "author")));

        addClient(
                BigInteger.valueOf(1),
                new Client("Jonny"));
        addClient(
                BigInteger.valueOf(2),
                new Client("Anna"));
        showAllClients();

    }

    private void cleanAllTables() {
        bookServiceClt.cleanAllTables()
                .thenAccept(System.out::println);
    }

    private BigInteger readId() {
        System.out.println("Enter id: ");
        try {
            return BigInteger.valueOf(Long.parseLong(scanner.next()));
        } catch (NumberFormatException ex) {
            System.out.println("Please use an integer for ID!");
        }
        return readId();
    }

    private Book readBook() {
        System.out.println("Enter title: ");
        final String title = scanner.next();
        System.out.println("Enter author: ");
        final String author = scanner.next();
        System.out.println("Enter price: ");
        final BigDecimal price = BigDecimal.valueOf(parseDouble(scanner.next()));
        return new Book(title, author, price);
    }

    private Client readClient() {
        System.out.println("Enter name: ");
        final String name = scanner.next();
        return new Client(name);
    }

    static class CloseException extends RuntimeException {
        public CloseException(String message) {
            super(message);
        }
    }

}