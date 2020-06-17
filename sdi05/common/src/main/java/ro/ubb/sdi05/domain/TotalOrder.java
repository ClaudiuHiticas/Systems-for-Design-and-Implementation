package ro.ubb.sdi05.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class TotalOrder implements Serializable {
    private static final long serialVersionUID = -2460402598678060799L;
    private Client client;
    private BigDecimal totalSpent;

    public TotalOrder(final Client client,
                      final BigDecimal totalSpent) {
        this.client = client;
        this.totalSpent = totalSpent;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    @Override
    public String toString() {
        return "TotalOrder{" +
                "client=" + client +
                ", totalSpent=" + totalSpent +
                '}';
    }
}
