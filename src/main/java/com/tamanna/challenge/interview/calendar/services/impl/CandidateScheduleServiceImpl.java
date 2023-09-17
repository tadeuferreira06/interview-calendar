package com.tamanna.challenge.interview.calendar.services.impl;

import com.tamanna.challenge.interview.calendar.entities.Candidate;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.repositories.ScheduleRepository;
import com.tamanna.challenge.interview.calendar.services.CandidateScheduleService;
import com.tamanna.challenge.interview.calendar.services.CandidateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * @author tlferreira
 */
@Log4j2
@Service
public class CandidateScheduleServiceImpl extends AbstractPersonScheduleServiceImpl<Candidate> implements CandidateScheduleService {
    public CandidateScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        super(scheduleRepository, PersonType.CANDIDATE);
    }
}
