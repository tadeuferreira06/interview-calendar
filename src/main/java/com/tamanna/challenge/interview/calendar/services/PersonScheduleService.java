package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.entities.AbstractPerson;
import com.tamanna.challenge.interview.calendar.entities.Schedule;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface PersonScheduleService<T extends AbstractPerson> {
    Optional<Schedule> addSchedule(T person, Schedule schedule) throws ServiceException;

    Optional<List<Schedule>> findAll(long personId) throws ServiceException;

    Optional<Schedule> findById(long personId, long scheduleId) throws ServiceException;

    Optional<Schedule> update(long personId, long scheduleId, Schedule schedule) throws ServiceException;

    Optional<Schedule> delete(long personId, long scheduleId) throws ServiceException;
}
