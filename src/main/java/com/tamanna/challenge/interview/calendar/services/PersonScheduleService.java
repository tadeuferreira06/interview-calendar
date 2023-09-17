package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.entities.Person;
import com.tamanna.challenge.interview.calendar.entities.Schedule;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;

import java.util.Optional;

public interface PersonScheduleService<T extends Person> {
    Optional<Schedule> addSchedule(T person, Schedule schedule) throws ServiceException;

    Optional<Schedule> findById(long personId, long scheduleId) throws ServiceException;

    Optional<Schedule> update(long personId, long scheduleId, Schedule schedule) throws ServiceException;

    Optional<Schedule> delete(long personId, long scheduleId) throws ServiceException;
}
