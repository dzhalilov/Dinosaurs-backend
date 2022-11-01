package com.rmr.dinosaurs.domain.auth.security.service;

import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.domain.auth.security.model.DinoAuthentication;

public interface JwtTokenService {

  DinoAuthentication getDinoAuthenticationByToken(String token);

  JwtTokenPair generateJwtTokenPair(DinoAuthentication dinoAuthentication);

  boolean isTokenValid(String value);

}
