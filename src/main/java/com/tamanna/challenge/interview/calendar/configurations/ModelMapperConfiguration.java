package com.tamanna.challenge.interview.calendar.configurations;

import com.tamanna.challenge.interview.calendar.dtos.BookingDTO;
import com.tamanna.challenge.interview.calendar.dtos.PersonDTO;
import com.tamanna.challenge.interview.calendar.dtos.ScheduleDTO;
import com.tamanna.challenge.interview.calendar.entities.enums.PersonType;
import com.tamanna.challenge.interview.calendar.entities.jpa.Booking;
import com.tamanna.challenge.interview.calendar.entities.jpa.Candidate;
import com.tamanna.challenge.interview.calendar.entities.jpa.Interviewer;
import com.tamanna.challenge.interview.calendar.entities.jpa.Schedule;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author tlferreira
 */
@Configuration
@Log4j2
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper
                .createTypeMap(Interviewer.class, PersonDTO.class)
                .addMapping(src -> PersonType.INTERVIEWER, PersonDTO::setPersonType);
        modelMapper
                .createTypeMap(Candidate.class, PersonDTO.class)
                .addMapping(src -> PersonType.CANDIDATE, PersonDTO::setPersonType);


        Converter<Schedule, Boolean> bookedScheduleConverter =
                context -> {
                    Optional<Schedule> sourceOpt = Optional.ofNullable(context.getSource());

                    Optional<Booking> ownedOpt = sourceOpt.map(Schedule::getOwnedBooking);
                    Optional<Booking> parentOpt = sourceOpt.map(Schedule::getParentBooking);

                    return ownedOpt.isPresent() || parentOpt.isPresent();
                };

        modelMapper
                .createTypeMap(Schedule.class, ScheduleDTO.class)
                .addMappings(mapper -> mapper.using(bookedScheduleConverter).map(schedule -> schedule, ScheduleDTO::setBooked));

        Converter<List<Schedule>, List<PersonDTO>> interviewerConverter =
                context -> Optional
                        .ofNullable(context.getSource())
                        .orElseGet(ArrayList::new)
                        .stream()
                        .map(Schedule::getPerson)
                        .map(person -> modelMapper.map(person, PersonDTO.class))
                        .toList();

        modelMapper
                .createTypeMap(Booking.class, BookingDTO.class)
                .addMapping(Booking::getOwnerSchedule, BookingDTO::setScheduleDTO)
                .addMapping(src -> src.getOwnerSchedule().getPerson(), BookingDTO::setCandidate)
                .addMappings(mapper -> mapper.using(interviewerConverter).map(Booking::getChildrenScheduleList, BookingDTO::setInterviewerList));

        return modelMapper;
    }
}
