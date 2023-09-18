package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.entities.AvailableMeeting;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;

import java.util.List;

public interface MeetingService {
    List<AvailableMeeting> queryMeeting(long candidateId, List<Long> interviewerIdList) throws ServiceException;
}
