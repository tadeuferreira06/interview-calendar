package com.tamanna.challenge.interview.calendar.repositories;


import com.tamanna.challenge.interview.calendar.entities.jpa.Interviewer;
import org.springframework.stereotype.Repository;
/**
 * @author tlferreira
 */
@Repository
public interface InterviewerRepository extends PersonRepository<Interviewer> {
}
