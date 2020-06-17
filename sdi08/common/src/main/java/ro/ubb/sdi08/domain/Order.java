package ro.ubb.sdi08.domain;

import java.io.Serializable;
import java.math.BigInteger;

public class Order extends BaseEntity<BigInteger> implements Serializable {
    private static final long serialVersionUID = 4528021924729730130L;
    private BigInteger clientId;
    private Book book;

    public Order() {
    }

    public Order(final BigInteger id,
                 final BigInteger clientId,
                 final Book book) {
        super.setId(id);
        this.clientId = clientId;
        this.book = book;
    }


    public BigInteger getClientId() {
        return clientId;
    }

    public void setClientId(BigInteger clientId) {
        this.clientId = clientId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "Order{" +
                "clientId=" + clientId +
                ", book=" + book +
                '}';
    }
}
