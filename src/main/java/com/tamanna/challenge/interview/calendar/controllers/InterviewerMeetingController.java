package com.tamanna.challenge.interview.calendar.controllers;

import com.tamanna.challenge.interview.calendar.dtos.BaseResponse;
import com.tamanna.challenge.interview.calendar.dtos.BookingDTO;
import com.tamanna.challenge.interview.calendar.entities.jpa.Booking;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.logging.MDCLogging;
import com.tamanna.challenge.interview.calendar.services.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

import static com.tamanna.challenge.interview.calendar.configurations.OpenApiConfiguration.SECURITY_SCHEMA_NAME;
import static com.tamanna.challenge.interview.calendar.configurations.WebSecurityConfiguration.HAS_INTERVIEWER_ROLE;
import static com.tamanna.challenge.interview.calendar.controllers.ControllerConstants.ID_PATH_VARIABLE;
import static com.tamanna.challenge.interview.calendar.controllers.ControllerConstants.INVALID_ID_MESSAGE;
import static com.tamanna.challenge.interview.calendar.controllers.ControllerUtils.buildResponse;

/**
 * @author tlferreira
 */
@RestController
@RequestMapping("/interviewers/{id}/meetings")
@AllArgsConstructor
@Validated
@PreAuthorize(HAS_INTERVIEWER_ROLE)
@SecurityRequirement(name = SECURITY_SCHEMA_NAME)
public class InterviewerMeetingController {
    private final ModelMapper modelMapper;
    private final MeetingService meetingService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List Interviewer Meetings",
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
    public ResponseEntity<BaseResponse<List<BookingDTO>>> listSchedules(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id) throws ServiceException {
        MDCLogging.putObjectMDC("listInterviewerMeetings{id[%s]}", id);
        List<Booking> meetings = meetingService.getInterviewerMeetings(id);
        return buildResponse(mapListEntityDTO(meetings), meetings.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    List<BookingDTO> mapListEntityDTO(List<Booking> entityList) {
        return modelMapper.map(entityList, new TypeToken<List<BookingDTO>>() {
        }.getType());
    }
}
