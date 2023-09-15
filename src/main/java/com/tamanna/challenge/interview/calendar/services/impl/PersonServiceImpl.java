package com.tamanna.challenge.interview.calendar.services.impl;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.tamanna.challenge.interview.calendar.configurations.PhoneNumberValidationKeys;
import com.tamanna.challenge.interview.calendar.entities.Person;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.repositories.PersonRepository;
import com.tamanna.challenge.interview.calendar.services.PersonService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author tlferreira
 */
@Log4j2
@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final PhoneNumberValidationKeys phoneNumberValidationKeys;

    @Override
    public Person createPerson(Person person) throws ServiceException {
        log.debug("Start creating Person {}", person);
        boolean success = true;
        try {
            validateAndFormatPhoneNumber(person);

            validateEmailUniqueness(person);

            return personRepository.save(person);
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to create Person {}, Illegal Argument, Exception: ", person, e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to create Person {}, Exception: ", person, e);
            throw new ServiceException("Error creating person", e);
        } finally {
            log.debug("Finished creating Person {}, success: {}", person, success);
        }
    }

    @Override
    public List<Person> findAllPageable(PersonType personType, int page, int size) throws ServiceException {
        log.debug("Start findAllPerson paginated, PersonType: {}, Page:{} Size:{}", personType, page, size);
        boolean success = true;
        try {
            return Optional.of(this.personRepository)
                    .map(repo -> repo.findAllByPersonType(personType, PageRequest.of(page, size)))
                    .orElseGet(ArrayList::new);
        } catch (Exception e) {
            success = false;
            log.error("Unable to findAllPerson, PersonType: {}, Page:{} Size:{}, Exception: ", personType, page, size, e);
            throw new ServiceException("Error findAllPerson paginated", e);
        } finally {
            log.debug("Finished findAllPerson paginated, PersonType: {}, Page:{} Size:{}, success: {}", personType, page, size, success);
        }
    }

    @Override
    public List<Person> findAll(PersonType personType) throws ServiceException {
        log.debug("Start findAllPerson PersonType: {}", personType);
        boolean success = true;
        try {
            return Optional.of(this.personRepository)
                    .map(repo -> repo.findAllByPersonType(personType))
                    .orElseGet(ArrayList::new);
        } catch (Exception e) {
            success = false;
            log.error("Unable to findAllPerson PersonType: {}, Exception: ", personType, e);
            throw new ServiceException("Error findAllPerson", e);
        } finally {
            log.debug("Finished findAllPerson PersonType: {}, success: {}", personType, success);
        }
    }

    @Override
    public Optional<Person> findById(PersonType personType, long id) throws ServiceException {
        log.debug("Start getPerson PersonType: {}, Id: {}", personType, id);
        boolean success = true;
        try {
            return this.personRepository.findByIdAndPersonType(id, personType);
        } catch (Exception e) {
            success = false;
            log.error("Unable to getPerson PersonType: {}, Id: {}, Exception: ", personType, id, e);
            throw new ServiceException("Error getPerson", e);
        } finally {
            log.debug("Finished getPerson PersonType: {}, Id: {}, success: {}", personType, id, success);
        }
    }

    @Override
    public Optional<Person> update(long id, Person person) throws ServiceException {
        log.debug("Start updatePerson Id: {}, Person: {}", id, person);
        boolean success = true;
        try {
            Optional<Person> personOpt = this.personRepository.findByIdAndPersonType(id, person.getPersonType());
            if (personOpt.isPresent()) {
                person.setId(id);

                validateAndFormatPhoneNumber(person);
                validateEmailUniqueness(person);

                personOpt = Optional.of(this.personRepository.save(person));
            }
            return personOpt;
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to updatePerson Id: {}, Person: {}, Illegal Argument, Exception: ", id, person, e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to updatePerson Id: {}, Person: {}, Exception: ", id, person, e);
            throw new ServiceException("Error updatePerson", e);
        } finally {
            log.debug("Finished updatePerson Id: {}, Person: {}, success: {}", id, person, success);
        }
    }

    @Override
    public Optional<Person> delete(PersonType personType, long id) throws ServiceException {
        log.debug("Start deletePerson PersonType: {}, Id: {}", personType, id);
        boolean success = true;
        try {
            Optional<Person> personOpt = this.personRepository.findByIdAndPersonType(id, personType);
            if (personOpt.isPresent()) {
                this.personRepository.deleteById(id);
            }
            return personOpt;
        } catch (Exception e) {
            success = false;
            log.error("Unable to deletePerson PersonType: {}, Id: {}, Exception: ", personType, id, e);
            throw new ServiceException("Error deletePerson", e);
        } finally {
            log.debug("Finished deletePerson PersonType: {}, Id: {}, success: {}", personType, id, success);
        }
    }

    private void validateAndFormatPhoneNumber(Person person) throws IllegalArgumentException {
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(person.getPhoneNumber(), this.phoneNumberValidationKeys.getDefaultRegion());
            if (!phoneNumberUtil.isValidNumber(phoneNumber)) {
                throw new IllegalArgumentException("Invalid PhoneNumber");
            }

            String formatted = phoneNumberUtil
                    .format(phoneNumber, this.phoneNumberValidationKeys.getFormat())
                    .replace("tel:", "");
            person.setPhoneNumber(formatted);

            validatePhoneNumberUniqueness(person);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse PhoneNumber");
        }
    }

    private void validatePhoneNumberUniqueness(Person person) {
        Optional<Person> existingPersonOpt = personRepository.findByPhoneNumber(person.getPhoneNumber());

        boolean invalid = existingPersonOpt.isPresent();
        if (person.getId() > 0) {
            invalid = checkNotMatchId(person, existingPersonOpt);
        }

        if (invalid) {
            throw new IllegalArgumentException("Phone number already in use");
        }
    }

    private void validateEmailUniqueness(Person person) {
        Optional<Person> existingPersonOpt = personRepository.findByEmail(person.getEmail());

        boolean invalid = existingPersonOpt.isPresent();
        if (person.getId() > 0) {
            invalid = checkNotMatchId(person, existingPersonOpt);
        }

        if (invalid) {
            throw new IllegalArgumentException("Email address already in use");
        }
    }

    private boolean checkNotMatchId(Person person, Optional<Person> existingPersonOpt) {
        return existingPersonOpt
                .map(Person::getId)
                .filter(id -> person.getId() != id)
                .isPresent();
    }


}
