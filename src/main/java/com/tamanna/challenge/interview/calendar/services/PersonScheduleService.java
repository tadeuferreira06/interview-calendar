package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.entities.Schedule;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface PersonScheduleService {
    List<Schedule> addSchedule(PersonType personType, long id, Schedule schedule) throws ServiceException;

    Optional<Schedule> findById(PersonType personType, long id, long scheduleId) throws ServiceException;

    Optional<Schedule> update(PersonType personType, long id, long scheduleId, Schedule schedule) throws ServiceException;

    Optional<Schedule> delete(PersonType personType, long id, long scheduleId) throws ServiceException;
}
