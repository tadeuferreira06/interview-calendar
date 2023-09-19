package com.tamanna.challenge.interview.calendar.services.impl;

import com.tamanna.challenge.interview.calendar.entities.AbstractPerson;
import com.tamanna.challenge.interview.calendar.entities.Schedule;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.exceptions.NotFoundException;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.repositories.ScheduleRepository;
import com.tamanna.challenge.interview.calendar.services.PersonScheduleService;
import com.tamanna.challenge.interview.calendar.services.PersonService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author tlferreira
 */
@Log4j2
@AllArgsConstructor
public abstract class AbstractPersonScheduleServiceImpl<T extends AbstractPerson> implements PersonScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PersonService<T> personService;
    private final PersonType personType;

    @Override
    public Schedule addSchedule(long personId, Schedule schedule) throws ServiceException {
        log.debug("Start addSchedule {}", personType);
        boolean success = true;
        try {
            T person = getPerson(personId);

            validateScheduleUniqueness(person.getId(), schedule);

            if (person.getScheduleList() == null) {
                person.setScheduleList(new ArrayList<>());
            }

            schedule.setPerson(person);

            return scheduleRepository.save(schedule);
        } catch (NotFoundException | IllegalArgumentException | ServiceException e) {
            success = false;
            log.error("Unable to addSchedule {}, Exception: ", personType, e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to addSchedule {}, Exception: ", personType, e);
            throw new ServiceException("Error addSchedule", e);
        } finally {
            log.debug("Finished addSchedule {}, success: {}", personType, success);
        }
    }

    @Override
    public List<Schedule> findAll(long personId) throws ServiceException {
        log.debug("Start getSchedules All {}", personType);
        boolean success = true;
        try {
            T person = getPerson(personId);
            return person.getScheduleList() == null ? new ArrayList<>() : person.getScheduleList();
        } catch (NotFoundException | IllegalArgumentException | ServiceException e) {
            success = false;
            log.error("Unable to getSchedules All {}, Illegal Argument, Exception: ", personType, e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to getSchedules All {}, Exception: ", personType, e);
            throw new ServiceException("Error getSchedule", e);
        } finally {
            log.debug("Finished getSchedules All {}, success: {}", personType, success);
        }
    }

    private T getPerson(long personId) throws ServiceException {
        return personService
                .findById(personId)
                .orElseThrow(()
                        -> new NotFoundException(String.format("Unable to find %s with Id %s", personType, personId)));
    }

    @Override
    public Optional<Schedule> findById(long personId, long scheduleId) throws ServiceException {
        log.debug("Start getSchedule {}", personType);
        boolean success = true;
        try {
            return scheduleRepository.findByIdAndPersonIdAndPersonType(scheduleId, personId, personType);
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to getSchedule {}, Illegal Argument, Exception: ", personType, e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to getSchedule {}, Exception: ", personType, e);
            throw new ServiceException("Error getSchedule", e);
        } finally {
            log.debug("Finished getSchedule {}, success: {}", personType, success);
        }
    }

    @Override
    public Optional<Schedule> update(long personId, long scheduleId, Schedule schedule) throws ServiceException {
        log.debug("Start updateSchedule {}", personType);
        boolean success = true;
        try {
            Optional<Schedule> scheduleOpt = scheduleRepository.findByIdAndPersonIdAndPersonType(scheduleId, personId, personType);
            if (scheduleOpt.isPresent()) {
                schedule.setId(scheduleId);
                validateScheduleUniqueness(personId, schedule);
                scheduleOpt = Optional.of(this.scheduleRepository.save(schedule));
            }
            return scheduleOpt;
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to updateSchedule {}, Illegal Argument, Exception: ", personType, e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to updateSchedule {}, Exception: ", personType, e);
            throw new ServiceException("Error updateSchedule", e);
        } finally {
            log.debug("Finished updateSchedule {}, success: {}", personType, success);
        }
    }

    @Override
    public Optional<Schedule> delete(long personId, long scheduleId) throws ServiceException {
        log.debug("Start deleteSchedule {}", personType);
        boolean success = true;
        try {
            Optional<Schedule> scheduleOpt = scheduleRepository.findByIdAndPersonIdAndPersonType(scheduleId, personId, personType);
            if (scheduleOpt.isPresent()) {
                this.scheduleRepository.deleteById(scheduleId);
            }
            return scheduleOpt;
        } catch (Exception e) {
            success = false;
            log.error("Unable to deleteSchedule {}, Exception: ", personType, e);
            throw new ServiceException("Error updateSchedule", e);
        } finally {
            log.debug("Finished deleteSchedule {}, success: {}", personType, success);
        }
    }

    private void validateScheduleUniqueness(long personId, Schedule schedule) {
        Optional<Schedule> existingScheduleOpt = scheduleRepository.findByPersonIdAndDayAndHour(personId, schedule.getDay(), schedule.getHour());

        boolean invalid = existingScheduleOpt.isPresent();
        if (schedule.getId() > 0) {
            invalid = existingScheduleOpt
                    .map(Schedule::getId)
                    .filter(id -> schedule.getId() != id)
                    .isPresent();
        }

        if (invalid) {
            throw new IllegalArgumentException("Schedule already exists");
        }
    }
}
