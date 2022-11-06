package com.rmr.dinosaurs.presentation.web.auth;

import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import com.rmr.dinosaurs.domain.auth.model.requests.LoginRequest;
import com.rmr.dinosaurs.domain.auth.model.requests.RefreshTokenRequest;
import com.rmr.dinosaurs.domain.auth.model.requests.SignupRequest;
import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.domain.auth.service.AuthService;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth controller")
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "Log in")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "JWT tokens pair given",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = JwtTokenPair.class))}),
      @ApiResponse(responseCode = "404", description = "user not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "403", description = "email not confirmed",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/login")
  JwtTokenPair login(
      @Parameter(description = "email and password")
      @RequestBody @Valid @NotNull LoginRequest loginRequest) {
    return authService.login(loginRequest);
  }

  @Operation(summary = "Sign up")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "UserDto given",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class))}),
      @ApiResponse(responseCode = "400", description = "user already exists",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/signup")
  UserDto signup(
      @Parameter(description = "email, password, name, surname required fields")
      @RequestBody @Valid @NotNull SignupRequest signupRequest) {
    return authService.signup(signupRequest);
  }

  @Operation(summary = "Confirm email")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "JWT tokens pair given",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = JwtTokenPair.class))}),
      @ApiResponse(responseCode = "400", description = "invalid confirmation code",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/signup/{confirmCode}")
  JwtTokenPair confirmEmail(
      @Parameter(description = "confirmation code")
      @PathVariable @NotNull UUID confirmCode) {
    return authService.confirmEmail(confirmCode);
  }

  @Operation(summary = "Refresh tokens")
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
