package com.tamanna.challenge.interview.calendar.dtos;

import lombok.Data;

import java.util.List;

/**
 * @author tlferreira
 */
@Data
public class BookingDTO {
    private long id;
    private ScheduleDTO scheduleDTO;
    private PersonDTO candidate;
    private List<PersonDTO> interviewerList;
}
