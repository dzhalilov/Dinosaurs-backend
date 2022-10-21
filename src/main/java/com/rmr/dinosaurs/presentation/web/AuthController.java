package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.core.exception.ServiceException;
import com.rmr.dinosaurs.core.model.LoginRequest;
import com.rmr.dinosaurs.core.model.RefreshTokenRequest;
import com.rmr.dinosaurs.core.model.SignupRequest;
import com.rmr.dinosaurs.core.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @Operation(description = "log in")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "JWT tokens pair given",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = JwtTokenPair.class))}),
      @ApiResponse(responseCode = "404", description = "user not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/login")
  JwtTokenPair login(
      @Parameter(description = "email and password")
      @RequestBody @Valid @NotNull LoginRequest loginRequest) {
    return authService.login(loginRequest);
  }

  @Operation(description = "sign up")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "JWT tokens pair given",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = JwtTokenPair.class))}),
      @ApiResponse(responseCode = "400", description = "user already exists",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/signup")
  JwtTokenPair signup(
      @Parameter(description = "email, password, name, surname required fields")
      @RequestBody @Valid @NotNull SignupRequest signupRequest) {
    return authService.signup(signupRequest);
  }

  @Operation(description = "refresh tokens")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "JWT tokens pair given",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = JwtTokenPair.class))}),
      @ApiResponse(responseCode = "401", description = "invalid token provided",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/refresh")
  JwtTokenPair refresh(
      @Parameter(description = "refresh token")
      @RequestBody @NotNull RefreshTokenRequest refreshTokenRequest) {
    return authService.refresh(refreshTokenRequest);
  }

}
