package com.tamanna.challenge.interview.calendar.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tlferreira
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleDTO extends ScheduleInfoDTO {
    private long id;
    private boolean booked;
}
