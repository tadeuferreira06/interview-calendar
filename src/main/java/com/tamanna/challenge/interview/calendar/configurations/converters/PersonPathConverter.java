package com.tamanna.challenge.interview.calendar.configurations.converters;

import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class PersonPathConverter implements Converter<String, PersonType> {
    @Override
    public PersonType convert(String value) {
        return PersonType.valueOf(value.toUpperCase());
    }
}