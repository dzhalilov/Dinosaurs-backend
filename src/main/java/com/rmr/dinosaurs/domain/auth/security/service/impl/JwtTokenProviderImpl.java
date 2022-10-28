package com.rmr.dinosaurs.domain.auth.security.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.domain.auth.security.model.DinoAuthentication;
import com.rmr.dinosaurs.domain.auth.security.model.dto.DinoAuthenticationDto;
import com.rmr.dinosaurs.domain.auth.security.service.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${jwt.ttl}")
  private Long ttl;
  @Value("${jwt.secretKey}")
  private String secretKey;
  private Key hmacKey;

  @PostConstruct
  private void initKey() {
    hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey),
        SignatureAlgorithm.HS512.getJcaName());
  }

  @Override
  public JwtTokenPair generateJwtTokenPair(DinoAuthentication dinoAuthentication) {
    var dinoAuthenticationDto = new DinoAuthenticationDto(
        dinoAuthentication.getId(),
        dinoAuthentication.getEmail(),
        dinoAuthentication.getRole()
    );
    var now = Instant.now();
    var dinoAuthDtoAsString = convertToJson(dinoAuthenticationDto);
    var accessToken = Jwts.builder()
        .setSubject(dinoAuthDtoAsString)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(ttl)))
        .signWith(hmacKey)
        .compact();
    var refreshTokenValue = Jwts.builder()
        .setSubject(dinoAuthentication.getId().toString())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plus(ttl, ChronoUnit.HOURS)))
        .signWith(hmacKey)
        .compact();
    return new JwtTokenPair(accessToken, refreshTokenValue);
  }

  @Override
  public boolean isTokenValid(String token) {
    return parseToken(token).getExpiration().after(Date.from(Instant.now()));
  }

  @Override
  public Claims parseToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(hmacKey)
        .build()
        .parseClaimsJws(token).getBody();
  }

  private String convertToJson(DinoAuthenticationDto dinoAuthenticationDto) {
    try {
      return objectMapper.writeValueAsString(dinoAuthenticationDto);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("can not convert dino authentication dto to json");
    }
  }

}
