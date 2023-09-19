package com.tamanna.challenge.interview.calendar.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tlferreira
 */
@Log4j2
@Configuration
public class OpenApiConfiguration {
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
