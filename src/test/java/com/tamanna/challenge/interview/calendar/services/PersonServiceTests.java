package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.configurations.PhoneNumberValidationKeys;
import com.tamanna.challenge.interview.calendar.entities.Person;
import com.tamanna.challenge.interview.calendar.entities.Interviewer;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.repositories.InterviewerRepository;
import com.tamanna.challenge.interview.calendar.services.impl.InterviewerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.tamanna.challenge.interview.calendar.DummyDataUtils.getNewPersonInterviewer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

/**
 * @author tlferreira
 */
@ExtendWith(MockitoExtension.class)
class PersonServiceTests {
    @Spy
    private PhoneNumberValidationKeys phoneNumberValidationKeys;

    @Mock
    private InterviewerRepository personRepository;

    @InjectMocks
    private InterviewerServiceImpl personService;

    @Test
    void createPersonTest_InvalidPhoneNumber() throws ServiceException {
        Interviewer person = getNewPersonInterviewer();
        person.setPhoneNumber("123abcd");

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.createPerson(person));
    }

    @Test
    void createPersonTest_PhoneNumberAlreadyExists() throws ServiceException {
        Interviewer person = getNewPersonInterviewer();

        //returns the same object for simplicity’s sake
        Mockito.when(personRepository.findByPhoneNumber(eq(person.getPhoneNumber()))).thenReturn(Optional.of(person));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.createPerson(person));
    }

    @Test
    void createPersonTest_EmailAlreadyExists() throws ServiceException {
        Interviewer person = getNewPersonInterviewer();

        Mockito.when(personRepository.findByPhoneNumber(eq(person.getPhoneNumber()))).thenReturn(Optional.empty());
        //returns the same object for simplicity’s sake
        Mockito.when(personRepository.findByEmail(eq(person.getEmail()))).thenReturn(Optional.of(person));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.createPerson(person));
    }

    @Test
    void createPersonTest_Success() throws ServiceException {
        Interviewer person = getNewPersonInterviewer();

        //returns the same object
        Mockito.when(personRepository.findByPhoneNumber(eq(person.getPhoneNumber()))).thenReturn(Optional.empty());
        Mockito.when(personRepository.findByEmail(eq(person.getEmail()))).thenReturn(Optional.empty());
        Mockito.when(personRepository.save(eq(person))).thenReturn(person);

        Assertions.assertNotNull(personService.createPerson(person));
    }

    @Test
    void findAllPersonPaginatedTest_Success() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer();
        Interviewer personA2 = getNewPersonInterviewer();

        Pageable pageable01 = PageRequest.of(0, 1);
        Pageable pageable11 = PageRequest.of(1, 1);
        Pageable pageable02 = PageRequest.of(0, 2);

        //returns the same object
        Mockito.when(personRepository.findAll(eq(pageable01))).thenReturn(new PageImpl<>(List.of(personA)));
        Mockito.when(personRepository.findAll(eq(pageable11))).thenReturn(new PageImpl<>(List.of(personA2)));
        Mockito.when(personRepository.findAll(eq(pageable02))).thenReturn(new PageImpl<>(List.of(personA, personA2)));

        List<Interviewer> interviewerList = personService.findAllPageable(0, 1);
        Assertions.assertNotNull(interviewerList);
        Assertions.assertFalse(interviewerList.isEmpty());
        Assertions.assertEquals(1, interviewerList.size());
        Assertions.assertTrue(interviewerList.contains(personA));

        interviewerList = personService.findAllPageable(1, 1);
        Assertions.assertNotNull(interviewerList);
        Assertions.assertFalse(interviewerList.isEmpty());
        Assertions.assertEquals(1, interviewerList.size());
        Assertions.assertTrue(interviewerList.contains(personA2));

        interviewerList = personService.findAllPageable(0, 2);
        Assertions.assertNotNull(interviewerList);
        Assertions.assertFalse(interviewerList.isEmpty());
        Assertions.assertEquals(2, interviewerList.size());
        Assertions.assertTrue(interviewerList.contains(personA));
        Assertions.assertTrue(interviewerList.contains(personA2));
    }

    @Test
    void findAllPersonTest_Success() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer();
        Interviewer personA2 = getNewPersonInterviewer();

        //returns the same object
        Mockito.when(personRepository.findAll()).thenReturn(List.of(personA, personA2));

        List<Interviewer> interviewerList = personService.findAll();

        Assertions.assertNotNull(interviewerList);
        Assertions.assertFalse(interviewerList.isEmpty());
        Assertions.assertEquals(2, interviewerList.size());
        Assertions.assertTrue(interviewerList.contains(personA));
        Assertions.assertTrue(interviewerList.contains(personA2));
    }

    @Test
    void findPersonTest_NotFound() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        Mockito.when(personRepository.findById(eq(personA.getId()))).thenReturn(Optional.empty());

        Optional<Interviewer> personOpt = personService.findById(personA.getId());
        Assertions.assertTrue(personOpt.isEmpty());
    }

    @Test
    void findPersonTest_Success() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);

        Mockito.when(personRepository.findById(eq(personA.getId()))).thenReturn(Optional.of(personA));

        Optional<Interviewer> personOpt = personService.findById(personA.getId());
        Assertions.assertTrue(personOpt.isPresent());
    }

    @Test
    void updatePersonTest_NotFound() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        Interviewer personUpdate = getNewPersonInterviewer();

        Mockito.when(personRepository.findById(eq(personA.getId()))).thenReturn(Optional.empty());

        Optional<Interviewer> personOpt = personService.update(personA.getId(), personUpdate);
        Assertions.assertTrue(personOpt.isEmpty());
    }

    @Test
    void updatePersonTest_NotFoundPersonType() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        Interviewer personUpdate = getNewPersonInterviewer();

        Mockito.when(personRepository.findById(eq(personA.getId()))).thenReturn(Optional.empty());

        Optional<Interviewer> personOpt = personService.update(personA.getId(), personUpdate);
        Assertions.assertTrue(personOpt.isEmpty());
    }

    @Test
    void updatePersonTest_InvalidPhoneNumber() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        Interviewer personUpdate = getNewPersonInterviewer();
        personUpdate.setPhoneNumber("123123");

        Mockito.when(personRepository.findById(eq(personA.getId()))).thenReturn(Optional.of(personA));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.update(personA.getId(), personUpdate));
    }

    @Test
    void updatePersonTest_PhoneNumberAlreadyExists() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        Interviewer personB = getNewPersonInterviewer(2L);
        personB.setPhoneNumber("+351-911-222-333");

        Assertions.assertNotEquals(personA.getPhoneNumber(), personB.getPhoneNumber());

        Interviewer personUpdate = getNewPersonInterviewer();
        personUpdate.setPhoneNumber(personB.getPhoneNumber());

        Mockito.when(personRepository.findById(eq(personA.getId()))).thenReturn(Optional.of(personA));
        Mockito.when(personRepository.findByPhoneNumber(eq(personUpdate.getPhoneNumber()))).thenReturn(Optional.of(personB));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.update(personA.getId(), personUpdate));
    }

    @Test
    void updatePersonTest_EmailAlreadyExists() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        Interviewer personB = getNewPersonInterviewer(2L);
        personB.setEmail("test@email.com");

        Assertions.assertNotEquals(personA.getEmail(), personB.getEmail());

        Interviewer personUpdate = getNewPersonInterviewer();
        personUpdate.setEmail(personB.getEmail());

        Mockito.when(personRepository.findById(eq(personA.getId()))).thenReturn(Optional.of(personA));

        Mockito.when(personRepository.findByPhoneNumber(eq(personUpdate.getPhoneNumber()))).thenReturn(Optional.empty());
        Mockito.when(personRepository.findByEmail(eq(personUpdate.getEmail()))).thenReturn(Optional.of(personB));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.update(personA.getId(), personUpdate));
    }

    @Test
    void updatePersonTest_SuccessEmail() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        Interviewer personUpdate = getNewPersonInterviewer();
        personUpdate.setEmail("ab@sapo.pt");

        Mockito.when(personRepository.findById(eq(personA.getId()))).thenReturn(Optional.of(personA));
        //return self
        Mockito.when(personRepository.findByPhoneNumber(eq(personUpdate.getPhoneNumber()))).thenReturn(Optional.of(personA));
        Mockito.when(personRepository.findByEmail(eq(personUpdate.getEmail()))).thenReturn(Optional.empty());

        Mockito.when(personRepository.save(any())).thenAnswer(answer -> answer.getArgument(0));

        Optional<Interviewer> personUpdatedOpt = personService.update(personA.getId(), personUpdate);

        Assertions.assertTrue(personUpdatedOpt.isPresent());
        Interviewer personUpdated = personUpdatedOpt.get();
        Assertions.assertEquals(personA.getId(), personUpdated.getId());
        Assertions.assertEquals(personA.getFirstName(), personUpdated.getFirstName());
        Assertions.assertEquals(personA.getLastName(), personUpdated.getLastName());
        Assertions.assertEquals(personA.getPersonType(), personUpdated.getPersonType());
        Assertions.assertEquals(personA.getPhoneNumber(), personUpdated.getPhoneNumber());
        Assertions.assertNotEquals(personA.getEmail(), personUpdated.getEmail());
    }

    @Test
    void updatePersonTest_SuccessPhoneNumber() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        Interviewer personUpdate = getNewPersonInterviewer();
        personUpdate.setPhoneNumber("+351-911-222-333");

        Mockito.when(personRepository.findById(eq(personA.getId()))).thenReturn(Optional.of(personA));
        Mockito.when(personRepository.findByPhoneNumber(eq(personUpdate.getPhoneNumber()))).thenReturn(Optional.empty());
        //return self
        Mockito.when(personRepository.findByEmail(eq(personUpdate.getEmail()))).thenReturn(Optional.of(personA));

        Mockito.when(personRepository.save(any())).thenAnswer(answer -> answer.getArgument(0));

        Optional<Interviewer> personUpdatedOpt = personService.update(personA.getId(), personUpdate);

        Assertions.assertTrue(personUpdatedOpt.isPresent());
        Person personUpdated = personUpdatedOpt.get();
        Assertions.assertEquals(personA.getId(), personUpdated.getId());
        Assertions.assertEquals(personA.getFirstName(), personUpdated.getFirstName());
        Assertions.assertEquals(personA.getLastName(), personUpdated.getLastName());
        Assertions.assertEquals(personA.getPersonType(), personUpdated.getPersonType());
        Assertions.assertNotEquals(personA.getPhoneNumber(), personUpdated.getPhoneNumber());
        Assertions.assertEquals(personA.getEmail(), personUpdated.getEmail());
    }

    @Test
    void deletePersonTest_NotFound() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);
        Mockito.when(personRepository.findById(eq(personA.getId()))).thenReturn(Optional.empty());

        Optional<Interviewer> personOpt = personService.delete(personA.getId());
        Assertions.assertTrue(personOpt.isEmpty());
    }

    @Test
    void deletePersonTest_Success() throws ServiceException {
        Interviewer personA = getNewPersonInterviewer(1L);

        Mockito.when(personRepository.findById(eq(personA.getId()))).thenReturn(Optional.of(personA));

        Optional<Interviewer> personOpt = personService.delete(personA.getId());
        Assertions.assertTrue(personOpt.isPresent());
    }
}
