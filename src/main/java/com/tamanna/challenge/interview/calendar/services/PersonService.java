package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.entities.Person;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface PersonService<T extends Person> {
    T createPerson(T person) throws ServiceException;

    List<T> findAllPageable(int page, int size) throws ServiceException;

    List<T> findAll() throws ServiceException;

    Optional<T> findById(long id) throws ServiceException;

    Optional<T> update(long id, T person) throws ServiceException;

    Optional<T> delete(long id) throws ServiceException;
}
