package com.rmr.dinosaurs.core.auth.security;

public interface JwtTokenService {

  DinoAuthentication getDinoAuthenticationByToken(String token);

  JwtToken generateToken(DinoAuthentication dinoAuthentication);

}
