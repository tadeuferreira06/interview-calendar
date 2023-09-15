package com.tamanna.challenge.interview.calendar;

import com.tamanna.challenge.interview.calendar.entities.Person;
import com.tamanna.challenge.interview.calendar.entities.Schedule;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;

import java.time.LocalDate;

/**
 * @author tlferreira
 */
public class DummyDataUtils {
    private DummyDataUtils() {
        //private
    }

    public static Person getNewPersonInterviewer() {
        return getNewPersonInterviewer(null);
    }

    public static Person getNewPersonInterviewer(Long id) {
        Person person = new Person();

        if (id != null) {
            person.setId(id);
        }

        person.setFirstName("Albino");
        person.setLastName("Silva");
        person.setEmail("as@mail.com");
        person.setPhoneNumber("+351-244-000-000");
        person.setPersonType(PersonType.INTERVIEWER);

        return person;
    }

    public static Person getNewPersonCandidate() {
        return getNewPersonCandidate(null);
    }

    public static Person getNewPersonCandidate(Long id) {
        Person person = new Person();

        if (id != null) {
            person.setId(id);
        }

        person.setFirstName("José");
        person.setLastName("Almeida");
        person.setEmail("ja@mail.com");
        person.setPhoneNumber("+351-244-000-001");
        person.setPersonType(PersonType.CANDIDATE);
        return person;
    }

    public static Schedule getNewSchedule() {
        return getNewSchedule(null, null, null);
    }

    public static Schedule getNewSchedule(Long id) {
        return getNewSchedule(id, null, null);
    }

    public static Schedule getNewSchedule(Long id, Integer hour) {
        return getNewSchedule(id, hour, null);
    }

    public static Schedule getNewSchedule(Long id, Integer hour, LocalDate date) {
        Schedule schedule = new Schedule();

        if (id != null) {
            schedule.setId(id);
        }

        if (hour == null) {
            hour = 12;
        }
        schedule.setHour(hour);

        if (date == null) {
            date = LocalDate.of(2023, 9, 15);
        }
        schedule.setDay(date);

        return schedule;
    }
}
