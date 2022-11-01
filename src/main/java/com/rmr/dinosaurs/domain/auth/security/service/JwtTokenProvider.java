package com.rmr.dinosaurs.domain.auth.security.service;

import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.domain.auth.security.model.DinoAuthentication;
import io.jsonwebtoken.Claims;

public interface JwtTokenProvider {

  JwtTokenPair generateJwtTokenPair(DinoAuthentication dinoAuthentication);

  boolean isTokenValid(String token);

  Claims parseToken(String token);

}
