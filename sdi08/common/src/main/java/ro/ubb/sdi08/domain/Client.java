package ro.ubb.sdi08.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

public class Client extends BaseEntity<BigInteger> implements Serializable {
    private static final long serialVersionUID = 2332504450543104587L;
    private String name;

    public Client(final BigInteger id,
                  final String name) {
        super.setId(id);
        this.name = name;
    }

    public Client() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + super.getId() + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    //    don't delete this; needed for top orders
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(getName(), client.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
