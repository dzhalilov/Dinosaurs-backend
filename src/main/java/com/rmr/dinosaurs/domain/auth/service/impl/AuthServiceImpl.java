package com.rmr.dinosaurs.domain.auth.service.impl;

import static com.rmr.dinosaurs.domain.auth.exception.errorcode.AuthErrorCode.USER_NOT_CONFIRMED;
import static com.rmr.dinosaurs.domain.auth.exception.errorcode.TempConfirmationErrorCode.INVALID_CONFIRMATION_CODE;
import static com.rmr.dinosaurs.domain.auth.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

import com.rmr.dinosaurs.domain.auth.exception.errorcode.AuthErrorCode;
import com.rmr.dinosaurs.domain.auth.exception.errorcode.UserErrorCode;
import com.rmr.dinosaurs.domain.auth.model.RefreshToken;
import com.rmr.dinosaurs.domain.auth.model.TempConfirmation;
import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import com.rmr.dinosaurs.domain.auth.model.requests.LoginRequest;
import com.rmr.dinosaurs.domain.auth.model.requests.RefreshTokenRequest;
import com.rmr.dinosaurs.domain.auth.model.requests.SignupRequest;
import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.domain.auth.security.model.DinoAuthentication;
import com.rmr.dinosaurs.domain.auth.security.model.DinoPrincipal;
import com.rmr.dinosaurs.domain.auth.security.service.JwtTokenService;
import com.rmr.dinosaurs.domain.auth.service.AuthService;
import com.rmr.dinosaurs.domain.auth.service.TempConfirmationService;
import com.rmr.dinosaurs.domain.auth.utils.converter.UserConverter;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.Authority;
import com.rmr.dinosaurs.domain.notification.client.NotificationClient;
import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import com.rmr.dinosaurs.infrastucture.database.auth.RefreshTokenRepository;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.infrastucture.database.userinfo.UserInfoRepository;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final UserInfoRepository userInfoRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  private final UserConverter userConverter;
  private final PasswordEncoder passwordEncoder;

  private final JwtTokenService jwtTokenService;
  private final NotificationClient notificationClient;
  private final TempConfirmationService tempConfirmationService;


  @Transactional
  @Override
  public JwtTokenPair login(LoginRequest loginRequest) {
    var user = userRepository.findByEmailIgnoreCase(loginRequest.email())
        .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    if (Objects.isNull(user.getIsConfirmed()) || Boolean.TRUE.equals(!user.getIsConfirmed())) {
      throw new ServiceException(USER_NOT_CONFIRMED);
    }
    validatePasswordOrThrowException(loginRequest, user.getPassword());

    return generateTokenPairAndSaveRefreshToken(user);
  }

  @Override
  public UserDto signup(SignupRequest signupRequest) {
    if (userRepository.findByEmailIgnoreCase(signupRequest.email()).isPresent()) {
      log.info("User {} trying to sign up, but already exists.", signupRequest.email());
      throw new ServiceException(UserErrorCode.USER_ALREADY_EXISTS);
    }
    var user = createAndSaveUserFromSignupRequest(signupRequest);
    var tempConfirmation = tempConfirmationService.createTempConfirmationFor(user);
    notificationClient.emailConfirmationNotification(tempConfirmation.getId(), user.getEmail());
    return userConverter.toUserDto(user);
  }

  @Override
  public JwtTokenPair confirmEmail(UUID uuid) {
    TempConfirmation tmpConfirmation = tempConfirmationService
        .validateTempConfirmationByCodeAndDelete(uuid)
        .orElseThrow(() -> new ServiceException(INVALID_CONFIRMATION_CODE));
    var user = userRepository.findById(tmpConfirmation.getUser().getId())
        .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    user.setIsConfirmed(true);
    userRepository.saveAndFlush(user);
    notificationClient.registrationWelcomeNotification(user.getEmail());
    return generateTokenPairAndSaveRefreshToken(user);
  }

  @Override
  public JwtTokenPair refresh(RefreshTokenRequest refreshTokenRequest) {
    RefreshToken savedRefreshToken = refreshTokenRepository.findByValue(
            refreshTokenRequest.refreshToken())
        .orElseThrow(() -> new ServiceException(AuthErrorCode.INVALID_TOKEN_PROVIDED));
    var user = userRepository.findById(savedRefreshToken.getUserId())
        .orElseThrow(() -> new ServiceException(AuthErrorCode.INVALID_TOKEN_PROVIDED));

    checkTokenIsValid(savedRefreshToken.getValue());

    JwtTokenPair jwtTokenPair = jwtTokenService.generateJwtTokenPair(
        getDinoAuthenticationFromUser(user));
    savedRefreshToken.setValue(jwtTokenPair.getRefreshToken());
    return jwtTokenPair;
  }

  private void checkTokenIsValid(String value) {
    if (!jwtTokenService.isTokenValid(value)) {
      throw new ServiceException(AuthErrorCode.INVALID_TOKEN_PROVIDED);
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
        passwordEncoder.encode(signupRequest.password()), Authority.ROLE_REGULAR,
        false, LocalDateTime.now(), false, null, null, null);
    var savedUser = userRepository.saveAndFlush(user);
    UserInfo userInfo = new UserInfo(null, signupRequest.name(), signupRequest.surname(),
        savedUser, null);
    userInfoRepository.saveAndFlush(userInfo);
    return savedUser;
  }

  private void validatePasswordOrThrowException(LoginRequest loginRequest, String userPassword) {
    if (!passwordEncoder.matches(loginRequest.password(), userPassword)) {
      log.info("User {} provided incorrect password", loginRequest.email());
      throw new ServiceException(AuthErrorCode.INCORRECT_CREDENTIALS);
    }
  }

  private DinoAuthentication getDinoAuthenticationFromUser(final User user) {
    return new DinoAuthentication(new DinoPrincipal(user.getId(), user.getEmail(), user.getRole()));
  }

}
