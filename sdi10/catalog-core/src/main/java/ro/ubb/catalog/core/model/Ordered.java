package ro.ubb.catalog.core.model;

import lombok.*;

import javax.persistence.Entity;
import java.math.BigInteger;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class Ordered extends BaseEntity<BigInteger> {
    private BigInteger bookId;
    private BigInteger clientId;
}
