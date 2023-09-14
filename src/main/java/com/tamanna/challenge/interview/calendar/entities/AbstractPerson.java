package com.tamanna.challenge.interview.calendar.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author tlferreira
 */
//I don't use @Data, because It's not recommended using with JPA Entities.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "PERSON")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="PERSON_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSON_SEQ_GEN")
    @SequenceGenerator(name = "PERSON_SEQ_GEN", sequenceName = "PERSON_SEQ", initialValue = 1, allocationSize = 10)
    private long id;
    private String firstName;
    private String lastName;
    @Column(unique=true)
    private String email;
    private String phoneNumber;
    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
