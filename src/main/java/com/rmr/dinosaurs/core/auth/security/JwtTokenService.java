package com.rmr.dinosaurs.core.auth.security;

public interface JwtTokenService {

  DinoAuthentication getDinoAuthenticationByToken(String token);

  JwtTokenPair generateJwtTokenPair(DinoAuthentication dinoAuthentication);

  boolean isTokenValid(String value);

}
