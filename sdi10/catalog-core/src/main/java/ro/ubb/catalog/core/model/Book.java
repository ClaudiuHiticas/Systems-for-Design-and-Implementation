package ro.ubb.catalog.core.model;

import lombok.*;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class Book extends BaseEntity<BigInteger>{
    private String title;
    private String author;
    private BigDecimal price;
}
