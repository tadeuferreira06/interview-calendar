package com.tamanna.challenge.interview.calendar.controllers;

import com.tamanna.challenge.interview.calendar.dtos.InterviewerDTO;
import com.tamanna.challenge.interview.calendar.entities.Interviewer;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.services.PersonService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author tlferreira
 */
@RestController
@RequestMapping("/interviewer")
@AllArgsConstructor
@Validated
public class InterviewerController {
    private final ModelMapper modelMapper;
    private final PersonService<Interviewer> personService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InterviewerDTO> createInterviewer(@Valid @RequestBody InterviewerDTO interviewerDTO) throws ServiceException {
        Interviewer interviewer = modelMapper.map(interviewerDTO, Interviewer.class);
        interviewer = personService.createPerson(interviewer);
        return new ResponseEntity<>(modelMapper.map(interviewer, InterviewerDTO.class), HttpStatus.CREATED);
    }
}
