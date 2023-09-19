package com.tamanna.challenge.interview.calendar.configurations;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.tamanna.challenge.interview.calendar.configurations.OpenApiConfiguration.SECURITY_SCHEMA_NAME;

/**
 * @author tlferreira
 */
@Log4j2
@Configuration
@SecurityScheme(name = SECURITY_SCHEMA_NAME, scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class OpenApiConfiguration {
    public static final String SECURITY_SCHEMA_NAME = "pocSecurity";
    @Bean
    public OpenAPI openAPI(BuildProperties buildProperties) {
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(new Info()
                .title(buildProperties.getArtifact())
                .description(buildProperties.getName())
                .version(buildProperties.getVersion())
        );
        return openAPI;
    }
}
