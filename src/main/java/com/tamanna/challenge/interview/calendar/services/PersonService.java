package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.entities.AbstractPerson;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;

public interface PersonService<T extends AbstractPerson> {
    T createPerson(T person) throws ServiceException;
}
