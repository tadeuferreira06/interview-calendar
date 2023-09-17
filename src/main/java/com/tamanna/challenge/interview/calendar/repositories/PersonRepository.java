package com.tamanna.challenge.interview.calendar.repositories;


import com.tamanna.challenge.interview.calendar.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PersonRepository<T extends Person> extends JpaRepository<T, Long> {
    Optional<T> findByEmail(String email);

    Optional<T> findByPhoneNumber(String phoneNumber);
}
