package com.tamanna.challenge.interview.calendar.entities;

import lombok.*;

import java.util.List;

/**
 * @author tlferreira
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AvailableMeeting {
    private Schedule candidateSchedule;
    private List<Interviewer> interviewerList;
}
