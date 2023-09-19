package com.tamanna.challenge.interview.calendar.dtos;

import lombok.Data;

import java.util.List;

/**
 * @author tlferreira
 */
@Data
public class BookingDTO {
    private long id;
    private ScheduleDTO candidateSchedule;
    private PersonDTO candidate;
    private List<PersonDTO> interviewerList;
}
