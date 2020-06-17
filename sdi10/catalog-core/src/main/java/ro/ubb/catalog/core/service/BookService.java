package ro.ubb.catalog.core.service;


import ro.ubb.catalog.core.model.Book;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    List<Book> getAllBooks();

    Optional<Book> getBookById(BigInteger id);

    Book saveBook(Book book);

    Book updateBook(BigInteger id, Book book);

    void deleteById(BigInteger id);

    Set<Book> filtredBooksByTitle(String name);
}
