package com.tamanna.challenge.interview.calendar.services.impl;

import com.tamanna.challenge.interview.calendar.entities.AbstractPerson;
import com.tamanna.challenge.interview.calendar.entities.Schedule;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.repositories.PersonRepository;
import com.tamanna.challenge.interview.calendar.repositories.ScheduleRepository;
import com.tamanna.challenge.interview.calendar.services.PersonScheduleService;
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
public abstract class AbstractPersonScheduleServiceImpl<T extends AbstractPerson> implements PersonScheduleService<T> {
    private final ScheduleRepository scheduleRepository;
    private final PersonRepository<T> personRepository;
    private final PersonType personType;

    @Override
    public Optional<Schedule> addSchedule(T person, Schedule schedule) throws ServiceException {
        log.debug("Start addSchedule {} to Person PersonType: {}, Person: {}", schedule, personType, person);
        boolean success = true;
        try {
            validateScheduleUniqueness(person.getId(), schedule);

            if (person.getScheduleList() == null) {
                person.setScheduleList(new ArrayList<>());
            }

            schedule.setPerson(person);

            return Optional.of(scheduleRepository.save(schedule));
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to addSchedule {} to Person PersonType: {}, Person: {}, Illegal Argument, Exception: ", schedule, personType, person, e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to addSchedule {} to Person PersonType: {}, Person: {}, Exception: ", schedule, personType, person, e);
            throw new ServiceException("Error addSchedule", e);
        } finally {
            log.debug("Finished addSchedule {} to Person PersonType: {}, Person: {}, success: {}", schedule, personType, person, success);
        }
    }

    @Override
    public Optional<List<Schedule>> findAll(long personId) throws ServiceException {
        log.debug("Start getSchedules All from Person PersonType: {}, Id: {}", personType, personId);
        boolean success = true;
        try {
            Optional<T> personOpt = personRepository.findById(personId);
            if(personOpt.isPresent()){
                T person = personOpt.get();
                return Optional.of(person.getScheduleList() == null ? new ArrayList<>() : person.getScheduleList());
            }
            success = false;
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to getSchedules All from Person PersonType: {}, Id: {}, Illegal Argument, Exception: ", personType, personId, e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to getSchedules All from Person PersonType: {}, Id: {}, Exception: ", personType, personId, e);
            throw new ServiceException("Error getSchedule", e);
        } finally {
            log.debug("Finished getSchedules All from Person PersonType: {}, Id: {}, success: {}", personType, personId, success);
        }
    }

    @Override
    public Optional<Schedule> findById(long personId, long scheduleId) throws ServiceException {
        log.debug("Start getSchedule ScheduleId: {} from Person PersonType: {}, Id: {}", scheduleId, personType, personId);
        boolean success = true;
        try {
            return scheduleRepository.findByIdAndPersonIdAndPersonType(scheduleId, personId, personType);
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to getSchedule ScheduleId: {} from Person PersonType: {}, Id: {}, Illegal Argument, Exception: ", scheduleId, personType, personId, e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to getSchedule ScheduleId: {} from Person PersonType: {}, Id: {}, Exception: ", scheduleId, personType, personId, e);
            throw new ServiceException("Error getSchedule", e);
        } finally {
            log.debug("Finished getSchedule ScheduleId: {} from Person PersonType: {}, Id: {}, success: {}", scheduleId, personType, personId, success);
        }
    }

    @Override
    public Optional<Schedule> update(long personId, long scheduleId, Schedule schedule) throws ServiceException {
        log.debug("Start updateSchedule ScheduleId: {}, Schedule:{} from Person PersonType: {}, Id: {}", scheduleId, schedule, personType, personId);
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
            log.error("Unable to updateSchedule ScheduleId: {}, Schedule:{} from Person PersonType: {}, Id: {}, Illegal Argument, Exception: ",  scheduleId, schedule, personType, personId, e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to updateSchedule ScheduleId: {}, Schedule:{} from Person PersonType: {}, Id: {}, Exception: ", scheduleId, schedule, personType, personId, e);
            throw new ServiceException("Error updateSchedule", e);
        } finally {
            log.debug("Finished updateSchedule ScheduleId: {}, Schedule:{} from Person PersonType: {}, Id: {}, success: {}", scheduleId, schedule, personType, personId, success);
        }
    }

    @Override
    public Optional<Schedule> delete(long personId, long scheduleId) throws ServiceException {
        log.debug("Start deleteSchedule ScheduleId: {} from Person PersonType: {}, Id: {}", scheduleId, personType, personId);
        boolean success = true;
        try {
            Optional<Schedule> scheduleOpt = scheduleRepository.findByIdAndPersonIdAndPersonType(scheduleId, personId, personType);
            if (scheduleOpt.isPresent()) {
                this.scheduleRepository.deleteById(scheduleId);
            }
            return scheduleOpt;
        } catch (Exception e) {
            success = false;
            log.error("Unable to deleteSchedule ScheduleId: {} from Person PersonType: {}, Id: {}, Exception: ", scheduleId, personType, personId, e);
            throw new ServiceException("Error updateSchedule", e);
        } finally {
            log.debug("Finished deleteSchedule ScheduleId: {} from Person PersonType: {}, Id: {}, success: {}", scheduleId, personType, personId, success);
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
