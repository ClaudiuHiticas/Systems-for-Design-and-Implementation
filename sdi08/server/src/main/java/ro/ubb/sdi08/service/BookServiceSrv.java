package ro.ubb.sdi08.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.sdi08.domain.Book;
import ro.ubb.sdi08.domain.Sort;
import ro.ubb.sdi08.repo.db.SortingRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toSet;

public class BookServiceSrv implements BookService {

    private SortingRepository bookRepo;

    @Autowired
    public BookServiceSrv(final SortingRepository bookRepository) {
        this.bookRepo = bookRepository;
    }

    @Override
    public Set<Book> getAll() {
        final Iterable<Book> books = bookRepo.findAll();
        return StreamSupport.stream(
                books.spliterator(),
                false)
                .collect(toSet());
    }

    @Override
    public Book getById(final BigInteger bookId) throws Throwable {
        return (Book) bookRepo
                .findOne(bookId)
                .orElseThrow(() -> new RuntimeException("could not find book with id: " + bookId));
    }

    @Override
    public String save(final Book book) {
        return bookRepo.save(book).isPresent() ?
                "book with same id already in db!"
                : "book saved! " + book;
    }

    @Override
    public String update(final Book book) {
        return bookRepo.update(book).isPresent() ?
                "book with given id not found in db!"
                : "book updated!" + book;
    }

    @Override
    public String delete(final BigInteger bookId) {
        return bookRepo.delete(bookId).isPresent() ?
                "book with id " + bookId + " was removed from the db!"
                : "no book with the given id found in the db!";

    }

    @Override
    public List<Book> findAllSorted(final Sort sort) {
        final Iterable<Book> books = bookRepo.findAll(sort);
        return StreamSupport.stream(
                books.spliterator(),
                false)
                .collect(Collectors.toList());
    }
}
