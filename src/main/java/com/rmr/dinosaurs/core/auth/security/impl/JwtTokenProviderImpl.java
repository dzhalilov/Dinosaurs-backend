package com.rmr.dinosaurs.core.auth.security.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmr.dinosaurs.core.auth.security.DinoAuthentication;
import com.rmr.dinosaurs.core.auth.security.DinoAuthenticationDto;
import com.rmr.dinosaurs.core.auth.security.JwtToken;
import com.rmr.dinosaurs.core.auth.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.Instant;
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
  public JwtToken generateJwtTokenPair(DinoAuthentication dinoAuthentication) {
    var dinoAuthenticationDto = new DinoAuthenticationDto(
        dinoAuthentication.getId(),
        dinoAuthentication.getEmail(),
        dinoAuthentication.getRole()
    );
    var now = Instant.now();
    var token = Jwts.builder()
        .setSubject(convertToJson(dinoAuthenticationDto))
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(ttl)))
        .signWith(hmacKey)
        .compact();
    return new JwtToken(token);
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
