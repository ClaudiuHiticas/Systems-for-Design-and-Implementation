package ro.ubb.sdi08.ui;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.sdi08.domain.Book;
import ro.ubb.sdi08.domain.Client;
import ro.ubb.sdi08.domain.Sort;
import ro.ubb.sdi08.service.BookService;
import ro.ubb.sdi08.service.ClientService;
import ro.ubb.sdi08.service.OrderService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.lang.Double.parseDouble;
import static java.math.BigInteger.valueOf;

public class Console {
    private final Scanner scanner = new Scanner(System.in);
    private BookService bookService;
    private ClientService clientService;
    private OrderService orderService;

    @Autowired
    public Console(final BookService bookService,
                   final ClientService clientService,
                   final OrderService orderService) {
        this.bookService = bookService;
        this.clientService = clientService;
        this.orderService = orderService;
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
                                        addBook(readBook());
                                        break;
                                    case "2":
                                        showAllBooks();
                                        updateBook(readBook());
                                        break;
                                    case "3":
                                        showAllBooks();
                                        deleteBook(readId());
                                        break;
                                    case "4":
                                        try {
                                            findBookByID(readId());
                                        } catch (Throwable throwable) {
                                            System.out.println(throwable.getMessage());
                                        }
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
                                            bookService
                                                    .findAllSorted(sort)
                                                    .forEach(System.out::println);

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
                                        addClient(readClient());
                                        break;
                                    case "2":
                                        showAllClients();
                                        updateClient(readClient());
                                        break;
                                    case "3":
                                        showAllClients();
                                        deleteClient(readId());
                                        break;
                                    case "4":
                                        try {
                                            findClientByID(readId());
                                        } catch (Throwable throwable) {
                                            throwable.printStackTrace();
                                        }
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
                                        try {
                                            buyBook(idClient,idBook);
                                        } catch (Throwable throwable) {
                                            throwable.printStackTrace();
                                        }
                                        break;
                                    case "7":
                                        showChart();
                                        break;
                                    case "0":
                                        throw new CloseException("Bye!");
                                }
                                break;
                            case "3":
                                try {
                                    demo();
                                } catch (Throwable throwable) {
                                    System.out.println(throwable.getMessage());
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


        private void addClient(final Client client) { System.out.println(clientService.save(client));}

    private void updateClient(final Client client) {
        System.out.println(clientService.update(client));
    }

    private void deleteClient(BigInteger id) {
        System.out.println(clientService.delete(id));
    }

    private void findClientByID(final BigInteger id) throws Throwable {
        System.out.println(clientService.getById(id));
    }

    private void showAllClients() {
        clientService.getAll().forEach(System.out::println);
    }


    private void buyBook(BigInteger idClient, BigInteger idBook) throws Throwable {
        System.out.println(orderService.buyBook(idClient, idBook));
    }

    private void showChart() {
        orderService.getTopOrders().forEach(System.out::println);
    }

    private void addBook(final Book book) {
        System.out.println(bookService.save(book));
    }


    private void updateBook(final Book book) {
        System.out.println(bookService.update(book));
    }

    private void deleteBook(final BigInteger id) {
        System.out.println(bookService.delete(id));
    }


    private void findBookByID(final BigInteger id) throws Throwable {
        System.out.println(bookService.getById(id));
    }

    private void showAllBooks() {
        bookService.getAll().forEach(System.out::println);
    }

    private void showAllBooks(final Sort sort) {
        bookService.findAllSorted(sort)
                .forEach(System.out::println);
    }

    private void demo() throws Throwable {
        addBook(
                new Book(
                        valueOf(1),
                        "title1",
                        "authorD",
                        BigDecimal.valueOf(1.01)));
        addBook(
                new Book(
                        valueOf(2),
                        "title2",
                        "authorM",
                        BigDecimal.valueOf(2.02)));
        addBook(
                new Book(
                        valueOf(3),
                        "title3",
                        "authorZ",
                        BigDecimal.valueOf(1.1)));
        addBook(
                new Book(
                        valueOf(4),
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

        addClient(new Client(valueOf(1), "Jonny"));
        addClient(new Client(valueOf(2), "Anna"));
        showAllClients();
        buyBook(valueOf(1), valueOf(1));
        buyBook(valueOf(1), valueOf(2));
        showChart();
    }

    private BigInteger readId() {
        System.out.println("Enter id: ");
        try {
            return valueOf(Long.parseLong(scanner.next()));
        } catch (NumberFormatException ex) {
            System.out.println("Please use an integer for ID!");
        }
        return readId();
    }

    private Book readBook() {
        final BigInteger bookId = readId();
        System.out.println("Enter title: ");
        final String title = scanner.next();
        System.out.println("Enter author: ");
        final String author = scanner.next();
        System.out.println("Enter price: ");
        final BigDecimal price = BigDecimal.valueOf(parseDouble(scanner.next()));
        return new Book(bookId, title, author, price);
    }

    private Client readClient() {
        BigInteger id = readId();
        System.out.println("Enter name: ");
        final String name = scanner.next();
        return new Client(id, name);
    }

    static class CloseException extends RuntimeException {
        public CloseException(String message) {
            super(message);
        }
    }

}