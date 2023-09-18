package com.tamanna.challenge.interview.calendar.services.impl;

import com.tamanna.challenge.interview.calendar.entities.AvailableMeeting;
import com.tamanna.challenge.interview.calendar.entities.AbstractPerson;
import com.tamanna.challenge.interview.calendar.entities.Interviewer;
import com.tamanna.challenge.interview.calendar.entities.Schedule;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.services.CandidateService;
import com.tamanna.challenge.interview.calendar.services.InterviewerService;
import com.tamanna.challenge.interview.calendar.services.MeetingService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tlferreira
 */
@Log4j2
@AllArgsConstructor
@Service
public class MeetingServiceImpl implements MeetingService {
    private final InterviewerService interviewerService;
    private final CandidateService candidateService;

    @Override
    public List<AvailableMeeting> queryMeeting(long candidateId, List<Long> interviewerIdList) throws ServiceException {
        log.debug("Start query meeting for candidate: {}, interviewers:{}", candidateId, listToString(interviewerIdList));
        boolean success = true;
        try {
            List<Schedule> candidateScheduleList = getCandidateSchedules(candidateId);
            List<Interviewer> interviewerList = getInterviewers(interviewerIdList);

            return getAvailableInterviewerList(candidateScheduleList, interviewerList);
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to query meeting candidate: {}, interviewers:{}, Illegal Argument, Exception: ", candidateId, listToString(interviewerIdList), e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to query meeting candidate: {}, interviewers:{}, Exception: ", candidateId, listToString(interviewerIdList), e);
            throw new ServiceException("Error creating person", e);
        } finally {
            log.debug("Finished query meeting candidate: {}, interviewers:{}, success: {}", candidateId, listToString(interviewerIdList), success);
        }
    }

    private List<AvailableMeeting> getAvailableInterviewerList(List<Schedule> candidateScheduleList, List<Interviewer> interviewerList) {
        List<AvailableMeeting> availableInterviewerList = new ArrayList<>();
        for (Schedule candidateSchedule : candidateScheduleList) {
            List<Interviewer> filteredInterviewerList = interviewerList
                    .stream()
                    .filter(interviewer -> interviewer
                            .getScheduleList()
                            .stream()
                            .anyMatch(interviewerSchedule -> interviewerSchedule.getDay().equals(candidateSchedule.getDay())
                                    && interviewerSchedule.getHour() == candidateSchedule.getHour()))
                    .toList();
            if (!filteredInterviewerList.isEmpty()) {
                availableInterviewerList.add(new AvailableMeeting(candidateSchedule, filteredInterviewerList));
            }
        }
        return availableInterviewerList;
    }

    private List<Schedule> getCandidateSchedules(long candidateId) throws ServiceException {
        return candidateService
                .findById(candidateId)
                .map(AbstractPerson::getScheduleList)
                .orElseThrow(() -> new ServiceException("Unable to find available schedules for candidate"));
    }

    private List<Interviewer> getInterviewers(List<Long> interviewerIdList) throws ServiceException {
        return  interviewerService.findAll(interviewerIdList);
    }

    private String listToString(List<?> list) {
        if (list != null) {
            return list
                    .stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
        }
        return "";
    }

}
