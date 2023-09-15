package com.tamanna.challenge.interview.calendar.repositories;


import com.tamanna.challenge.interview.calendar.entities.Person;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAllByPersonType(PersonType personType, Pageable pageable);

    List<Person> findAllByPersonType(PersonType personType);

    Optional<Person> findByIdAndPersonType(long id, PersonType personType);

    Optional<Person> findByEmail(String email);

    Optional<Person> findByPhoneNumber(String phoneNumber);
}
