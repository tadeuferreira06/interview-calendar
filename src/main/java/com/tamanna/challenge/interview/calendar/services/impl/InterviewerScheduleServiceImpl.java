package com.tamanna.challenge.interview.calendar.services.impl;

import com.tamanna.challenge.interview.calendar.entities.Interviewer;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.repositories.InterviewerRepository;
import com.tamanna.challenge.interview.calendar.repositories.ScheduleRepository;
import com.tamanna.challenge.interview.calendar.services.InterviewerScheduleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * @author tlferreira
 */
@Log4j2
@Service("InterviewerScheduleService")
public class InterviewerScheduleServiceImpl extends AbstractPersonScheduleServiceImpl<Interviewer> implements InterviewerScheduleService {
    public InterviewerScheduleServiceImpl(ScheduleRepository scheduleRepository, InterviewerRepository interviewerRepository) {
        super(scheduleRepository, interviewerRepository, PersonType.INTERVIEWER);
    }
}
