package com.tamanna.challenge.interview.calendar.configurations;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author tlferreira
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("validation.phone.number")
@Getter
@Setter
public class PhoneNumberValidationKeys {
    private String defaultRegion = "PT";
    private PhoneNumberUtil.PhoneNumberFormat format = PhoneNumberUtil.PhoneNumberFormat.RFC3966;
}
