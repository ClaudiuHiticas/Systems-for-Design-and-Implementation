package ro.ubb.sdi08.domain;

import java.io.Serializable;

public class BaseEntity<ID> implements Serializable {
    private static final long serialVersionUID = -8510993353141909892L;
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}
