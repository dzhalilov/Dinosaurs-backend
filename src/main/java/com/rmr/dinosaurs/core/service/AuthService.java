package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.core.model.LoginRequest;
import com.rmr.dinosaurs.core.model.RefreshTokenRequest;
import com.rmr.dinosaurs.core.model.SignupRequest;

public interface AuthService {

  /**
   * log in
   *
   * @param loginRequest email and password provided by user
   * @return generated jwt tokens pair
   */
  JwtTokenPair login(LoginRequest loginRequest);

  /**
   * sign up
   *
   * @param signupRequest email, password, name, surname
   * @return generated jwt tokens pair
   */
  JwtTokenPair signup(SignupRequest signupRequest);

  /**
   * refreshing tokens
   *
   * @param refreshTokenRequest refresh token value
   * @return updated jwt tokens pair
   */
  JwtTokenPair refresh(RefreshTokenRequest refreshTokenRequest);

}
