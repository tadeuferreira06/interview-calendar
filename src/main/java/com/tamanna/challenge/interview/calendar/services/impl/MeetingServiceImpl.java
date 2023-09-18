package com.tamanna.challenge.interview.calendar.services.impl;

import com.tamanna.challenge.interview.calendar.entities.*;
import com.tamanna.challenge.interview.calendar.exceptions.NotFoundException;
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
import java.util.Optional;
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
        } catch (NotFoundException | IllegalArgumentException e) {
            success = false;
            log.error("Unable to query meeting candidate: {}, interviewers:{}, Exception: ", candidateId, listToString(interviewerIdList), e);
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
        Candidate candidate = candidateService
                .findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Unable to find candidate"));

        return Optional
                .of(candidate)
                .filter(c -> c.getScheduleList() != null && !c.getScheduleList().isEmpty())
                .map(AbstractPerson::getScheduleList)
                .orElseThrow(() -> new ServiceException("Candidate without schedules"));
    }

    private List<Interviewer> getInterviewers(List<Long> interviewerIdList) throws ServiceException {
        List<Interviewer> interviewerList = interviewerService
                .findAll(interviewerIdList)
                .stream()
                .filter(interviewer -> interviewer.getScheduleList() != null && !interviewer.getScheduleList().isEmpty())
                .toList();
        if(interviewerList.isEmpty()){
            throw new ServiceException("No available interviewer");
        }
        return interviewerList;
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
