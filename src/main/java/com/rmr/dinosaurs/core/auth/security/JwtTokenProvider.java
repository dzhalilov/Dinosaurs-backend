package com.rmr.dinosaurs.core.auth.security;

import io.jsonwebtoken.Claims;

public interface JwtTokenProvider {

  JwtTokenPair generateJwtTokenPair(DinoAuthentication dinoAuthentication);

  boolean isTokenValid(String token);

  Claims parseToken(String token);

}
