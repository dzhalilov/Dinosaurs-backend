package com.rmr.dinosaurs.domain.auth.security.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.domain.auth.security.model.DinoAuthentication;
import com.rmr.dinosaurs.domain.auth.security.model.DinoPrincipal;
import com.rmr.dinosaurs.domain.auth.security.model.dto.DinoAuthenticationDto;
import com.rmr.dinosaurs.domain.auth.security.service.JwtTokenProvider;
import com.rmr.dinosaurs.domain.auth.security.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public DinoAuthentication getDinoAuthenticationByToken(String token) {
    Claims claims = jwtTokenProvider.parseToken(token);

    DinoAuthenticationDto dinoAuthDto = parseDinoAuthentication(
        claims.getSubject());

    DinoPrincipal principal = new DinoPrincipal(
        dinoAuthDto.getId(),
        dinoAuthDto.getEmail(),
        dinoAuthDto.getRole()
    );

    return new DinoAuthentication(principal, token);
  }

  @Override
  public JwtTokenPair generateJwtTokenPair(DinoAuthentication dinoAuthentication) {
    return jwtTokenProvider.generateJwtTokenPair(dinoAuthentication);
  }

  @Override
  public boolean isTokenValid(String value) {
    return jwtTokenProvider.isTokenValid(value);
  }

  private DinoAuthenticationDto parseDinoAuthentication(String subject) {
    try {
      return objectMapper.readValue(subject, DinoAuthenticationDto.class);
    } catch (Exception e) {
      throw new RuntimeException("authentication parsing exception");
    }
  }

}
