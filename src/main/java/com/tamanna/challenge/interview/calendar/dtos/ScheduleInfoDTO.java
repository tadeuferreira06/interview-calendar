package com.tamanna.challenge.interview.calendar.dtos;

import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * @author tlferreira
 */
@Data
public class ScheduleInfoDTO {
    @FutureOrPresent(message = "Day must be today or future")
    private LocalDate day;
    @Size(min = 0, max = 23, message = "Hour must be [0,23]")
    private int hour;
}
