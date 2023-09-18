package com.tamanna.challenge.interview.calendar.dtos;

import lombok.Data;

import java.util.List;

/**
 * @author tlferreira
 */
@Data
public class AvailableMeetingDTO {
    private ScheduleDTO candidateSchedule;
    private List<PersonDTO> availableInterviewerList;
}
