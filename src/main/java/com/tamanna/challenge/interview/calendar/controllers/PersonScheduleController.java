package com.tamanna.challenge.interview.calendar.controllers;

import com.tamanna.challenge.interview.calendar.dtos.PersonDTO;
import com.tamanna.challenge.interview.calendar.dtos.ScheduleDTO;
import com.tamanna.challenge.interview.calendar.dtos.ScheduleInfoDTO;
import com.tamanna.challenge.interview.calendar.entities.Person;
import com.tamanna.challenge.interview.calendar.entities.Schedule;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.services.PersonScheduleService;
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
@RequestMapping("/{personType}s/{id}/schedules")
@AllArgsConstructor
@Validated
public class PersonScheduleController {
    private final ModelMapper modelMapper;
    private final PersonScheduleService personScheduleService;
    private final PersonService personService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleDTO> createSchedule(@PathVariable(PERSON_TYPE_PATH_PARAM) PersonType personType, @Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id, @RequestBody ScheduleInfoDTO scheduleInfoDTO) throws ServiceException {
        Schedule schedule = this.mapDTOEntity(scheduleInfoDTO);

        Optional<Schedule> scheduleOpt = personScheduleService.addSchedule(personType, id, schedule);

        return handleOptResponse(scheduleOpt);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ScheduleDTO>> listSchedules(@PathVariable(PERSON_TYPE_PATH_PARAM) PersonType personType, @Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id) throws ServiceException {
        Optional<Person> personOpt = personService.findById(personType, id);

        if (personOpt.isPresent()) {
            return personOpt
                    .map(Person::getAvailableSchedules)
                    .map(schedules -> new ResponseEntity<>(this.mapListEntityDTO(schedules), HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleDTO> getSchedule(@PathVariable(PERSON_TYPE_PATH_PARAM) PersonType personType, @Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id, @Min(value = 1, message = INVALID_SCHEDULE_ID_MESSAGE) @PathVariable(value = SCHEDULE_ID_PATH_VARIABLE) long scheduleId) throws ServiceException {
        Optional<Schedule> scheduleOpt = personScheduleService.findById(personType, id, scheduleId);

        return handleOptResponse(scheduleOpt);
    }

    @PutMapping(path = "/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleDTO> putSchedule(@PathVariable(PERSON_TYPE_PATH_PARAM) PersonType personType, @Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id, @Min(value = 1, message = INVALID_SCHEDULE_ID_MESSAGE) @PathVariable(value = SCHEDULE_ID_PATH_VARIABLE) long scheduleId, @RequestBody ScheduleInfoDTO scheduleInfoDTO) throws ServiceException {
        Optional<Schedule> scheduleOpt = personScheduleService.update(personType, id, scheduleId, mapDTOEntity(scheduleInfoDTO));

        return handleOptResponse(scheduleOpt);
    }

    @DeleteMapping(path = "/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleDTO> deleteSchedule(@PathVariable(PERSON_TYPE_PATH_PARAM) PersonType personType, @Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id, @Min(value = 1, message = INVALID_SCHEDULE_ID_MESSAGE) @PathVariable(value = SCHEDULE_ID_PATH_VARIABLE) long scheduleId) throws ServiceException {
        Optional<Schedule> scheduleOpt = personScheduleService.delete(personType, id, scheduleId);

        return handleOptResponse(scheduleOpt);
    }

    private ResponseEntity<ScheduleDTO> handleOptResponse(Optional<Schedule> entityOpt) {
        return entityOpt
                .map(entity -> new ResponseEntity<>(mapEntityDTO(entity), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    Schedule mapDTOEntity(ScheduleInfoDTO scheduleDTO) {
        return modelMapper.map(scheduleDTO, Schedule.class);
    }

    ScheduleDTO mapEntityDTO(Schedule entity) {
        return modelMapper.map(entity, ScheduleDTO.class);
    }

    List<ScheduleDTO> mapListEntityDTO(List<Schedule> entityList) {
        return modelMapper.map(entityList, new TypeToken<List<ScheduleDTO>>() {
        }.getType());
    }
}
