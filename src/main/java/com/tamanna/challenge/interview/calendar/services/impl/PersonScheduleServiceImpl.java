package com.tamanna.challenge.interview.calendar.services.impl;

import com.tamanna.challenge.interview.calendar.entities.Person;
import com.tamanna.challenge.interview.calendar.entities.Schedule;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.repositories.PersonRepository;
import com.tamanna.challenge.interview.calendar.repositories.ScheduleRepository;
import com.tamanna.challenge.interview.calendar.services.PersonScheduleService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author tlferreira
 */
@Log4j2
@Service
@AllArgsConstructor
public class PersonScheduleServiceImpl implements PersonScheduleService {
    private final PersonRepository personRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public Optional<Schedule> addSchedule(PersonType personType, long id, Schedule schedule) throws ServiceException {
        log.debug("Start addSchedule {} to Person PersonType: {}, Id: {}", schedule, personType, id);
        boolean success = true;
        try {
            //getPerson
            Optional<Person> personOpt = personRepository.findByIdAndPersonType(id, personType);
            if (personOpt.isEmpty()) {
                log.warn("Unable to find Person PersonType: {}, Id: {} ", personType, id);
                return Optional.empty();
            }
            Person person = personOpt.get();

            validateScheduleUniqueness(id, schedule);

            if (person.getAvailableSchedules() == null) {
                person.setAvailableSchedules(new ArrayList<>());
            }

            schedule.setPerson(person);

            return Optional.of(scheduleRepository.save(schedule));
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to addSchedule {} to Person PersonType: {}, Id: {}, Illegal Argument, Exception: ", schedule, personType, id, e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to addSchedule {} to Person PersonType: {}, Id: {}, Exception: ", schedule, personType, id, e);
            throw new ServiceException("Error addSchedule", e);
        } finally {
            log.debug("Finished addSchedule {} to Person PersonType: {}, Id: {}, success: {}", schedule, personType, id, success);
        }
    }

    @Override
    public Optional<Schedule> findById(PersonType personType, long id, long scheduleId) throws ServiceException {
        log.debug("Start getSchedule ScheduleId: {} from Person PersonType: {}, Id: {}", scheduleId, personType, id);
        boolean success = true;
        try {
            return scheduleRepository.findByIdAndPersonIdAndPersonType(scheduleId, id, personType);
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to getSchedule ScheduleId: {} from Person PersonType: {}, Id: {}, Illegal Argument, Exception: ", scheduleId, personType, id, e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to getSchedule ScheduleId: {} from Person PersonType: {}, Id: {}, Exception: ", scheduleId, personType, id, e);
            throw new ServiceException("Error getSchedule", e);
        } finally {
            log.debug("Finished getSchedule ScheduleId: {} from Person PersonType: {}, Id: {}, success: {}", scheduleId, personType, id, success);
        }
    }

    @Override
    public Optional<Schedule> update(PersonType personType, long id, long scheduleId, Schedule schedule) throws ServiceException {
        log.debug("Start update Schedule ScheduleId: {}, Schedule:{} from Person PersonType: {}, Id: {}", scheduleId, schedule, personType, id);
        boolean success = true;
        try {
            Optional<Schedule> scheduleOpt = scheduleRepository.findByIdAndPersonIdAndPersonType(scheduleId, id, personType);
            if (scheduleOpt.isPresent()) {
                schedule.setId(scheduleId);
                validateScheduleUniqueness(id, schedule);
                scheduleOpt = Optional.of(this.scheduleRepository.save(schedule));
            }
            return scheduleOpt;
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to updateSchedule ScheduleId: {}, Schedule:{} from Person PersonType: {}, Id: {}, Illegal Argument, Exception: ",  scheduleId, schedule, personType, id, e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to updateSchedule ScheduleId: {}, Schedule:{} from Person PersonType: {}, Id: {}, Exception: ", scheduleId, schedule, personType, id, e);
            throw new ServiceException("Error updateSchedule", e);
        } finally {
            log.debug("Finished updateSchedule ScheduleId: {}, Schedule:{} from Person PersonType: {}, Id: {}, success: {}", scheduleId, schedule, personType, id, success);
        }
    }

    @Override
    public Optional<Schedule> delete(PersonType personType, long id, long scheduleId) throws ServiceException {
        log.debug("Start update Schedule ScheduleId: {} from Person PersonType: {}, Id: {}", scheduleId, personType, id);
        boolean success = true;
        try {
            Optional<Schedule> scheduleOpt = scheduleRepository.findByIdAndPersonIdAndPersonType(scheduleId, id, personType);
            if (scheduleOpt.isPresent()) {
                this.scheduleRepository.deleteById(scheduleId);
            }
            return scheduleOpt;
        } catch (Exception e) {
            success = false;
            log.error("Unable to deleteSchedule ScheduleId: {} from Person PersonType: {}, Id: {}, Exception: ", scheduleId, personType, id, e);
            throw new ServiceException("Error updateSchedule", e);
        } finally {
            log.debug("Finished deleteSchedule ScheduleId: {} from Person PersonType: {}, Id: {}, success: {}", scheduleId, personType, id, success);
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
