package com.rmr.dinosaurs.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
  private static final String[] AUTH_WHITELIST = {
    "/swagger-ui.html",
    "/swagger-ui/**",
    "/swagger-resources/**",
    "/v2/api-docs/**",
    "/v3/api-docs/**",
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .antMatchers(AUTH_WHITELIST).permitAll()
      .antMatchers("/**").authenticated();
    return http.build();
  }
}
