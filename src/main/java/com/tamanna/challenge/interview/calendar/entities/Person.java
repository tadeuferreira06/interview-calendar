package com.tamanna.challenge.interview.calendar.entities;

import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="personType",
        discriminatorType = DiscriminatorType.STRING)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false)
    private PersonType personType;

    private String firstName;

    private String lastName;

    @Column(unique=true)
    private String email;

    @Column(unique=true)
    private String phoneNumber;

    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Schedule> availableSchedules;


}
