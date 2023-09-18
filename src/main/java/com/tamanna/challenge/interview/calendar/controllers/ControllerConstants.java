package com.tamanna.challenge.interview.calendar.controllers;

/**
 * @author tlferreira
 */
public class ControllerConstants {
    public static final String INVALID_ID_MESSAGE = "Invalid Id";
    public static final String ID_PATH_VARIABLE = "id";
    public static final String INVALID_SCHEDULE_ID_MESSAGE = "Invalid scheduleId";
    public static final String SCHEDULE_ID_PATH_VARIABLE = "scheduleId";
    public static final String CANDIDATE_ID_REQ_PARAM = "candidateId";
    public static final String INTERVIEWER_ID_REQ_PARAM = "interviewerId";
    public static final String INVALID_PAGE_MESSAGE = "Invalid page, must be greater than or equal to 0";
    public static final String INVALID_SIZE_MESSAGE = "Invalid size, must be greater than 0";
    public static final String PAGE_PARAM = "page";
    public static final String PAGE_DEFAULT = "0";
    public static final String SIZE_PARAM = "size";
    public static final String INTERVIEWER_NOT_FOUND = "Interviewer not found";
    public static final String CANDIDATE_NOT_FOUND = "Candidate not found";

    private ControllerConstants() {
        //private constructor
    }
}
