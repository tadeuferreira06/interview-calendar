package com.tamanna.challenge.interview.calendar.services.impl;

import com.tamanna.challenge.interview.calendar.configurations.PhoneNumberValidationKeys;
import com.tamanna.challenge.interview.calendar.entities.jpa.Candidate;
import com.tamanna.challenge.interview.calendar.repositories.CandidateRepository;
import com.tamanna.challenge.interview.calendar.services.CandidateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * @author tlferreira
 */
@Log4j2
@Service
public class CandidateServiceImpl extends AbstractPersonServiceImpl<Candidate, CandidateRepository> implements CandidateService {
    public CandidateServiceImpl(CandidateRepository personRepository, PhoneNumberValidationKeys phoneNumberValidationKeys) {
        super(personRepository, phoneNumberValidationKeys);
    }
}
