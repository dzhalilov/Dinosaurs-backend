package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.auth.security.JwtToken;
import com.rmr.dinosaurs.core.model.LoginRequest;
import com.rmr.dinosaurs.core.model.SignupRequest;

public interface AuthService {

  /**
   * log in
   * @param loginRequest email and password provided by user
   * @return generated jwt token
   */
  JwtToken login(LoginRequest loginRequest);

  /**
   * sign up
   * @param signupRequest email, password, name, surname
   * @return generated jwt token
   */
  JwtToken signup(SignupRequest signupRequest);

}
