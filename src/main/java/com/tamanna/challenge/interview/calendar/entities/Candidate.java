package com.tamanna.challenge.interview.calendar.entities;

import lombok.*;

import javax.persistence.*;

/**
 * @author tlferreira
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue("CANDIDATE")
public class Candidate extends AbstractPerson {

}
