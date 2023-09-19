package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.entities.jpa.AbstractPerson;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

/**
 * @author tlferreira
 */
public interface PersonService<T extends AbstractPerson> {
    T createPerson(T person) throws ServiceException;

    List<T> findAllPageable(int page, int size) throws ServiceException;

    List<T> findAll() throws ServiceException;

    List<T> findAll(List<Long> ids) throws ServiceException;

    Optional<T> findById(long id) throws ServiceException;

    Optional<T> update(long id, T person) throws ServiceException;

    Optional<T> delete(long id) throws ServiceException;
}
