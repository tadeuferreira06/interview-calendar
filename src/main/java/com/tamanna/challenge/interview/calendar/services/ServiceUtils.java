package com.tamanna.challenge.interview.calendar.services;

import com.tamanna.challenge.interview.calendar.entities.jpa.AbstractPerson;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tlferreira
 */
public class ServiceUtils {
    private ServiceUtils() {
        //private
    }

    public static <T extends AbstractPerson> String listPersonToIdString(List<T> list) {
        if (list != null) {
            return listToString(list
                    .stream()
                    .filter(Objects::nonNull)
                    .map(AbstractPerson::getId)
                    .toList());
        }
        return "";
    }

    public static String listToString(List<?> list) {
        if (list != null) {
            return list
                    .stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
        }
        return "";
    }

}
