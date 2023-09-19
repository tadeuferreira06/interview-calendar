package com.tamanna.challenge.interview.calendar.services.impl;

import com.tamanna.challenge.interview.calendar.entities.AvailableMeeting;
import com.tamanna.challenge.interview.calendar.entities.jpa.*;
import com.tamanna.challenge.interview.calendar.exceptions.NotFoundException;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.repositories.BookingRepository;
import com.tamanna.challenge.interview.calendar.repositories.ScheduleRepository;
import com.tamanna.challenge.interview.calendar.services.CandidateService;
import com.tamanna.challenge.interview.calendar.services.InterviewerService;
import com.tamanna.challenge.interview.calendar.services.MeetingService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.tamanna.challenge.interview.calendar.services.ServiceUtils.listPersonToIdString;
import static com.tamanna.challenge.interview.calendar.services.ServiceUtils.listToString;

/**
 * @author tlferreira
 */
@Log4j2
@AllArgsConstructor
@Service
public class MeetingServiceImpl implements MeetingService {
    private final InterviewerService interviewerService;
    private final CandidateService candidateService;

    private final BookingRepository bookingRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public List<AvailableMeeting> queryMeeting(long candidateId, List<Long> interviewerIdList) throws ServiceException {
        log.debug("Start queryMeeting");
        boolean success = true;
        try {
            List<Schedule> candidateScheduleList = getCandidateSchedules(candidateId);
            List<Interviewer> interviewerList = getInterviewers(interviewerIdList);

            return getAvailableInterviewerList(candidateScheduleList, interviewerList);
        } catch (NotFoundException | IllegalArgumentException e) {
            success = false;
            log.error("Unable to queryMeeting, Exception: ", e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to queryMeeting, Exception: ", e);
            throw new ServiceException("Error queryMeeting", e);
        } finally {
            log.debug("Finished queryMeeting, success: {}", success);
        }
    }

    @Override
    public Booking bookMeeting(long scheduleId, long candidateId, List<Long> interviewerIdList) throws ServiceException {
        log.debug("Start bookMeeting");
        boolean success = true;
        try {
            log.debug("Going to get to check if meeting is available");
            List<AvailableMeeting> availableMeetings = queryMeeting(candidateId, interviewerIdList);

            AvailableMeeting availableMeeting = availableMeetings
                    .stream()
                    .filter(meeting -> meeting.getCandidateSchedule().getId() == scheduleId)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Unable to find available meeting"));

            if (availableMeeting.getInterviewerList().size() != interviewerIdList.size()) {
                throw new ServiceException(String.format("Not all Interviewers are available, Requested:[%s], Available:[%s]",
                        listToString(interviewerIdList),
                        listPersonToIdString(availableMeeting.getInterviewerList())));
            }


            Booking booking = new Booking();

            booking.setOwnerSchedule(availableMeeting.getCandidateSchedule());
            booking.setChildrenScheduleList(getInterviewersSchedules(availableMeeting));

            Booking savedBooking = bookingRepository.save(booking);
            List<Schedule> schedulesToSave = new ArrayList<>();

            schedulesToSave.addAll(savedBooking.getChildrenScheduleList());
            schedulesToSave.forEach(schedule -> schedule.setParentBooking(savedBooking));

            savedBooking.getOwnerSchedule().setOwnedBooking(savedBooking);
            schedulesToSave.add(savedBooking.getOwnerSchedule());

            scheduleRepository.saveAll(schedulesToSave);

            return savedBooking;
        } catch (NotFoundException | IllegalArgumentException e) {
            success = false;
            log.error("Unable to bookMeeting, Exception: ", e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to bookMeeting, Exception: ", e);
            throw new ServiceException("Error bookingMeeting", e);
        } finally {
            log.debug("Finished bookMeeting, success: {}", success);
        }
    }

    @Override
    public Optional<Booking> getMeeting(long id) throws ServiceException {
        log.debug("Start getMeeting");
        boolean success = true;
        try {
            return bookingRepository
                    .findById(id);
        } catch (NotFoundException | IllegalArgumentException e) {
            success = false;
            log.error("Unable to getMeeting, Exception: ", e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to getMeeting, Exception: ", e);
            throw new ServiceException("Error getMeeting", e);
        } finally {
            log.debug("Finished getMeeting, success: {}", success);
        }
    }

    @Override
    public Optional<Booking> cancelMeeting(long id) throws ServiceException {
        log.debug("Start cancelMeeting");
        boolean success = true;
        try {
            Optional<Booking> bookingOpt = bookingRepository.findById(id);
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();

                booking.getOwnerSchedule().setOwnedBooking(null);
                booking.getChildrenScheduleList().forEach(schedule -> schedule.setParentBooking(null));

                bookingRepository.delete(booking);
            }
            return bookingOpt;
        } catch (NotFoundException | IllegalArgumentException e) {
            success = false;
            log.error("Unable to cancelMeeting, Exception: ", e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to cancelMeeting, Exception: ", e);
            throw new ServiceException("Error cancelMeeting", e);
        } finally {
            log.debug("Finished cancelMeeting, success: {}", success);
        }
    }

    @Override
    public List<Booking> getCandidateMeetings(long candidateId) throws ServiceException {
        return getPersonMeetings(candidateId, true);
    }

    @Override
    public List<Booking> getInterviewerMeetings(long interviewerId) throws ServiceException {
        return getPersonMeetings(interviewerId, false);
    }

    private List<Booking> getPersonMeetings(long personId, boolean candidate) throws ServiceException {
        log.debug("Start getPersonMeeting");
        boolean success = true;
        try {
            List<Booking> bookingList = new ArrayList<>();
            if (candidate) {
                bookingList.addAll(bookingRepository.findByOwnerSchedulePersonId(personId));
            } else {
                bookingList.addAll(bookingRepository.findByChildrenScheduleListPersonId(personId));
            }
            return bookingList;
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to getPersonMeeting, Exception: ", e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to getPersonMeeting, Exception: ", e);
            throw new ServiceException("Error getMeeting", e);
        } finally {
            log.debug("Finished getPersonMeeting, success: {}", success);
        }
    }


    private List<Schedule> getInterviewersSchedules(AvailableMeeting availableMeeting) {
        LocalDate day = availableMeeting.getCandidateSchedule().getDay();
        int hour = availableMeeting.getCandidateSchedule().getHour();

        return availableMeeting
                .getInterviewerList()
                .stream()
                .map(interviewer -> filterBookedSchedules(interviewer.getScheduleList())
                        .stream()
                        .filter(this.getPredicateSchedule(day, hour))
                        .findFirst())
                .flatMap(Optional::stream)
                .toList();
    }

    private List<AvailableMeeting> getAvailableInterviewerList(List<Schedule> candidateScheduleList, List<Interviewer> interviewerList) {
        List<AvailableMeeting> availableInterviewerList = new ArrayList<>();
        for (Schedule candidateSchedule : candidateScheduleList) {
            LocalDate day = candidateSchedule.getDay();
            int hour = candidateSchedule.getHour();

            List<Interviewer> filteredInterviewerList = interviewerList
                    .stream()
                    .filter(interviewer -> filterBookedSchedules(interviewer.getScheduleList())
                            .stream()
                            .anyMatch(this.getPredicateSchedule(day, hour)))
                    .toList();

            if (!filteredInterviewerList.isEmpty()) {
                availableInterviewerList.add(new AvailableMeeting(candidateSchedule, filteredInterviewerList));
            }
        }
        return availableInterviewerList;
    }

    private Predicate<Schedule> getPredicateSchedule(LocalDate day, int hour) {
        return (schedule) -> schedule.getDay().equals(day) && schedule.getHour() == hour;
    }

    private List<Schedule> getCandidateSchedules(long candidateId) throws ServiceException {
        Candidate candidate = candidateService
                .findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Unable to find candidate"));

        return Optional
                .of(candidate)
                .filter(c -> c.getScheduleList() != null && !c.getScheduleList().isEmpty())
                .map(AbstractPerson::getScheduleList)
                .map(this::filterBookedSchedules)
                .orElseThrow(() -> new ServiceException("Candidate without schedules"));
    }

    private List<Interviewer> getInterviewers(List<Long> interviewerIdList) throws ServiceException {
        List<Interviewer> interviewerList = interviewerService
                .findAll(interviewerIdList)
                .stream()
                .filter(interviewer -> interviewer.getScheduleList() != null && !interviewer.getScheduleList().isEmpty())
                .toList();
        if (interviewerList.isEmpty()) {
            throw new ServiceException("No available interviewer");
        }
        return interviewerList;
    }

    private List<Schedule> filterBookedSchedules(List<Schedule> schedules) {
        return schedules == null ? new ArrayList<>() : schedules
                .stream()
                .filter(schedule -> schedule.getOwnedBooking() == null && schedule.getParentBooking() == null)
                .toList();
    }
}
