package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.entities.AvailableMeeting;
import com.tamanna.challenge.interview.calendar.entities.Candidate;
import com.tamanna.challenge.interview.calendar.entities.Interviewer;
import com.tamanna.challenge.interview.calendar.entities.Schedule;
import com.tamanna.challenge.interview.calendar.exceptions.NotFoundException;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.services.impl.MeetingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tamanna.challenge.interview.calendar.DummyDataUtils.*;
import static org.mockito.ArgumentMatchers.*;

/**
 * @author tlferreira
 */
@ExtendWith(MockitoExtension.class)
class MeetingServiceTests {
    @Mock
    private CandidateService candidateService;

    @Mock
    private InterviewerService interviewerService;

    @InjectMocks
    private MeetingServiceImpl meetingService;

    @Test
    void queryMeeting_CandidateNotFound() throws ServiceException {
        Mockito.when(candidateService.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> meetingService.queryMeeting(1L, List.of(2L)));
    }

    @Test
    void queryMeeting_CandidateWithoutSchedules() throws ServiceException {
        Candidate candidate = getNewPersonCandidate(1L);

        Mockito.when(candidateService.findById(anyLong())).thenReturn(Optional.of(candidate));

        Assertions.assertThrows(ServiceException.class, () -> meetingService.queryMeeting(1L, List.of(2L)));
    }

    @Test
    void queryMeeting_InterviewerNotFound() throws ServiceException {
        Candidate candidate = getNewPersonCandidateWithSchedule(1L);

        List<Long> ids = List.of(2L, 3L);

        Mockito.when(candidateService.findById(anyLong())).thenReturn(Optional.of(candidate));
        Mockito.when(interviewerService.findAll(eq(ids))).thenReturn(new ArrayList<>());

        Assertions.assertThrows(ServiceException.class, () -> meetingService.queryMeeting(1L, ids));
    }

    @Test
    void queryMeeting_InterviewerWithoutSchedules() throws ServiceException {
        Candidate candidate = getNewPersonCandidateWithSchedule(1L);

        Interviewer interviewer = getNewPersonInterviewer(2L);

        List<Long> ids = List.of(interviewer.getId());

        Mockito.when(candidateService.findById(anyLong())).thenReturn(Optional.of(candidate));
        Mockito.when(interviewerService.findAll(eq(ids))).thenReturn(List.of(interviewer));

        Assertions.assertThrows(ServiceException.class, () -> meetingService.queryMeeting(1L, ids));
    }

    @Test
    void queryMeeting_SuccessOneInterviewer() throws ServiceException {
        Candidate candidate = getNewPersonCandidateWithSchedule(1L);

        Interviewer interviewer = getNewPersonInterviewerWithSchedule(2L);
        List<Long> ids = List.of(interviewer.getId());

        Mockito.when(candidateService.findById(anyLong())).thenReturn(Optional.of(candidate));
        Mockito.when(interviewerService.findAll(eq(ids))).thenReturn(List.of(interviewer));

        List<AvailableMeeting> availableMeetingList = meetingService.queryMeeting(1L, ids);

        Assertions.assertFalse(availableMeetingList.isEmpty());
        Assertions.assertEquals(1, availableMeetingList.size());

        AvailableMeeting availableMeeting = availableMeetingList.get(0);
        Assertions.assertEquals(1, availableMeeting.getInterviewerList().size());

        Schedule candidateSchedule = candidate.getScheduleList().get(0);
        Assertions.assertEquals(candidateSchedule, availableMeeting.getCandidateSchedule());

        Schedule interviewerSchedule = interviewer.getScheduleList().get(0);
        Assertions.assertEquals(interviewerSchedule.getHour(), candidateSchedule.getHour());
        Assertions.assertEquals(interviewerSchedule.getDay(), candidateSchedule.getDay());

        Assertions.assertTrue(availableMeeting.getInterviewerList().contains(interviewer));
    }

    @Test
    void queryMeeting_SuccessTwoInterviewer() throws ServiceException {
        Candidate candidate = getNewPersonCandidateWithSchedule(1L);

        Interviewer interviewer = getNewPersonInterviewer(2L);
        interviewer.setScheduleList(new ArrayList<>());
        //equal to candidate's schedule
        interviewer.getScheduleList().add(getNewSchedule(2L));

        Interviewer interviewerB = getNewPersonInterviewer(3L);
        interviewerB.setScheduleList(new ArrayList<>());
        interviewerB.getScheduleList().add(getNewSchedule(2L, 5));

        List<Long> ids = List.of(interviewer.getId(), interviewerB.getId());

        Mockito.when(candidateService.findById(anyLong())).thenReturn(Optional.of(candidate));
        Mockito.when(interviewerService.findAll(eq(ids))).thenReturn(List.of(interviewer, interviewerB));

        List<AvailableMeeting> availableMeetingList = meetingService.queryMeeting(1L, ids);

        Assertions.assertFalse(availableMeetingList.isEmpty());
        Assertions.assertEquals(1, availableMeetingList.size());

        AvailableMeeting availableMeeting = availableMeetingList.get(0);
        Assertions.assertEquals(1, availableMeeting.getInterviewerList().size());

        Schedule candidateSchedule = candidate.getScheduleList().get(0);
        Assertions.assertEquals(candidateSchedule, availableMeeting.getCandidateSchedule());

        Schedule interviewerSchedule = interviewer.getScheduleList().get(0);
        Assertions.assertEquals(interviewerSchedule.getHour(), candidateSchedule.getHour());
        Assertions.assertEquals(interviewerSchedule.getDay(), candidateSchedule.getDay());

        Assertions.assertTrue(availableMeeting.getInterviewerList().contains(interviewer));
    }

    @Test
    void queryMeeting_SuccessThreeInterviewer() throws ServiceException {
        Candidate candidate = getNewPersonCandidateWithSchedule(1L);

        Interviewer interviewer = getNewPersonInterviewer(2L);
        interviewer.setScheduleList(new ArrayList<>());
        //equal to candidate's schedule
        interviewer.getScheduleList().add(getNewSchedule(2L));

        Interviewer interviewerB = getNewPersonInterviewer(3L);
        interviewerB.setScheduleList(new ArrayList<>());
        interviewerB.getScheduleList().add(getNewSchedule(2L, 5));

        Interviewer interviewerC = getNewPersonInterviewer(3L);
        interviewerC.setScheduleList(new ArrayList<>());
        //equal to candidate's schedule
        interviewerC.getScheduleList().add(getNewSchedule(3L, 19));
        candidate.getScheduleList().add(getNewSchedule(3L, 19));

        List<Long> ids = List.of(interviewer.getId(), interviewerB.getId(), interviewerC.getId());

        Mockito.when(candidateService.findById(anyLong())).thenReturn(Optional.of(candidate));
        Mockito.when(interviewerService.findAll(eq(ids))).thenReturn(List.of(interviewer, interviewerB, interviewerC));

        List<AvailableMeeting> availableMeetingList = meetingService.queryMeeting(1L, ids);

        Assertions.assertFalse(availableMeetingList.isEmpty());
        Assertions.assertEquals(2, availableMeetingList.size());

        AvailableMeeting availableMeeting = availableMeetingList.get(0);
        Assertions.assertEquals(1, availableMeeting.getInterviewerList().size());

        Schedule candidateSchedule = candidate.getScheduleList().get(0);
        Assertions.assertEquals(candidateSchedule, availableMeeting.getCandidateSchedule());

        Schedule interviewerSchedule = interviewer.getScheduleList().get(0);
        Assertions.assertEquals(interviewerSchedule.getHour(), candidateSchedule.getHour());
        Assertions.assertEquals(interviewerSchedule.getDay(), candidateSchedule.getDay());

        Assertions.assertTrue(availableMeeting.getInterviewerList().contains(interviewer));

        availableMeeting = availableMeetingList.get(1);
        Assertions.assertEquals(1, availableMeeting.getInterviewerList().size());
        candidateSchedule = candidate.getScheduleList().get(1);
        Assertions.assertEquals(candidateSchedule, availableMeeting.getCandidateSchedule());

        interviewerSchedule = interviewerC.getScheduleList().get(0);
        Assertions.assertEquals(interviewerSchedule.getHour(), candidateSchedule.getHour());
        Assertions.assertEquals(interviewerSchedule.getDay(), candidateSchedule.getDay());
        Assertions.assertTrue(availableMeeting.getInterviewerList().contains(interviewerC));
    }
}
