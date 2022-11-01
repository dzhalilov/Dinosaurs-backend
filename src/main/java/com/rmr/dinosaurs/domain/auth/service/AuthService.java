package com.rmr.dinosaurs.domain.auth.service;

import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import com.rmr.dinosaurs.domain.auth.model.requests.LoginRequest;
import com.rmr.dinosaurs.domain.auth.model.requests.RefreshTokenRequest;
import com.rmr.dinosaurs.domain.auth.model.requests.SignupRequest;
import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import java.util.UUID;

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
   * @return registered not confirmed user as UserDto
   */
  UserDto signup(SignupRequest signupRequest);

  /**
   * confirm email
   *
   * @param uuid confirmation code
   * @return generated jwt tokens pair
   */
  JwtTokenPair confirmEmail(UUID uuid);

  /**
   * refreshing tokens
   *
   * @param refreshTokenRequest refresh token value
   * @return updated jwt tokens pair
   */
  JwtTokenPair refresh(RefreshTokenRequest refreshTokenRequest);

}
