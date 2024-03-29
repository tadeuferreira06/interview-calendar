package com.tamanna.challenge.interview.calendar.entities.jpa;

import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author tlferreira
 */
@Entity
@DiscriminatorValue(PersonType.Values.INTERVIEWER)
public class Interviewer extends AbstractPerson {

}
