package ro.ubb.sdi05.service;

import ro.ubb.sdi05.domain.Book;
import ro.ubb.sdi05.domain.Sort;
import ro.ubb.sdi05.domain.validators.ValidatorException;
import ro.ubb.sdi05.repo.db.RepoBookDb;
import ro.ubb.sdi05.repo.db.SortingRepository;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class BookServiceSrv {
    private SortingRepository<BigInteger, Book> repoBook;

    public BookServiceSrv() {
        this.repoBook = new RepoBookDb();
    }

    public Optional<Book> addBook(final BigInteger id,
                                  final Book book) throws ValidatorException {
        book.setId(id);
        return repoBook.save(book);
    }

    public Set<Book> getAllBooks() {
        Iterable<Book> books = repoBook.findAll();
        return StreamSupport.stream(books.spliterator(), false).collect(Collectors.toSet());
    }

    /*
        filteredBooksByTitle return all books which contains the title from parameter
     */
    public Set<Book> filteredBooksByTitle(String title) {
        Iterable<Book> books = repoBook.findAll();
        Set<Book> filteredBooks = new HashSet<>();
        books.forEach(filteredBooks::add);
        filteredBooks.removeIf(book -> !book.getTitle().contains(title));
        return filteredBooks;
    }

    public Optional<Book> getBookById(BigInteger bookId) {
        return repoBook.findOne(bookId);
    }

    public Optional<Book> updateBook(final BigInteger bookId,
                                     final Book book) {
        book.setId(bookId);
        return repoBook.update(book);
    }

    public Optional<Book> deleteBook(final BigInteger bookId) {
        return repoBook.delete(bookId);
    }

    public List<Book> findAllSorted(final Sort sort) {
        return StreamSupport
                .stream(
                        repoBook
                                .findAll(sort)
                                .spliterator(),
                        false)
                .collect(toList());
    }
}
