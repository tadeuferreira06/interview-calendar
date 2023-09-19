package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.entities.jpa.Schedule;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

/**
 * @author tlferreira
 */
public interface PersonScheduleService {
    Schedule addSchedule(long personId, Schedule schedule) throws ServiceException;

    List<Schedule> findAll(long personId) throws ServiceException;

    Optional<Schedule> findById(long personId, long scheduleId) throws ServiceException;

    Optional<Schedule> update(long personId, long scheduleId, Schedule schedule) throws ServiceException;

    Optional<Schedule> delete(long personId, long scheduleId) throws ServiceException;
}
