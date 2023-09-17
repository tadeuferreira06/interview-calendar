package com.tamanna.challenge.interview.calendar.controllers;

import com.tamanna.challenge.interview.calendar.dtos.ScheduleDTO;
import com.tamanna.challenge.interview.calendar.dtos.ScheduleInfoDTO;
import com.tamanna.challenge.interview.calendar.entities.Interviewer;
import com.tamanna.challenge.interview.calendar.entities.Person;
import com.tamanna.challenge.interview.calendar.entities.Schedule;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.services.InterviewerScheduleService;
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
@RequestMapping("/interviewers/{id}/schedules")
@AllArgsConstructor
@Validated
public class InterviewerScheduleController {
    private final ModelMapper modelMapper;
    private final InterviewerScheduleService personScheduleService;
    private final InterviewerService personService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleDTO> createSchedule(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id, @RequestBody ScheduleInfoDTO scheduleInfoDTO) throws ServiceException {
        Optional<Schedule> scheduleOpt = Optional.empty();

        Optional<Interviewer> personOpt = personService.findById(id);
        if(personOpt.isPresent()){
            Schedule schedule = this.mapDTOEntity(scheduleInfoDTO);
            scheduleOpt = personScheduleService.addSchedule(personOpt.get(), schedule);
        }

        return handleOptResponse(scheduleOpt);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ScheduleDTO>> listSchedules(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id) throws ServiceException {
        Optional<Interviewer> personOpt = personService.findById(id);

        if (personOpt.isPresent()) {
            return personOpt
                    .map(Person::getAvailableSchedules)
                    .map(schedules -> new ResponseEntity<>(this.mapListEntityDTO(schedules), HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleDTO> getSchedule(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id, @Min(value = 1, message = INVALID_SCHEDULE_ID_MESSAGE) @PathVariable(value = SCHEDULE_ID_PATH_VARIABLE) long scheduleId) throws ServiceException {
        Optional<Schedule> scheduleOpt = personScheduleService.findById(id, scheduleId);

        return handleOptResponse(scheduleOpt);
    }

    @PutMapping(path = "/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleDTO> putSchedule(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id, @Min(value = 1, message = INVALID_SCHEDULE_ID_MESSAGE) @PathVariable(value = SCHEDULE_ID_PATH_VARIABLE) long scheduleId, @RequestBody ScheduleInfoDTO scheduleInfoDTO) throws ServiceException {
        Optional<Schedule> scheduleOpt = personScheduleService.update(id, scheduleId, mapDTOEntity(scheduleInfoDTO));

        return handleOptResponse(scheduleOpt);
    }

    @DeleteMapping(path = "/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleDTO> deleteSchedule(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id, @Min(value = 1, message = INVALID_SCHEDULE_ID_MESSAGE) @PathVariable(value = SCHEDULE_ID_PATH_VARIABLE) long scheduleId) throws ServiceException {
        Optional<Schedule> scheduleOpt = personScheduleService.delete( id, scheduleId);

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
