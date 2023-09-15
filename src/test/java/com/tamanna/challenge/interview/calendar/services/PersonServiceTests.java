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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

/**
 * @author tlferreira
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PersonServiceTests {
    @Spy
    private PhoneNumberValidationKeys phoneNumberValidationKeys;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    @Test
    void createPersonTest_InvalidPhoneNumber() throws ServiceException {
        Person person = getNewPerson();
        person.setPhoneNumber("123abcd");

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.createPerson(person));
    }

    @Test
    void createPersonTest_PhoneNumberAlreadyExists() throws ServiceException {
        Person person = getNewPerson();

        //returns the same object
        Mockito.when(personRepository.findByPhoneNumber(person.getPhoneNumber())).thenReturn(Optional.of(person));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.createPerson(person));
    }

    @Test
    void createPersonTest_EmailAlreadyExists() throws ServiceException {
        Person person = getNewPerson();

        //returns the same object
        Mockito.when(personRepository.findByPhoneNumber(person.getPhoneNumber())).thenReturn(Optional.empty());
        Mockito.when(personRepository.findByEmail(person.getEmail())).thenReturn(Optional.of(person));

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.createPerson(person));
    }

    @Test
    void createPersonTest_Success() throws ServiceException {
        Person person = getNewPerson();

        //returns the same object
        Mockito.when(personRepository.findByPhoneNumber(person.getPhoneNumber())).thenReturn(Optional.empty());
        Mockito.when(personRepository.findByEmail(person.getEmail())).thenReturn(Optional.empty());
        Mockito.when(personRepository.save(person)).thenReturn(person);

        Assertions.assertNotNull(personService.createPerson(person));
    }

    private Person getNewPerson() {
        Person person = new Person();
        person.setFirstName("Albino");
        person.setLastName("Silva");
        person.setEmail("as@mail.com");
        person.setPhoneNumber("+351-244-000-000");
        person.setPersonType(PersonType.INTERVIEWER);
        return person;
    }
}
