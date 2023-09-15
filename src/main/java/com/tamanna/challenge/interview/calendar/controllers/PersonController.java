package com.tamanna.challenge.interview.calendar.controllers;

import com.tamanna.challenge.interview.calendar.dtos.PersonDTO;
import com.tamanna.challenge.interview.calendar.dtos.PersonInfoDTO;
import com.tamanna.challenge.interview.calendar.entities.Person;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.services.PersonService;
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
@RequestMapping("/{personType}s")
@Validated
@AllArgsConstructor
public class PersonController {
    private final ModelMapper modelMapper;
    private final PersonService personService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> createPerson(@PathVariable(PERSON_TYPE_PATH_PARAM) PersonType personType, @RequestBody PersonInfoDTO personInfoDTO) throws ServiceException {
        Person entity = personService.createPerson(mapDTOEntity(personInfoDTO, personType));

        return new ResponseEntity<>(mapEntityDTO(entity), HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonDTO>> listPerson(@PathVariable(PERSON_TYPE_PATH_PARAM) PersonType personType, @Min(value = 0, message = INVALID_PAGE_MESSAGE) @RequestParam(value = PAGE_PARAM, defaultValue = PAGE_DEFAULT) int page, @Min(value = 1, message = INVALID_SIZE_MESSAGE) @RequestParam(value = SIZE_PARAM, required = false) Integer size) throws ServiceException {
        List<Person> entityList = size == null ? personService.findAll(personType) : personService.findAllPageable(personType, page, size);

        return new ResponseEntity<>(mapListEntityDTO(entityList), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> getPerson(@PathVariable(PERSON_TYPE_PATH_PARAM) PersonType personType, @Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id) throws ServiceException {
        Optional<Person> entityOpt = personService.findById(personType, id);

        return handleOptResponse(entityOpt);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> putPerson(@PathVariable(PERSON_TYPE_PATH_PARAM) PersonType personType, @Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id, @RequestBody PersonInfoDTO personInfoDTO) throws ServiceException {
        Optional<Person> entityOpt = personService.update(id, mapDTOEntity(personInfoDTO, personType));

        return handleOptResponse(entityOpt);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> deletePerson(@PathVariable(PERSON_TYPE_PATH_PARAM) PersonType personType, @Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id) throws ServiceException {
        Optional<Person> entityOpt = personService.delete(personType, id);

        return handleOptResponse(entityOpt);
    }

    Person mapDTOEntity(PersonInfoDTO personInfoDTO, PersonType personType) {
        Person entity = this.modelMapper.map(personInfoDTO, Person.class);
        entity.setPersonType(personType);
        return entity;
    }

    PersonDTO mapEntityDTO(Person entity) {
        return this.modelMapper.map(entity, PersonDTO.class);
    }

    List<PersonDTO> mapListEntityDTO(List<Person> entityList) {
        return modelMapper.map(entityList, new TypeToken<List<PersonDTO>>() {
        }.getType());
    }

    private ResponseEntity<PersonDTO> handleOptResponse(Optional<Person> entityOpt) {
        return entityOpt
                .map(entity -> new ResponseEntity<>(mapEntityDTO(entity), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
