package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.entities.Interviewer;
import com.tamanna.challenge.interview.calendar.entities.Schedule;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.exceptions.NotFoundException;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.repositories.ScheduleRepository;
import com.tamanna.challenge.interview.calendar.services.impl.InterviewerScheduleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static com.tamanna.challenge.interview.calendar.DummyDataUtils.getNewPersonInterviewer;
import static com.tamanna.challenge.interview.calendar.DummyDataUtils.getNewSchedule;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

/**
 * @author tlferreira
 */
@ExtendWith(MockitoExtension.class)
class PersonScheduleServiceTests {
    @Mock
    private InterviewerService interviewerService;
    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private InterviewerScheduleServiceImpl personScheduleService;

    @Test
    void addScheduleTest_PersonNotFound() throws ServiceException {
        Interviewer person = getNewPersonInterviewer();
        Schedule schedule = getNewSchedule();

        Mockito.when(interviewerService.findById(eq(person.getId()))).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> personScheduleService.addSchedule(person.getId(), schedule));
    }

    @Test
    void addScheduleTest_ScheduleAlreadyExits() throws ServiceException {
        Interviewer person = getNewPersonInterviewer();
        Schedule schedule = getNewSchedule();

        Mockito.when(interviewerService.findById(eq(person.getId()))).thenReturn(Optional.of(person));
        Mockito.when(scheduleRepository.findByPersonIdAndDayAndHour(eq(person.getId()), eq(schedule.getDay()), eq(schedule.getHour()))).thenReturn(Optional.of(schedule));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personScheduleService.addSchedule(person.getId(), schedule));
    }

    @Test
    void addScheduleTest_Success() throws ServiceException {
        Interviewer person = getNewPersonInterviewer();
        Schedule schedule = getNewSchedule();

        Mockito.when(interviewerService.findById(eq(person.getId()))).thenReturn(Optional.of(person));
        Mockito.when(scheduleRepository.findByPersonIdAndDayAndHour(eq(person.getId()), eq(schedule.getDay()), eq(schedule.getHour()))).thenReturn(Optional.empty());
        Mockito.when(scheduleRepository.save(any())).thenAnswer(answer -> answer.getArgument(0));

        Schedule scheduleUpdated = personScheduleService.addSchedule(person.getId(), schedule);
        Assertions.assertEquals(scheduleUpdated, schedule);
    }

    @Test
    void findScheduleTest_NotFound() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        Schedule schedule = getNewSchedule(5L);
        schedule.setPerson(personA);

        Mockito.when(scheduleRepository.findByIdAndPersonIdAndPersonType(eq(schedule.getId()), eq(personA.getId()), eq(PersonType.INTERVIEWER))).thenReturn(Optional.empty());

        Optional<Schedule> scheduleOpt = personScheduleService.findById(personA.getId(), schedule.getId());
        Assertions.assertTrue(scheduleOpt.isEmpty());
    }

    @Test
    void findScheduleTest_Success() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        Schedule schedule = getNewSchedule(5L);
        schedule.setPerson(personA);

        Mockito.when(scheduleRepository.findByIdAndPersonIdAndPersonType(eq(schedule.getId()), eq(personA.getId()), eq(PersonType.INTERVIEWER))).thenReturn(Optional.of(schedule));

        Optional<Schedule> scheduleOpt = personScheduleService.findById(personA.getId(), schedule.getId());
        Assertions.assertTrue(scheduleOpt.isPresent());
    }

    @Test
    void updateScheduleTest_NotFound() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        personA.setScheduleList(new ArrayList<>());

        Schedule schedule = getNewSchedule(5L, 16);
        Schedule scheduleUpdate = getNewSchedule();

        Mockito.when(scheduleRepository.findByIdAndPersonIdAndPersonType(eq(schedule.getId()), eq(personA.getId()), eq(PersonType.INTERVIEWER))).thenReturn(Optional.empty());

        Optional<Schedule> scheduleOpt = personScheduleService.update(personA.getId(), schedule.getId(), scheduleUpdate);
        Assertions.assertTrue(scheduleOpt.isEmpty());
    }

    @Test
    void updatePersonTest_DuplicatedSchedule() {
        Interviewer personA = getNewPersonInterviewer(1L);
        personA.setScheduleList(new ArrayList<>());

        Schedule scheduleA = getNewSchedule(5L, 15);
        scheduleA.setPerson(personA);
        personA.getScheduleList().add(scheduleA);

        Schedule scheduleB = getNewSchedule(6L);
        scheduleB.setPerson(personA);
        personA.getScheduleList().add(scheduleB);
        //update = b
        Schedule scheduleUpdate = getNewSchedule();

        Mockito.when(scheduleRepository.findByIdAndPersonIdAndPersonType(eq(scheduleA.getId()), eq(personA.getId()), eq(PersonType.INTERVIEWER))).thenReturn(Optional.of(scheduleA));
        Mockito.when(scheduleRepository.findByPersonIdAndDayAndHour(eq(personA.getId()), eq(scheduleB.getDay()), eq(scheduleB.getHour()))).thenReturn(Optional.of(scheduleB));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personScheduleService.update(personA.getId(), scheduleA.getId(), scheduleUpdate));
    }

    @Test
    void updatePersonTest_SuccessHour() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        personA.setScheduleList(new ArrayList<>());

        Schedule scheduleA = getNewSchedule(5L, 15);
        scheduleA.setPerson(personA);
        personA.getScheduleList().add(scheduleA);

        Schedule scheduleB = getNewSchedule(6L, 16);
        scheduleB.setPerson(personA);
        personA.getScheduleList().add(scheduleB);
        //update = b
        Schedule scheduleUpdate = getNewSchedule();

        Mockito.when(scheduleRepository.findByIdAndPersonIdAndPersonType(eq(scheduleA.getId()), eq(personA.getId()), eq(PersonType.INTERVIEWER))).thenReturn(Optional.of(scheduleA));
        Mockito.when(scheduleRepository.findByPersonIdAndDayAndHour(eq(personA.getId()), eq(scheduleUpdate.getDay()), eq(scheduleUpdate.getHour()))).thenReturn(Optional.empty());

        Mockito.when(scheduleRepository.save(any())).thenAnswer(answer -> answer.getArgument(0));

        Optional<Schedule> scheduleUpdatedOpt = personScheduleService.update(personA.getId(), scheduleA.getId(), scheduleUpdate);
        Assertions.assertTrue(scheduleUpdatedOpt.isPresent());
        Schedule scheduleUpdated = scheduleUpdatedOpt.get();
        Assertions.assertEquals(scheduleA.getId(), scheduleUpdated.getId());
        Assertions.assertEquals(scheduleA.getDay(), scheduleUpdated.getDay());
        Assertions.assertNotEquals(scheduleA.getHour(), scheduleUpdated.getHour());
    }

    @Test
    void updatePersonTest_SuccessDay() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        personA.setScheduleList(new ArrayList<>());

        Schedule scheduleA = getNewSchedule(5L, 15, LocalDate.of(2023, 10, 15));
        scheduleA.setPerson(personA);
        personA.getScheduleList().add(scheduleA);

        Schedule scheduleB = getNewSchedule(6L, 16);
        scheduleB.setPerson(personA);
        personA.getScheduleList().add(scheduleB);
        //update = b
        Schedule scheduleUpdate = getNewSchedule();
        scheduleUpdate.setHour(scheduleA.getHour());

        Mockito.when(scheduleRepository.findByIdAndPersonIdAndPersonType(eq(scheduleA.getId()), eq(personA.getId()), eq(PersonType.INTERVIEWER))).thenReturn(Optional.of(scheduleA));
        Mockito.when(scheduleRepository.findByPersonIdAndDayAndHour(eq(personA.getId()), eq(scheduleUpdate.getDay()), eq(scheduleUpdate.getHour()))).thenReturn(Optional.empty());

        Mockito.when(scheduleRepository.save(any())).thenAnswer(answer -> answer.getArgument(0));

        Optional<Schedule> scheduleUpdatedOpt = personScheduleService.update(personA.getId(), scheduleA.getId(), scheduleUpdate);
        Assertions.assertTrue(scheduleUpdatedOpt.isPresent());
        Schedule scheduleUpdated = scheduleUpdatedOpt.get();
        Assertions.assertEquals(scheduleA.getId(), scheduleUpdated.getId());
        Assertions.assertNotEquals(scheduleA.getDay(), scheduleUpdated.getDay());
        Assertions.assertEquals(scheduleA.getHour(), scheduleUpdated.getHour());
    }

    @Test
    void deletePersonTest_NotFound() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        Schedule schedule = getNewSchedule(5L);
        Mockito.when(scheduleRepository.findByIdAndPersonIdAndPersonType(eq(schedule.getId()), eq(personA.getId()), eq(PersonType.INTERVIEWER))).thenReturn(Optional.empty());

        Optional<Schedule> scheduleOpt = personScheduleService.delete(personA.getId(), schedule.getId());
        Assertions.assertTrue(scheduleOpt.isEmpty());
    }

    @Test
    void deletePersonTest_Success() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        Schedule schedule = getNewSchedule(5L);
        Mockito.when(scheduleRepository.findByIdAndPersonIdAndPersonType(eq(schedule.getId()), eq(personA.getId()), eq(PersonType.INTERVIEWER))).thenReturn(Optional.of(schedule));

        Optional<Schedule> scheduleOpt = personScheduleService.delete(personA.getId(), schedule.getId());
        Assertions.assertTrue(scheduleOpt.isPresent());
    }
}
