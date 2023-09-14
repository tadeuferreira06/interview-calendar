package com.tamanna.challenge.interview.calendar.repositories;


import com.tamanna.challenge.interview.calendar.entities.AbstractPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository <T extends AbstractPerson> extends JpaRepository<T, UUID> {

    Optional<T> findByEmail(String email);
}
