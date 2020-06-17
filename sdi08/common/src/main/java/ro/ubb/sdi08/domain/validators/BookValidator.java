package ro.ubb.sdi08.domain.validators;

import ro.ubb.sdi08.domain.Book;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class BookValidator implements Validator<Book> {
    private final static Predicate<Book> okBookTitle = (b) -> b.getTitle().length() > 0 &&
            b.getTitle().length() <= 255 &&
            !Objects.equals(b.getTitle(), "N/A");

    @Override
    public void validate(final Book book) throws ValidatorException {
        Optional.ofNullable(book)
                .orElseThrow(() -> new IllegalArgumentException("Given book was null!"));
        Optional.of(book)
                .filter(okBookTitle)
                .orElseThrow(() -> new ValidatorException("Book title is not valid!"));
    }
}
