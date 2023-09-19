package com.tamanna.challenge.interview.calendar.repositories;


import com.tamanna.challenge.interview.calendar.entities.jpa.Candidate;
import org.springframework.stereotype.Repository;
/**
 * @author tlferreira
 */
@Repository
public interface CandidateRepository extends PersonRepository<Candidate> {
}
