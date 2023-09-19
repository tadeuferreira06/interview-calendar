package com.tamanna.challenge.interview.calendar.configurations;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author tlferreira
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfiguration {

    public static final String CANDIDATE_ROLE = "CANDIDATE";
    public static final String INTERVIEWER_ROLE = "INTERVIEWER";

    public static final String HAS_CANDIDATE_ROLE = "hasRole('" + CANDIDATE_ROLE + "')";
    public static final String HAS_INTERVIEWER_ROLE = "hasRole('" + INTERVIEWER_ROLE + "')";
    public static final String HAS_INTERVIEWER_CANDIDATE_ROLE = HAS_INTERVIEWER_ROLE + " or " + HAS_CANDIDATE_ROLE;

    private static final String[] SWAGGER_WHITELIST = new String[]{"/api-docs", "/api-docs.*", "/api-docs/*",
            "/v3/api-docs.*", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger.html"};
    private static final String[] H_2_WHITELIST = new String[]{"/h2-console", "/h2-console/*", "/h2-console/**"};

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        //missing encryption - POC
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles(INTERVIEWER_ROLE, CANDIDATE_ROLE)
                .build();
        UserDetails candidate = User.withDefaultPasswordEncoder()
                .username("candidate")
                .password("candidate")
                .roles(CANDIDATE_ROLE)
                .build();

        UserDetails interviewer = User.withDefaultPasswordEncoder()
                .username("interviewer")
                .password("interviewer")
                .roles(INTERVIEWER_ROLE)
                .build();

        return new InMemoryUserDetailsManager(admin, candidate, interviewer);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, APIAuthenticationEntryPoint authenticationEntryPoint) throws Exception {

        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(SWAGGER_WHITELIST).permitAll()
                .antMatchers(H_2_WHITELIST).permitAll()
                .antMatchers(HttpMethod.GET).authenticated()
                .antMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.PUT).authenticated()
                .antMatchers(HttpMethod.PATCH).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated();
    
        http.csrf().ignoringAntMatchers(H_2_WHITELIST);
        http.headers().frameOptions().disable();

        http.httpBasic();

        return http.build();
    }
}
