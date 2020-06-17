package ro.ubb.sdi05.domain;

import java.io.Serializable;
import java.math.BigInteger;

public class Order extends BaseEntity<BigInteger> implements Serializable {
    private static final long serialVersionUID = 4528021924729730130L;
    private Client client;
    private Book book;

    public Order() {
    }

    public Order(final Client client,
                 final Book book) {
        this.client = client;
        this.book = book;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
                "client=" + client +
                ", book=" + book +
                '}';
    }
}
