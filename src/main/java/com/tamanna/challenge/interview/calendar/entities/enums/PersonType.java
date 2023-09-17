package com.tamanna.challenge.interview.calendar.entities.enums;

/**
 * @author tlferreira
 */

public enum PersonType {
    INTERVIEWER(Values.INTERVIEWER),
    CANDIDATE(Values.CANDIDATE);


    PersonType(String value) {
        if (!this.toString().equals(value)) {
            throw new IllegalArgumentException("Invalid Enum configuration!");
        }
    }

    public class Values {
        public static final String INTERVIEWER = "INTERVIEWER";
        public static final String CANDIDATE = "CANDIDATE";
    }
}
