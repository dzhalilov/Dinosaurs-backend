package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.core.model.LoginRequest;
import com.rmr.dinosaurs.core.model.RefreshTokenRequest;
import com.rmr.dinosaurs.core.model.SignupRequest;
import com.rmr.dinosaurs.core.service.AuthService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
  JwtTokenPair login(@RequestBody @Valid @NotNull LoginRequest loginRequest) {
    return authService.login(loginRequest);
  }

  @PostMapping("/signup")
  JwtTokenPair signup(@RequestBody @Valid @NotNull SignupRequest signupRequest) {
    return authService.signup(signupRequest);
  }

  @PostMapping("/refresh")
  JwtTokenPair refresh(@RequestBody @NotNull RefreshTokenRequest refreshTokenRequest) {
    return authService.refresh(refreshTokenRequest);
  }

}
