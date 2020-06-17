package ro.ubb.sdi08.service;

import ro.ubb.sdi08.domain.Book;
import ro.ubb.sdi08.domain.Sort;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public interface BookService {

    Set<Book> getAll();

    Book getById(final BigInteger bookId) throws Throwable;

    String save(final Book book);

    String update(final Book book);

    String delete(final BigInteger bookId);

    List<Book> findAllSorted(final Sort sort);
}
