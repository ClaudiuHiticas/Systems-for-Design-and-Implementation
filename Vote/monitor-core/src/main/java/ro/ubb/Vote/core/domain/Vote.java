package ro.ubb.Vote.core.domain;

//import lombok.*;
import javax.persistence.*;


@Entity
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

}
