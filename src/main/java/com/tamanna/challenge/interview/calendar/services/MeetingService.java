package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.entities.AvailableMeeting;
import com.tamanna.challenge.interview.calendar.entities.jpa.Booking;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

/**
 * @author tlferreira
 */
public interface MeetingService {
    List<AvailableMeeting> queryMeeting(long candidateId, List<Long> interviewerIdList) throws ServiceException;

    Booking bookMeeting(long scheduleId, long candidateId, List<Long> interviewerIdList) throws ServiceException;

    Optional<Booking> getMeeting(long id) throws ServiceException;

    Optional<Booking> cancelMeeting(long id) throws ServiceException;

    List<Booking> getCandidateMeetings(long candidateId) throws ServiceException;

    List<Booking> getInterviewerMeetings(long interviewerId) throws ServiceException;
}
