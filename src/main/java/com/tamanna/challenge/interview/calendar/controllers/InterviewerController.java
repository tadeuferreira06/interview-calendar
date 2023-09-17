package com.tamanna.challenge.interview.calendar.controllers;

import com.tamanna.challenge.interview.calendar.dtos.PersonDTO;
import com.tamanna.challenge.interview.calendar.dtos.PersonInfoDTO;
import com.tamanna.challenge.interview.calendar.entities.Interviewer;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.services.InterviewerService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

import static com.tamanna.challenge.interview.calendar.controllers.ControllerConstants.*;

/**
 * @author tlferreira
 */
@RestController
@RequestMapping("/interviewers")
@Validated
@AllArgsConstructor
public class InterviewerController {
    private final ModelMapper modelMapper;
    private final InterviewerService interviewerService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonInfoDTO personInfoDTO) throws ServiceException {
        Interviewer entity = interviewerService.createPerson(mapDTOEntity(personInfoDTO));

        return new ResponseEntity<>(mapEntityDTO(entity), HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonDTO>> listPerson(@Min(value = 0, message = INVALID_PAGE_MESSAGE) @RequestParam(value = PAGE_PARAM, defaultValue = PAGE_DEFAULT) int page, @Min(value = 1, message = INVALID_SIZE_MESSAGE) @RequestParam(value = SIZE_PARAM, required = false) Integer size) throws ServiceException {
        List<Interviewer> entityList = size == null ? interviewerService.findAll() : interviewerService.findAllPageable(page, size);

        return new ResponseEntity<>(mapListEntityDTO(entityList), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> getPerson(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id) throws ServiceException {
        Optional<Interviewer> entityOpt = interviewerService.findById(id);

        return handleOptResponse(entityOpt);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> putPerson(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id, @RequestBody PersonInfoDTO personInfoDTO) throws ServiceException {
        Optional<Interviewer> entityOpt = interviewerService.update(id, mapDTOEntity(personInfoDTO));

        return handleOptResponse(entityOpt);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> deletePerson(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id) throws ServiceException {
        Optional<Interviewer> entityOpt = interviewerService.delete(id);

        return handleOptResponse(entityOpt);
    }

    Interviewer mapDTOEntity(PersonInfoDTO personInfoDTO) {
        return this.modelMapper.map(personInfoDTO, Interviewer.class);
    }

    PersonDTO mapEntityDTO(Interviewer entity) {
        return this.modelMapper.map(entity, PersonDTO.class);
    }

    List<PersonDTO> mapListEntityDTO(List<Interviewer> entityList) {
        return modelMapper.map(entityList, new TypeToken<List<PersonDTO>>() {
        }.getType());
    }

    private ResponseEntity<PersonDTO> handleOptResponse(Optional<Interviewer> entityOpt) {
        return entityOpt
                .map(entity -> new ResponseEntity<>(mapEntityDTO(entity), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
