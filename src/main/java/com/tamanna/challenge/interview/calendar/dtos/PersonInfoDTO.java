package com.tamanna.challenge.interview.calendar.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author tlferreira
 */
@Data
public class PersonInfoDTO {
    @NotBlank(message = "First name cannot be empty!")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty!")
    private String lastName;
    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Must be a valid email format!")
    private String email;
    @NotBlank(message = "phoneNumber cannot be empty!")
    private String phoneNumber;
}
