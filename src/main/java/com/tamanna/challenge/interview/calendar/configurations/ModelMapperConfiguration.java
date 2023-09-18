package com.tamanna.challenge.interview.calendar.configurations;

import com.tamanna.challenge.interview.calendar.dtos.PersonDTO;
import com.tamanna.challenge.interview.calendar.dtos.ScheduleDTO;
import com.tamanna.challenge.interview.calendar.entities.Candidate;
import com.tamanna.challenge.interview.calendar.entities.Interviewer;
import com.tamanna.challenge.interview.calendar.entities.Schedule;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tlferreira
 */
@Configuration
@Log4j2
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        //added mappings because of AvailableMeetingDTO
        modelMapper.createTypeMap(Interviewer.class, PersonDTO.class);
        modelMapper.createTypeMap(Candidate.class, PersonDTO.class);

        modelMapper.createTypeMap(Schedule.class, ScheduleDTO.class);

        return new ModelMapper();
    }
}
