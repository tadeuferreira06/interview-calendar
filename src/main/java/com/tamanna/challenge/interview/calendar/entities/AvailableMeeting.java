package com.tamanna.challenge.interview.calendar.entities;

import com.tamanna.challenge.interview.calendar.entities.jpa.Interviewer;
import com.tamanna.challenge.interview.calendar.entities.jpa.Schedule;
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
