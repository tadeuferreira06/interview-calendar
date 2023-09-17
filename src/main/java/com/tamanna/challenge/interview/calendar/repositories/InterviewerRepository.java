package com.tamanna.challenge.interview.calendar.repositories;


import com.tamanna.challenge.interview.calendar.entities.Interviewer;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewerRepository extends PersonRepository<Interviewer> {
}
