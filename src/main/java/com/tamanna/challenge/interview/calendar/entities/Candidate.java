package com.tamanna.challenge.interview.calendar.entities;

import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author tlferreira
 */
@Entity
@DiscriminatorValue(PersonType.Values.CANDIDATE)
public class Candidate extends AbstractPerson {
}
