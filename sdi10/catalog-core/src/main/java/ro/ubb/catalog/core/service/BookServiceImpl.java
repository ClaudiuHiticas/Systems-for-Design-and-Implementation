package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Book;
import ro.ubb.catalog.core.repository.BookRepository;


import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {
    public static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        log.trace("getAllBooks --- method entered");
        List<Book> result = bookRepository.findAll();
        log.trace("getAllBooks: result={}", result);
        return result;
    }

    @Override
    public Optional<Book> getBookById(BigInteger id) {
        log.trace("getBookById --- method entered");
        Optional<Book> result =  bookRepository.findById(id);
        log.trace("getBookById: result={}", result);
        return  result;
    }

    @Override
    public Book saveBook(Book book) {
        log.trace("saveBook --- method entered");
        Book result =  bookRepository.save(book);
        log.trace("saveBook: result={}", result);
        return  result;
    }

    @Override
    public void deleteById(BigInteger id) {
        log.trace("deleteById --- method entered");
        bookRepository.deleteById(id);

    }

    @Override
    public Set<Book> filtredBooksByTitle(String title) {
        log.trace("filteredBooksByTitle - method entered");
        Iterable<Book> books = bookRepository.findAll();
        Set<Book> filteredBooks = new HashSet<>();
        books.forEach(filteredBooks::add);
        filteredBooks.removeIf(book -> !book.getTitle().contains(title));
        return filteredBooks;
    }

    @Override
    @Transactional
    public Book updateBook(BigInteger id, Book book) {
        log.trace("updateBook --- method entered");
        Book update = bookRepository.findById(id).orElse(book);
        update.setTitle(book.getTitle());
        update.setAuthor(book.getAuthor());
        update.setPrice(book.getPrice());
        log.trace("updateBook: result={}", update);
        return update;
    }

}
