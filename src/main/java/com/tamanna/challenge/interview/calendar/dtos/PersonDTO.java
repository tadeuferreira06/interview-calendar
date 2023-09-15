package com.tamanna.challenge.interview.calendar.dtos;

import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tlferreira
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PersonDTO extends PersonInfoDTO {
    private long id;
    private PersonType personType;
}
