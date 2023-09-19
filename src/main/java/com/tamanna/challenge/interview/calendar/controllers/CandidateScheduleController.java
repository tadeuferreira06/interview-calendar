package com.tamanna.challenge.interview.calendar.controllers;

import com.tamanna.challenge.interview.calendar.dtos.BaseResponse;
import com.tamanna.challenge.interview.calendar.dtos.ScheduleDTO;
import com.tamanna.challenge.interview.calendar.dtos.ScheduleInfoDTO;
import com.tamanna.challenge.interview.calendar.entities.Schedule;
import com.tamanna.challenge.interview.calendar.exceptions.NotFoundException;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.logging.MDCLogging;
import com.tamanna.challenge.interview.calendar.services.CandidateScheduleService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

import static com.tamanna.challenge.interview.calendar.controllers.ControllerConstants.*;
import static com.tamanna.challenge.interview.calendar.controllers.ControllerUtils.buildResponse;

/**
 * @author tlferreira
 */
@RestController
@RequestMapping("/candidates/{id}/schedules")
@AllArgsConstructor
@Validated
public class CandidateScheduleController {
    private final ModelMapper modelMapper;
    private final CandidateScheduleService personScheduleService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<ScheduleDTO>> createSchedule(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id,
                                                                    @Valid @RequestBody ScheduleInfoDTO scheduleInfoDTO) throws ServiceException {
        MDCLogging.putObjectMDC("createCandidateSchedule{id[%s]}", id);
        Schedule schedule = personScheduleService.addSchedule(id, this.mapDTOEntity(scheduleInfoDTO));
        return buildResponse(mapEntityDTO(schedule), HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<List<ScheduleDTO>>> listSchedules(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id) throws ServiceException {
        MDCLogging.putObjectMDC("listCandidateSchedule{id[%s]}", id);
        List<Schedule> schedules = personScheduleService.findAll(id);
        return buildResponse(mapListEntityDTO(schedules), schedules.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @GetMapping(path = "/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<ScheduleDTO>> getSchedule(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id,
                                                                 @Min(value = 1, message = INVALID_SCHEDULE_ID_MESSAGE) @PathVariable(value = SCHEDULE_ID_PATH_VARIABLE) long scheduleId) throws ServiceException {
        MDCLogging.putObjectMDC("getCandidateSchedule{id[%s];scheduleId[%s]}", id, scheduleId);
        Optional<Schedule> scheduleOpt = personScheduleService.findById(id, scheduleId);
        return handleOptResponse(scheduleOpt);
    }

    @PutMapping(path = "/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<ScheduleDTO>> putSchedule(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id,
                                                                 @Min(value = 1, message = INVALID_SCHEDULE_ID_MESSAGE) @PathVariable(value = SCHEDULE_ID_PATH_VARIABLE) long scheduleId,
                                                                 @Valid @RequestBody ScheduleInfoDTO scheduleInfoDTO) throws ServiceException {
        MDCLogging.putObjectMDC("putCandidateSchedule{id[%s];scheduleId[%s]}", id, scheduleId);
        Optional<Schedule> scheduleOpt = personScheduleService.update(id, scheduleId, mapDTOEntity(scheduleInfoDTO));
        return handleOptResponse(scheduleOpt);
    }

    @DeleteMapping(path = "/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<ScheduleDTO>> deleteSchedule(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id,
                                                                    @Min(value = 1, message = INVALID_SCHEDULE_ID_MESSAGE) @PathVariable(value = SCHEDULE_ID_PATH_VARIABLE) long scheduleId) throws ServiceException {
        MDCLogging.putObjectMDC("deleteCandidateSchedule{id[%s];scheduleId[%s]}", id, scheduleId);
        Optional<Schedule> scheduleOpt = personScheduleService.delete(id, scheduleId);
        return handleOptResponse(scheduleOpt);
    }

    private ResponseEntity<BaseResponse<ScheduleDTO>> handleOptResponse(Optional<Schedule> entityOpt) {
        return entityOpt
                .map(entity -> buildResponse(mapEntityDTO(entity), HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(CANDIDATE_NOT_FOUND));
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
