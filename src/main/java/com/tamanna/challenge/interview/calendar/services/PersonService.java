package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.entities.Person;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    Person createPerson(Person person) throws ServiceException;

    List<Person> findAllPageable(PersonType personType, int page, int size) throws ServiceException;

    List<Person> findAll(PersonType personType) throws ServiceException;

    Optional<Person> findById(PersonType personType, long id) throws ServiceException;

    Optional<Person> update(long id, Person person) throws ServiceException;

    Optional<Person> delete(PersonType personType, long id) throws ServiceException;
}
