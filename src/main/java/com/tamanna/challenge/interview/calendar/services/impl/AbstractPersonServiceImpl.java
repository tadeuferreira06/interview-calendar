package com.tamanna.challenge.interview.calendar.services.impl;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.tamanna.challenge.interview.calendar.configurations.PhoneNumberValidationKeys;
import com.tamanna.challenge.interview.calendar.entities.AbstractPerson;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import com.tamanna.challenge.interview.calendar.repositories.PersonRepository;
import com.tamanna.challenge.interview.calendar.services.PersonService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author tlferreira
 */
@Log4j2
@AllArgsConstructor
public abstract class AbstractPersonServiceImpl<T extends AbstractPerson, E extends PersonRepository<T>> implements PersonService <T> {
    private final E personRepository;
    private final PhoneNumberValidationKeys phoneNumberValidationKeys;

    @Override
    public T createPerson(T person) throws ServiceException {
        log.debug("Start creatingPerson");
        boolean success = true;
        try {
            validateAndFormatPhoneNumber(person);

            validateEmailUniqueness(person);

            return personRepository.save(person);
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to createPerson, Illegal Argument, Exception: ", e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to createPerson, Exception: ", e);
            throw new ServiceException("Error creating person", e);
        } finally {
            log.debug("Finished creatingPerson, success: {}", success);
        }
    }

    @Override
    public List<T> findAllPageable(int page, int size) throws ServiceException {

        log.debug("Start findAllPerson paginated");
        boolean success = true;
        try {
            return Optional.of(this.personRepository)
                    .map(repo -> repo.findAll(PageRequest.of(page, size)))
                    .map(Page::getContent)
                    .orElseGet(ArrayList::new);
        } catch (Exception e) {
            success = false;
            log.error("Unable to findAllPerson, Exception: ", e);
            throw new ServiceException("Error findAllPerson paginated", e);
        } finally {
            log.debug("Finished findAllPerson paginated, success: {}", success);
        }
    }

    @Override
    public List<T> findAll() throws ServiceException {
        log.debug("Start findAllPerson");
        boolean success = true;
        try {
            return Optional.of(this.personRepository)
                    .map(JpaRepository::findAll)
                    .orElseGet(ArrayList::new);
        } catch (Exception e) {
            success = false;
            log.error("Unable to findAllPerson, Exception: ", e);
            throw new ServiceException("Error findAllPerson", e);
        } finally {
            log.debug("Finished findAllPerson, success: {}", success);
        }
    }

    @Override
    public List<T> findAll(List<Long> ids) throws ServiceException {
        log.debug("Start findAllById");
        boolean success = true;
        try {
            return Optional.of(this.personRepository)
                    .map(repo -> repo.findAllById(ids))
                    .orElseGet(ArrayList::new);
        } catch (Exception e) {
            success = false;
            log.error("Unable to findAllById, Exception: ", e);
            throw new ServiceException("Error findAllPerson Ids", e);
        } finally {
            log.debug("Finished findAllById, success: {}", success);
        }
    }

    @Override
    public Optional<T> findById(long id) throws ServiceException {
        log.debug("Start getPerson");
        boolean success = true;
        try {
            return this.personRepository.findById(id);
        } catch (Exception e) {
            success = false;
            log.error("Unable to getPerson, Exception: ", e);
            throw new ServiceException("Error getPerson", e);
        } finally {
            log.debug("Finished getPerson, success: {}", success);
        }
    }

    @Override
    public Optional<T> update(long id, T person) throws ServiceException {
        log.debug("Start updatePerson");
        boolean success = true;
        try {
            Optional<T> personOpt = this.personRepository.findById(id);
            if (personOpt.isPresent()) {
                person.setId(id);

                validateAndFormatPhoneNumber(person);
                validateEmailUniqueness(person);

                personOpt = Optional.of(this.personRepository.save(person));
            }
            return personOpt;
        } catch (IllegalArgumentException e) {
            success = false;
            log.error("Unable to updatePerson, Illegal Argument, Exception: ", e);
            throw e;
        } catch (Exception e) {
            success = false;
            log.error("Unable to updatePerson, Exception: ", e);
            throw new ServiceException("Error updatePerson", e);
        } finally {
            log.debug("Finished updatePerson, success: {}", success);
        }
    }

    @Override
    public Optional<T> delete(long id) throws ServiceException {
        log.debug("Start deletePerson");
        boolean success = true;
        try {
            Optional<T> personOpt = this.personRepository.findById(id);
            if (personOpt.isPresent()) {
                this.personRepository.deleteById(id);
            }
            return personOpt;
        } catch (Exception e) {
            success = false;
            log.error("Unable to deletePerson, Exception: ", e);
            throw new ServiceException("Error deletePerson", e);
        } finally {
            log.debug("Finished deletePerson, success: {}", success);
        }
    }

    private void validateAndFormatPhoneNumber(AbstractPerson person) throws IllegalArgumentException {
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

    private void validatePhoneNumberUniqueness(AbstractPerson person) {
        Optional<T> existingPersonOpt = personRepository.findByPhoneNumber(person.getPhoneNumber());

        boolean invalid = existingPersonOpt.isPresent();
        if (person.getId() > 0) {
            invalid = checkNotMatchId(person, existingPersonOpt);
        }

        if (invalid) {
            throw new IllegalArgumentException("Phone number already in use");
        }
    }

    private void validateEmailUniqueness(AbstractPerson person) {
        Optional<T> existingPersonOpt = personRepository.findByEmail(person.getEmail());

        boolean invalid = existingPersonOpt.isPresent();
        if (person.getId() > 0) {
            invalid = checkNotMatchId(person, existingPersonOpt);
        }

        if (invalid) {
            throw new IllegalArgumentException("Email address already in use");
        }
    }

    private boolean checkNotMatchId(AbstractPerson person, Optional<T> existingPersonOpt) {
        return existingPersonOpt
                .map(AbstractPerson::getId)
                .filter(id -> person.getId() != id)
                .isPresent();
    }


}
