package com.tamanna.challenge.interview.calendar.repositories;


import com.tamanna.challenge.interview.calendar.entities.jpa.AbstractPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author tlferreira
 */
public interface PersonRepository<T extends AbstractPerson> extends JpaRepository<T, Long> {
    Optional<T> findByEmail(String email);

    Optional<T> findByPhoneNumber(String phoneNumber);
}
