package com.rmr.dinosaurs.core.configuration;

import com.rmr.dinosaurs.core.auth.security.JwtTokenFilter;
import com.rmr.dinosaurs.core.auth.security.JwtTokenProvider;
import com.rmr.dinosaurs.core.auth.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtTokenService jwtTokenService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .addFilterBefore(getJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .anyRequest().permitAll()

        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
        .formLogin().disable()
        .logout().disable()

        .csrf().disable()
        .httpBasic().disable()
        .build();
  }

  @Bean
  public JwtTokenFilter getJwtTokenFilter() {
    return new JwtTokenFilter(jwtTokenProvider, jwtTokenService);
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder(BCryptVersion.$2Y, 12);
  }

}
