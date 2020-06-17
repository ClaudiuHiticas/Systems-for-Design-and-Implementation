package ro.ubb.sdi08.domain.validators;

public class BookstoreException extends RuntimeException {

    public BookstoreException(String message) {
        super(message);
    }

    public BookstoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookstoreException(Throwable cause) {
        super(cause);
    }
}
