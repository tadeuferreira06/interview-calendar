package com.tamanna.challenge.interview.calendar.repositories;


import com.tamanna.challenge.interview.calendar.entities.jpa.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
/**
 * @author tlferreira
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.person.id = :personId AND s.day = :day and s.hour = :hour")
    Optional<Schedule> findByPersonIdAndDayAndHour(long personId, LocalDate day, int hour);

    @Query("SELECT s FROM Schedule s WHERE s.id = :id AND s.person.id = :personId AND s.person.class = :personType")
    Optional<Schedule> findByIdAndPersonIdAndPersonType(long id, long personId, String personType);
}
