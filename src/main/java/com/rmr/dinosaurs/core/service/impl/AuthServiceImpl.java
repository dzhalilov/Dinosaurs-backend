package com.rmr.dinosaurs.core.service.impl;

import static com.rmr.dinosaurs.core.exception.errorcode.AuthErrorCode.INCORRECT_CREDENTIALS;
import static com.rmr.dinosaurs.core.exception.errorcode.AuthErrorCode.INVALID_TOKEN_PROVIDED;
import static com.rmr.dinosaurs.core.exception.errorcode.UserErrorCode.USER_ALREADY_EXISTS;
import static com.rmr.dinosaurs.core.exception.errorcode.UserErrorCode.USER_NOT_FOUND;
import static com.rmr.dinosaurs.core.model.Authority.ROLE_REGULAR;

import com.rmr.dinosaurs.core.auth.security.DinoAuthentication;
import com.rmr.dinosaurs.core.auth.security.DinoPrincipal;
import com.rmr.dinosaurs.core.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.core.auth.security.JwtTokenService;
import com.rmr.dinosaurs.core.exception.ServiceException;
import com.rmr.dinosaurs.core.model.LoginRequest;
import com.rmr.dinosaurs.core.model.RefreshToken;
import com.rmr.dinosaurs.core.model.RefreshTokenRequest;
import com.rmr.dinosaurs.core.model.SignupRequest;
import com.rmr.dinosaurs.core.model.User;
import com.rmr.dinosaurs.core.model.UserInfo;
import com.rmr.dinosaurs.core.service.AuthService;
import com.rmr.dinosaurs.infrastucture.database.RefreshTokenRepository;
import com.rmr.dinosaurs.infrastucture.database.UserInfoRepository;
import com.rmr.dinosaurs.infrastucture.database.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final UserInfoRepository userInfoRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenService jwtTokenService;

  @Override
  public JwtTokenPair login(LoginRequest loginRequest) {
    var user = userRepository.findByEmailIgnoreCase(loginRequest.email())
        .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));

    validatePasswordOrThrowException(loginRequest, user.getPassword());

    return generateTokenPairAndSaveRefreshToken(user);
  }

  @Override
  public JwtTokenPair signup(SignupRequest signupRequest) {
    if (userRepository.findByEmailIgnoreCase(signupRequest.email()).isPresent()) {
      log.info("User {} trying to sign up, but already exists.", signupRequest.email());
      throw new ServiceException(USER_ALREADY_EXISTS);
    }
    var user = createAndSaveUserFromSignupRequest(signupRequest);

    return generateTokenPairAndSaveRefreshToken(user);
  }

  @Override
  public JwtTokenPair refresh(RefreshTokenRequest refreshTokenRequest) {
    RefreshToken savedRefreshToken = refreshTokenRepository.findByValue(
            refreshTokenRequest.refreshToken())
        .orElseThrow(() -> new ServiceException(INVALID_TOKEN_PROVIDED));
    var user = userRepository.findById(savedRefreshToken.getUserId())
        .orElseThrow(() -> new ServiceException(INVALID_TOKEN_PROVIDED));

    checkTokenIsValid(savedRefreshToken.getValue());

    JwtTokenPair jwtTokenPair = jwtTokenService.generateJwtTokenPair(
        getDinoAuthenticationFromUser(user));
    savedRefreshToken.setValue(jwtTokenPair.getRefreshToken());
    return jwtTokenPair;
  }

  private void checkTokenIsValid(String value) {
    if (!jwtTokenService.isTokenValid(value)) {
      throw new ServiceException(INVALID_TOKEN_PROVIDED);
    }
  }

  private JwtTokenPair generateTokenPairAndSaveRefreshToken(User user) {
    var jwtTokenPair = jwtTokenService.generateJwtTokenPair(getDinoAuthenticationFromUser(user));

    refreshTokenRepository.findByUserId(user.getId())
        .ifPresentOrElse(
            refreshToken -> refreshToken.setValue(jwtTokenPair.getRefreshToken()),
            () -> refreshTokenRepository.save(
                new RefreshToken(null, jwtTokenPair.getRefreshToken(), user.getId())));
    return jwtTokenPair;
  }

  private User createAndSaveUserFromSignupRequest(SignupRequest signupRequest) {
    var user = new User(null, signupRequest.email().toLowerCase(),
        passwordEncoder.encode(signupRequest.password()), ROLE_REGULAR, null);
    var savedUser = userRepository.save(user);
    UserInfo userInfo = new UserInfo(null, signupRequest.name(), signupRequest.surname(),
        LocalDateTime.now(), true, null, savedUser, null);
    userInfoRepository.save(userInfo);
    return savedUser;
  }

  private void validatePasswordOrThrowException(LoginRequest loginRequest, String userPassword) {
    if (!passwordEncoder.matches(loginRequest.password(), userPassword)) {
      log.info("User {} provided incorrect password", loginRequest.email());
      throw new ServiceException(INCORRECT_CREDENTIALS);
    }
  }

  private DinoAuthentication getDinoAuthenticationFromUser(final User user) {
    return new DinoAuthentication(new DinoPrincipal(user.getId(), user.getEmail(), user.getRole()));
  }

}
