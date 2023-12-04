package pl.kurs.persondiary.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@MappedSuperclass
@Table(uniqueConstraints = {@UniqueConstraint(name = "UC_PERSON_PESEL",columnNames = "pesel")})
public abstract class Person implements Serializable, Identificationable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)//baza danych nie pozwoli na zapis 2 takich samych peseli
    private String pesel;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private String email;

    public Person(Long id, String firstName, String lastName, String pesel, Double height, Double weight, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.height = height;
        this.weight = weight;
        this.email = email;
    }
}
