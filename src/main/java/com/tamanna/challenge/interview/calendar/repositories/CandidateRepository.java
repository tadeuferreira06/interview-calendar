package com.tamanna.challenge.interview.calendar.repositories;


import com.tamanna.challenge.interview.calendar.entities.Candidate;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends PersonRepository<Candidate> {
}
