package com.tamanna.challenge.interview.calendar.entities;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author tlferreira
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue("INTERVIEWER")
public class Interviewer extends AbstractPerson {

}
