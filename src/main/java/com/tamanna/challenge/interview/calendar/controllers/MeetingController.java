package com.tamanna.challenge.interview.calendar.controllers;

import com.tamanna.challenge.interview.calendar.dtos.AvailableMeetingDTO;
import com.tamanna.challenge.interview.calendar.dtos.BaseResponse;
import com.tamanna.challenge.interview.calendar.dtos.BookingDTO;
import com.tamanna.challenge.interview.calendar.entities.AvailableMeeting;
import com.tamanna.challenge.interview.calendar.entities.jpa.Booking;
import com.tamanna.challenge.interview.calendar.exceptions.NotFoundException;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.logging.MDCLogging;
import com.tamanna.challenge.interview.calendar.services.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tamanna.challenge.interview.calendar.controllers.ControllerConstants.*;
import static com.tamanna.challenge.interview.calendar.controllers.ControllerUtils.buildResponse;
import static com.tamanna.challenge.interview.calendar.controllers.ControllerUtils.listToString;

/**
 * @author tlferreira
 */
@RestController
@RequestMapping("/meetings")
@Validated
@AllArgsConstructor
public class MeetingController {
    private final ModelMapper modelMapper;
    private final MeetingService meetingService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Query Meeting for Candidate",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Get"),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    ),
            })
    public ResponseEntity<BaseResponse<List<AvailableMeetingDTO>>> query(@Min(value = 1, message = INVALID_ID_MESSAGE) @RequestParam(CANDIDATE_ID_REQ_PARAM) long candidateId,
                                                                         @RequestParam(value = INTERVIEWER_ID_REQ_PARAM, required = false) List<Long> interviewerIdList) throws ServiceException {
        MDCLogging.putObjectMDC("queryMeeting{candidateId[%s],interviewerId:[%s]}", candidateId, listToString(interviewerIdList));
        List<AvailableMeeting> availableMeetings = this.meetingService.queryMeeting(candidateId, Optional.ofNullable(interviewerIdList).orElseGet(ArrayList::new));
        return buildResponse(mapListEntityDTO(availableMeetings), availableMeetings.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @PostMapping(path = "/book/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Book a Meeting with Candidate Schedule",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successful Create"),
                    @ApiResponse(responseCode = "304",
                            description = "Not Modified",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    ),
            })
    public ResponseEntity<BaseResponse<BookingDTO>> book(@Min(value = 1, message = INVALID_SCHEDULE_ID_MESSAGE) @PathVariable(SCHEDULE_ID_PATH_VARIABLE) long scheduleId,
                                                         @Min(value = 1, message = INVALID_ID_MESSAGE) @RequestParam(CANDIDATE_ID_REQ_PARAM) long candidateId,
                                                         @NotEmpty @RequestParam(INTERVIEWER_ID_REQ_PARAM) List<Long> interviewerIdList) throws ServiceException {
        MDCLogging.putObjectMDC("bookMeeting{scheduleId[%s],candidateId[%s],interviewerId:[%s]}", scheduleId, candidateId, listToString(interviewerIdList));
        Booking booking = this.meetingService.bookMeeting(scheduleId, candidateId, interviewerIdList);
        return buildResponse(mapEntityDTO(booking), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Booked Meeting",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Get"),
                    @ApiResponse(responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    ),
            })
    public ResponseEntity<BaseResponse<BookingDTO>> get(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(ID_PATH_VARIABLE) long id) throws ServiceException {
        MDCLogging.putObjectMDC("getMeeting{id[%s]}", id);
        return handleOptResponse(this.meetingService.getMeeting(id));
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Candidate Schedule by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Delete"),
                    @ApiResponse(responseCode = "304",
                            description = "Not Modified",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    ),
            })
    public ResponseEntity<BaseResponse<BookingDTO>> cancel(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(ID_PATH_VARIABLE) long id) throws ServiceException {
        MDCLogging.putObjectMDC("cancelMeeting{id[%s]}", id);
        return handleOptResponse(this.meetingService.cancelMeeting(id));
    }

    BookingDTO mapEntityDTO(Booking entity) {
        return modelMapper.map(entity, BookingDTO.class);
    }

    List<AvailableMeetingDTO> mapListEntityDTO(List<AvailableMeeting> entityList) {
        return modelMapper.map(entityList, new TypeToken<List<AvailableMeetingDTO>>() {
        }.getType());
    }

    private ResponseEntity<BaseResponse<BookingDTO>> handleOptResponse(Optional<Booking> entityOpt) {
        return entityOpt
                .map(entity -> buildResponse(mapEntityDTO(entity), HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(MEETING_NOT_FOUND));
    }

}
