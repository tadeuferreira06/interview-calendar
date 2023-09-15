package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.configurations.PhoneNumberValidationKeys;
import com.tamanna.challenge.interview.calendar.entities.Person;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.repositories.PersonRepository;
import com.tamanna.challenge.interview.calendar.services.impl.PersonServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.tamanna.challenge.interview.calendar.DummyDataUtils.getNewPersonCandidate;
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
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    @Test
    void createPersonTest_InvalidPhoneNumber() throws ServiceException {
        Person person = getNewPersonInterviewer();
        person.setPhoneNumber("123abcd");

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.createPerson(person));
    }

    @Test
    void createPersonTest_PhoneNumberAlreadyExists() throws ServiceException {
        Person person = getNewPersonInterviewer();

        //returns the same object for simplicity’s sake
        Mockito.when(personRepository.findByPhoneNumber(eq(person.getPhoneNumber()))).thenReturn(Optional.of(person));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.createPerson(person));
    }

    @Test
    void createPersonTest_EmailAlreadyExists() throws ServiceException {
        Person person = getNewPersonInterviewer();

        Mockito.when(personRepository.findByPhoneNumber(eq(person.getPhoneNumber()))).thenReturn(Optional.empty());
        //returns the same object for simplicity’s sake
        Mockito.when(personRepository.findByEmail(eq(person.getEmail()))).thenReturn(Optional.of(person));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.createPerson(person));
    }

    @Test
    void createPersonTest_Success() throws ServiceException {
        Person person = getNewPersonInterviewer();

        //returns the same object
        Mockito.when(personRepository.findByPhoneNumber(eq(person.getPhoneNumber()))).thenReturn(Optional.empty());
        Mockito.when(personRepository.findByEmail(eq(person.getEmail()))).thenReturn(Optional.empty());
        Mockito.when(personRepository.save(eq(person))).thenReturn(person);

        Assertions.assertNotNull(personService.createPerson(person));
    }

    @Test
    void findAllPersonPaginatedTest_Success() throws ServiceException {
        Person personA = getNewPersonInterviewer();
        Person personA2 = getNewPersonInterviewer();

        Pageable pageable01 = PageRequest.of(0, 1);
        Pageable pageable11 = PageRequest.of(1, 1);
        Pageable pageable02 = PageRequest.of(0, 2);

        //returns the same object
        Mockito.when(personRepository.findAllByPersonType(eq(PersonType.INTERVIEWER), eq(pageable01))).thenReturn(List.of(personA));
        Mockito.when(personRepository.findAllByPersonType(eq(PersonType.INTERVIEWER), eq(pageable11))).thenReturn(List.of(personA2));
        Mockito.when(personRepository.findAllByPersonType(eq(PersonType.INTERVIEWER), eq(pageable02))).thenReturn(List.of(personA, personA2));

        List<Person> interviewerList = personService.findAllPageable(PersonType.INTERVIEWER, 0, 1);
        Assertions.assertNotNull(interviewerList);
        Assertions.assertFalse(interviewerList.isEmpty());
        Assertions.assertEquals(1, interviewerList.size());
        Assertions.assertTrue(interviewerList.contains(personA));

        interviewerList = personService.findAllPageable(PersonType.INTERVIEWER, 1, 1);
        Assertions.assertNotNull(interviewerList);
        Assertions.assertFalse(interviewerList.isEmpty());
        Assertions.assertEquals(1, interviewerList.size());
        Assertions.assertTrue(interviewerList.contains(personA2));

        interviewerList = personService.findAllPageable(PersonType.INTERVIEWER, 0, 2);
        Assertions.assertNotNull(interviewerList);
        Assertions.assertFalse(interviewerList.isEmpty());
        Assertions.assertEquals(2, interviewerList.size());
        Assertions.assertTrue(interviewerList.contains(personA));
        Assertions.assertTrue(interviewerList.contains(personA2));
    }

    @Test
    void findAllPersonTest_Success() throws ServiceException {
        Person personA = getNewPersonInterviewer();
        Person personA2 = getNewPersonInterviewer();
        Person personB = getNewPersonCandidate();

        //returns the same object
        Mockito.when(personRepository.findAllByPersonType(eq(PersonType.INTERVIEWER))).thenReturn(List.of(personA, personA2));
        Mockito.when(personRepository.findAllByPersonType(eq(PersonType.CANDIDATE))).thenReturn(List.of(personB));

        List<Person> interviewerList = personService.findAll(PersonType.INTERVIEWER);

        Assertions.assertNotNull(interviewerList);
        Assertions.assertFalse(interviewerList.isEmpty());
        Assertions.assertEquals(2, interviewerList.size());
        Assertions.assertTrue(interviewerList.contains(personA));
        Assertions.assertTrue(interviewerList.contains(personA2));
        Assertions.assertFalse(interviewerList.contains(personB));

        List<Person> candidateList = personService.findAll(PersonType.CANDIDATE);

        Assertions.assertNotNull(candidateList);
        Assertions.assertFalse(candidateList.isEmpty());
        Assertions.assertEquals(1, candidateList.size());
        Assertions.assertFalse(candidateList.contains(personA));
        Assertions.assertFalse(candidateList.contains(personA2));
        Assertions.assertTrue(candidateList.contains(personB));

        Mockito.when(personRepository.findAllByPersonType(eq(PersonType.CANDIDATE))).thenReturn(Collections.emptyList());
        List<Person> candidateList2 = personService.findAll(PersonType.CANDIDATE);
        Assertions.assertNotNull(candidateList2);
        Assertions.assertTrue(candidateList2.isEmpty());
    }

    @Test
    void findPersonTest_NotFound() throws ServiceException {
        Person personA = getNewPersonInterviewer(1L);
        Mockito.when(personRepository.findByIdAndPersonType(eq(personA.getId()), eq(personA.getPersonType()))).thenReturn(Optional.empty());

        Optional<Person> personOpt = personService.findById(personA.getPersonType(), personA.getId());
        Assertions.assertTrue(personOpt.isEmpty());
    }

    @Test
    void findPersonTest_Success() throws ServiceException {
        Person personA = getNewPersonInterviewer(1L);

        Mockito.when(personRepository.findByIdAndPersonType(eq(personA.getId()), eq(personA.getPersonType()))).thenReturn(Optional.of(personA));

        Optional<Person> personOpt = personService.findById(personA.getPersonType(), personA.getId());
        Assertions.assertTrue(personOpt.isPresent());
    }

    @Test
    void updatePersonTest_NotFound() throws ServiceException {
        Person personA = getNewPersonInterviewer(1L);
        Person personUpdate = getNewPersonInterviewer();

        Mockito.when(personRepository.findByIdAndPersonType(eq(personA.getId()), eq(personA.getPersonType()))).thenReturn(Optional.empty());

        Optional<Person> personOpt = personService.update(personA.getId(), personUpdate);
        Assertions.assertTrue(personOpt.isEmpty());
    }

    @Test
    void updatePersonTest_NotFoundPersonType() throws ServiceException {
        Person personA = getNewPersonInterviewer(1L);
        Person personUpdate = getNewPersonInterviewer();
        personUpdate.setPersonType(PersonType.CANDIDATE);

        Mockito.when(personRepository.findByIdAndPersonType(eq(personA.getId()), eq(personUpdate.getPersonType()))).thenReturn(Optional.empty());

        Optional<Person> personOpt = personService.update(personA.getId(), personUpdate);
        Assertions.assertTrue(personOpt.isEmpty());
    }

    @Test
    void updatePersonTest_InvalidPhoneNumber() throws ServiceException {
        Person personA = getNewPersonInterviewer(1L);
        Person personUpdate = getNewPersonInterviewer();
        personUpdate.setPhoneNumber("123123");

        Mockito.when(personRepository.findByIdAndPersonType(eq(personA.getId()), eq(personA.getPersonType()))).thenReturn(Optional.of(personA));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.update(personA.getId(), personUpdate));
    }

    @Test
    void updatePersonTest_PhoneNumberAlreadyExists() throws ServiceException {
        Person personA = getNewPersonInterviewer(1L);

        Person personB = getNewPersonCandidate(2L);
        Person personUpdate = getNewPersonInterviewer();
        personUpdate.setPhoneNumber(personB.getPhoneNumber());

        Mockito.when(personRepository.findByIdAndPersonType(eq(personA.getId()), eq(personA.getPersonType()))).thenReturn(Optional.of(personA));
        Mockito.when(personRepository.findByPhoneNumber(eq(personUpdate.getPhoneNumber()))).thenReturn(Optional.of(personB));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.update(personA.getId(), personUpdate));
    }

    @Test
    void updatePersonTest_EmailAlreadyExists() throws ServiceException {
        Person personA = getNewPersonInterviewer(1L);

        Person personB = getNewPersonCandidate(2L);
        Person personUpdate = getNewPersonInterviewer();
        personUpdate.setEmail(personB.getEmail());

        Mockito.when(personRepository.findByIdAndPersonType(eq(personA.getId()), eq(personA.getPersonType()))).thenReturn(Optional.of(personA));

        Mockito.when(personRepository.findByPhoneNumber(eq(personUpdate.getPhoneNumber()))).thenReturn(Optional.empty());
        Mockito.when(personRepository.findByEmail(eq(personUpdate.getEmail()))).thenReturn(Optional.of(personB));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.update(personA.getId(), personUpdate));
    }

    @Test
    void updatePersonTest_SuccessEmail() throws ServiceException {
        Person personA = getNewPersonInterviewer(1L);
        Person personUpdate = getNewPersonInterviewer();
        personUpdate.setEmail("ab@sapo.pt");

        Mockito.when(personRepository.findByIdAndPersonType(eq(personA.getId()), eq(personA.getPersonType()))).thenReturn(Optional.of(personA));
        //return self
        Mockito.when(personRepository.findByPhoneNumber(eq(personUpdate.getPhoneNumber()))).thenReturn(Optional.of(personA));
        Mockito.when(personRepository.findByEmail(eq(personUpdate.getEmail()))).thenReturn(Optional.empty());

        Mockito.when(personRepository.save(any())).thenAnswer(answer -> answer.getArgument(0));

        Optional<Person> personUpdatedOpt = personService.update(personA.getId(), personUpdate);

        Assertions.assertTrue(personUpdatedOpt.isPresent());
        Person personUpdated = personUpdatedOpt.get();
        Assertions.assertEquals(personA.getId(), personUpdated.getId());
        Assertions.assertEquals(personA.getFirstName(), personUpdated.getFirstName());
        Assertions.assertEquals(personA.getLastName(), personUpdated.getLastName());
        Assertions.assertEquals(personA.getPersonType(), personUpdated.getPersonType());
        Assertions.assertEquals(personA.getPhoneNumber(), personUpdated.getPhoneNumber());
        Assertions.assertNotEquals(personA.getEmail(), personUpdated.getEmail());
    }

    @Test
    void updatePersonTest_SuccessPhoneNumber() throws ServiceException {
        Person personA = getNewPersonInterviewer(1L);
        Person personUpdate = getNewPersonInterviewer();
        personUpdate.setPhoneNumber("+351-911-222-333");

        Mockito.when(personRepository.findByIdAndPersonType(eq(personA.getId()), eq(personA.getPersonType()))).thenReturn(Optional.of(personA));
        Mockito.when(personRepository.findByPhoneNumber(eq(personUpdate.getPhoneNumber()))).thenReturn(Optional.empty());
        //return self
        Mockito.when(personRepository.findByEmail(eq(personUpdate.getEmail()))).thenReturn(Optional.of(personA));

        Mockito.when(personRepository.save(any())).thenAnswer(answer -> answer.getArgument(0));

        Optional<Person> personUpdatedOpt = personService.update(personA.getId(), personUpdate);

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
        Person personA = getNewPersonInterviewer(1L);
        Mockito.when(personRepository.findByIdAndPersonType(eq(personA.getId()), eq(personA.getPersonType()))).thenReturn(Optional.empty());

        Optional<Person> personOpt = personService.delete(personA.getPersonType(), personA.getId());
        Assertions.assertTrue(personOpt.isEmpty());
    }

    @Test
    void deletePersonTest_Success() throws ServiceException {
        Person personA = getNewPersonInterviewer(1L);

        Mockito.when(personRepository.findByIdAndPersonType(eq(personA.getId()), eq(personA.getPersonType()))).thenReturn(Optional.of(personA));

        Optional<Person> personOpt = personService.delete(personA.getPersonType(), personA.getId());
        Assertions.assertTrue(personOpt.isPresent());
    }
}
