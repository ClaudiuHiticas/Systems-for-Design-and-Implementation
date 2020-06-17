package ro.ubb.sdi05.service;

import ro.ubb.sdi05.Message;
import ro.ubb.sdi05.domain.Book;
import ro.ubb.sdi05.domain.Sort;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface BookService {

    CompletableFuture<String> addBook(BigInteger id,
                                      Book book);

    CompletableFuture<Set<Book>> getAllBooks();

    CompletableFuture<Message> getBookById(BigInteger bookId);

    CompletableFuture<String> updateBook(BigInteger bookId,
                                         Book book);

    CompletableFuture<String> deleteBook(BigInteger bookId);

    CompletableFuture<List<Book>> findAllSorted(Sort sort);
}
