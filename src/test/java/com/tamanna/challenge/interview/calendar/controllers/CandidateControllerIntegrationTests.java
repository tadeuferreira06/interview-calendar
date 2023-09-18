package com.tamanna.challenge.interview.calendar.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamanna.challenge.interview.calendar.dtos.BaseResponse;
import com.tamanna.challenge.interview.calendar.dtos.PersonDTO;
import com.tamanna.challenge.interview.calendar.entities.Candidate;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.repositories.CandidateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.tamanna.challenge.interview.calendar.DummyDataUtils.getNewPersonCandidate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author tlferreira
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CandidateControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CandidateRepository candidateRepository;

    @Test
    void createCandidate() throws Exception {
        Candidate candidate = getNewPersonCandidate();

        MvcResult result = mockMvc.perform(post("/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidate)))
                .andReturn();

        BaseResponse<PersonDTO> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<BaseResponse<PersonDTO>>() {
        });
        Assertions.assertNotNull(response.getResponse());

        PersonDTO personDTO = response.getResponse();
        Assertions.assertNotNull(personDTO);

        Assertions.assertTrue(personDTO.getId() > 0);
        Assertions.assertEquals(PersonType.CANDIDATE, personDTO.getPersonType());
        Assertions.assertEquals(personDTO.getFirstName(), candidate.getFirstName());
        Assertions.assertEquals(personDTO.getLastName(), candidate.getLastName());
        Assertions.assertEquals(personDTO.getEmail(), candidate.getEmail());
        Assertions.assertEquals(personDTO.getPhoneNumber(), candidate.getPhoneNumber());

        Assertions.assertTrue(candidateRepository.findById(personDTO.getId()).isPresent());
    }
}
