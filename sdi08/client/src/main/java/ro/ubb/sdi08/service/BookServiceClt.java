package ro.ubb.sdi08.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.sdi08.domain.Book;
import ro.ubb.sdi08.domain.Sort;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public class BookServiceClt implements BookService {
    @Autowired
    private BookService bookService;

    @Override
    public Set<Book> getAll() {
        return bookService.getAll();
    }

    @Override
    public Book getById(final BigInteger bookId) throws Throwable {
        return bookService.getById(bookId);
    }

    @Override
    public String save(final Book book) {
        return bookService.save(book);
    }

    @Override
    public String update(final Book book) {
        return bookService.update(book);
    }

    @Override
    public String delete(final BigInteger bookId) {
        return bookService.delete(bookId);
    }

    @Override
    public List<Book> findAllSorted(final Sort sort) {
        return bookService.findAllSorted(sort);
    }
}
