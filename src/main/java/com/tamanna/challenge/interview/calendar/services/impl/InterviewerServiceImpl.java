package com.tamanna.challenge.interview.calendar.services.impl;

import com.tamanna.challenge.interview.calendar.configurations.PhoneNumberValidationKeys;
import com.tamanna.challenge.interview.calendar.entities.jpa.Interviewer;
import com.tamanna.challenge.interview.calendar.repositories.InterviewerRepository;
import com.tamanna.challenge.interview.calendar.services.InterviewerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * @author tlferreira
 */
@Log4j2
@Service
public class InterviewerServiceImpl extends AbstractPersonServiceImpl<Interviewer, InterviewerRepository> implements InterviewerService {
    public InterviewerServiceImpl(InterviewerRepository personRepository, PhoneNumberValidationKeys phoneNumberValidationKeys) {
        super(personRepository, phoneNumberValidationKeys);
    }
}
