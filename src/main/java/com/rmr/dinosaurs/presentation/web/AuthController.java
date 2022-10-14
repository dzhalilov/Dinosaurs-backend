package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.auth.security.JwtToken;
import com.rmr.dinosaurs.core.model.LoginRequest;
import com.rmr.dinosaurs.core.model.SignupRequest;
import com.rmr.dinosaurs.core.service.AuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  JwtToken login(@RequestBody @Valid LoginRequest loginRequest) {
    return authService.login(loginRequest);
  }

  @PostMapping("/signup")
  JwtToken signup(@RequestBody @Valid SignupRequest signupRequest) {
    return authService.signup(signupRequest);
  }

}
