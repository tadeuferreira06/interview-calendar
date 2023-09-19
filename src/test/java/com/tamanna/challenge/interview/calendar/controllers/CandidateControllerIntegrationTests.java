package com.tamanna.challenge.interview.calendar.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamanna.challenge.interview.calendar.dtos.BaseResponse;
import com.tamanna.challenge.interview.calendar.dtos.PersonDTO;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.entities.jpa.Candidate;
import com.tamanna.challenge.interview.calendar.repositories.CandidateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.tamanna.challenge.interview.calendar.DummyDataUtils.getNewPersonCandidate;
import static com.tamanna.challenge.interview.calendar.configurations.WebSecurityConfiguration.CANDIDATE_ROLE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author tlferreira
 */


@SpringBootTest
@AutoConfigureMockMvc
class CandidateControllerIntegrationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CandidateRepository candidateRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = CANDIDATE_ROLE)
    void createCandidate() throws Exception {
        Candidate candidate = getNewPersonCandidate();

        MvcResult result = mockMvc
                .perform(post("/candidates")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidate)))
                .andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());

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
