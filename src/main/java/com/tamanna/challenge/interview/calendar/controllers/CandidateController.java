package com.tamanna.challenge.interview.calendar.controllers;

import com.tamanna.challenge.interview.calendar.dtos.BaseResponse;
import com.tamanna.challenge.interview.calendar.dtos.PersonDTO;
import com.tamanna.challenge.interview.calendar.dtos.PersonInfoDTO;
import com.tamanna.challenge.interview.calendar.entities.jpa.Candidate;
import com.tamanna.challenge.interview.calendar.exceptions.NotFoundException;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.logging.MDCLogging;
import com.tamanna.challenge.interview.calendar.services.CandidateService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

import static com.tamanna.challenge.interview.calendar.configurations.OpenApiConfiguration.SECURITY_SCHEMA_NAME;
import static com.tamanna.challenge.interview.calendar.configurations.WebSecurityConfiguration.HAS_CANDIDATE_ROLE;
import static com.tamanna.challenge.interview.calendar.controllers.ControllerConstants.*;
import static com.tamanna.challenge.interview.calendar.controllers.ControllerUtils.buildResponse;

/**
 * @author tlferreira
 */
@RestController
@RequestMapping("/candidates")
@Validated
@AllArgsConstructor
@PreAuthorize(HAS_CANDIDATE_ROLE)
@SecurityRequirement(name = SECURITY_SCHEMA_NAME)
public class CandidateController {
    private final ModelMapper modelMapper;
    private final CandidateService candidateService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new Candidate",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successful Create"),
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
    public ResponseEntity<BaseResponse<PersonDTO>> createPerson(@Valid @RequestBody PersonInfoDTO personInfoDTO) throws ServiceException {
        MDCLogging.putObjectMDC("createCandidate{}");
        Candidate entity = candidateService.createPerson(mapDTOEntity(personInfoDTO));
        return buildResponse(mapEntityDTO(entity), HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get All Candidates",
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
    public ResponseEntity<BaseResponse<List<PersonDTO>>> listPerson(@Min(value = 0, message = INVALID_PAGE_MESSAGE) @RequestParam(value = PAGE_PARAM, defaultValue = PAGE_DEFAULT) int page,
                                                                    @Min(value = 1, message = INVALID_SIZE_MESSAGE) @RequestParam(value = SIZE_PARAM, required = false) Integer size) throws ServiceException {
        MDCLogging.putObjectMDC("listCandidate{Page[%s];Size[%s]}", page, size);
        List<Candidate> entityList = size == null ? candidateService.findAll() : candidateService.findAllPageable(page, size);
        return buildResponse(mapListEntityDTO(entityList), entityList.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Candidate by id",
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
    public ResponseEntity<BaseResponse<PersonDTO>> getPerson(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id) throws ServiceException {
        MDCLogging.putObjectMDC("getCandidate{id[%s]}", id);
        Optional<Candidate> entityOpt = candidateService.findById(id);
        return handleOptResponse(entityOpt);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Candidate by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Update"),
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
    public ResponseEntity<BaseResponse<PersonDTO>> putPerson(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id,
                                                             @Valid @RequestBody PersonInfoDTO personInfoDTO) throws ServiceException {
        MDCLogging.putObjectMDC("putCandidate{id[%s]}", id);
        Optional<Candidate> entityOpt = candidateService.update(id, mapDTOEntity(personInfoDTO));
        return handleOptResponse(entityOpt);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Candidate by id",
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
    public ResponseEntity<BaseResponse<PersonDTO>> deletePerson(@Min(value = 1, message = INVALID_ID_MESSAGE) @PathVariable(value = ID_PATH_VARIABLE) long id) throws ServiceException {
        MDCLogging.putObjectMDC("deleteCandidate{id[%s]}", id);
        Optional<Candidate> entityOpt = candidateService.delete(id);
        return handleOptResponse(entityOpt);
    }

    Candidate mapDTOEntity(PersonInfoDTO personInfoDTO) {
        return this.modelMapper.map(personInfoDTO, Candidate.class);
    }

    PersonDTO mapEntityDTO(Candidate entity) {
        return this.modelMapper.map(entity, PersonDTO.class);
    }

    List<PersonDTO> mapListEntityDTO(List<Candidate> entityList) {
        return modelMapper.map(entityList, new TypeToken<List<PersonDTO>>() {
        }.getType());
    }

    private ResponseEntity<BaseResponse<PersonDTO>> handleOptResponse(Optional<Candidate> entityOpt) {
        return entityOpt
                .map(entity -> buildResponse(mapEntityDTO(entity), HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(CANDIDATE_NOT_FOUND));
    }
}
