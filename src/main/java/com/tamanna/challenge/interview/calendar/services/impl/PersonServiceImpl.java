package com.tamanna.challenge.interview.calendar.services.impl;

import com.tamanna.challenge.interview.calendar.entities.AbstractPerson;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.repositories.PersonRepository;
import com.tamanna.challenge.interview.calendar.services.PersonService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * @author tlferreira
 */
@Log4j2
@Service
@AllArgsConstructor
public class PersonServiceImpl<T extends AbstractPerson> implements PersonService<T> {
    private final PersonRepository<T> personRepository;

    @Override
    public T createPerson(T person) throws ServiceException {
        try {
            //check for email uniqueness
            if(personRepository.findByEmail(person.getEmail()).isPresent()){
                throw new IllegalArgumentException("Email address already in use");
            }
            return personRepository.save(person);
        } catch (IllegalArgumentException e) {
            log.error("Unable to create person {}, Illegal Argument, Exception: ", person, e);
            throw e;
        } catch (Exception e) {
            log.error("Unable to create person {}, Exception: ", person, e);
            throw new ServiceException("Error creating person", e);
        }
    }
}
