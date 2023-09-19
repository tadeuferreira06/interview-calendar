package com.tamanna.challenge.interview.calendar.controllers;

import com.tamanna.challenge.interview.calendar.dtos.AvailableMeetingDTO;
import com.tamanna.challenge.interview.calendar.dtos.BaseResponse;
import com.tamanna.challenge.interview.calendar.entities.AvailableMeeting;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.services.MeetingService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static com.tamanna.challenge.interview.calendar.controllers.ControllerConstants.*;
import static com.tamanna.challenge.interview.calendar.controllers.ControllerUtils.buildResponse;

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
    public ResponseEntity<BaseResponse<List<AvailableMeetingDTO>>> query(@Min(value = 1, message = INVALID_ID_MESSAGE) @RequestParam(CANDIDATE_ID_REQ_PARAM) long candidateId,
                                                                         @NotEmpty @RequestParam(INTERVIEWER_ID_REQ_PARAM) List<Long> interviewerIdList) throws ServiceException {
        List<AvailableMeeting> availableMeetings = this.meetingService.queryMeeting(candidateId, interviewerIdList);
        return buildResponse(mapListEntityDTO(availableMeetings), availableMeetings.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    List<AvailableMeetingDTO> mapListEntityDTO(List<AvailableMeeting> entityList) {
        return modelMapper.map(entityList, new TypeToken<List<AvailableMeetingDTO>>() {
        }.getType());
    }
}
