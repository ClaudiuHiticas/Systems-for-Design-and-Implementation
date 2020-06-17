package ro.ubb.sdi08.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TotalOrder implements Serializable {
    private static final long serialVersionUID = -2460402598678060799L;
    private BigInteger clientId;
    private BigDecimal totalSpent;

    public TotalOrder(final BigInteger clientId,
                      final BigDecimal totalSpent) {
        this.clientId = clientId;
        this.totalSpent = totalSpent;
    }

    public BigInteger getClientId() {
        return clientId;
    }

    public void setClientId(BigInteger clientId) {
        this.clientId = clientId;
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
                "clientId=" + clientId +
                ", totalSpent=" + totalSpent +
                '}';
    }
}
