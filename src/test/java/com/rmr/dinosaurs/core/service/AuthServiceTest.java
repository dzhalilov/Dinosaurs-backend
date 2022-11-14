package com.rmr.dinosaurs.core.service;

import static com.rmr.dinosaurs.domain.auth.exception.errorcode.AuthErrorCode.INCORRECT_CREDENTIALS;
import static com.rmr.dinosaurs.domain.auth.exception.errorcode.AuthErrorCode.INVALID_TOKEN_PROVIDED;
import static com.rmr.dinosaurs.domain.auth.exception.errorcode.UserErrorCode.USER_ALREADY_EXISTS;
import static com.rmr.dinosaurs.domain.auth.exception.errorcode.UserErrorCode.USER_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_REGULAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.rmr.dinosaurs.domain.auth.model.RefreshToken;
import com.rmr.dinosaurs.domain.auth.model.TempConfirmation;
import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import com.rmr.dinosaurs.domain.auth.model.requests.LoginRequest;
import com.rmr.dinosaurs.domain.auth.model.requests.RefreshTokenRequest;
import com.rmr.dinosaurs.domain.auth.model.requests.SignupRequest;
import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.domain.auth.security.model.DinoAuthentication;
import com.rmr.dinosaurs.domain.auth.security.service.JwtTokenService;
import com.rmr.dinosaurs.domain.auth.service.TempConfirmationService;
import com.rmr.dinosaurs.domain.auth.service.impl.AuthServiceImpl;
import com.rmr.dinosaurs.domain.auth.utils.converter.UserConverter;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.notification.client.NotificationClient;
import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import com.rmr.dinosaurs.infrastucture.database.auth.RefreshTokenRepository;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.infrastucture.database.userinfo.UserInfoRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  private final UserInfo testUserInfo = new UserInfo(2L, "Test", "Usersky", null, null);
  private final User testUser = new User(1L, "super@email.com", "stR4nGeRp4Ssw0rDHaHa",
      ROLE_REGULAR, true, LocalDateTime.now(ZoneOffset.UTC), false, null, null, null);
  @Mock
  private NotificationClient notificationClientMock;
  @Mock
  private UserRepository userRepositoryMock;
  @Mock
  private UserInfoRepository userInfoRepositoryMock;
  @Mock
  private RefreshTokenRepository refreshTokenRepositoryMock;
  @Mock
  private TempConfirmationService tempConfirmationServiceMock;
  @Mock
  private UserConverter userConverterMock;
  @Mock
  private PasswordEncoder passwordEncoderMock;
  @Mock
  private JwtTokenService jwtTokenServiceMock;
  @InjectMocks
  private AuthServiceImpl authService;

  @Test
  @DisplayName("should login successfully")
  void shouldLogin() {
    // given
    RefreshToken testRefreshToken = new RefreshToken(10L, "refreshToken", testUser.getId());
    given(userRepositoryMock.findByEmailIgnoreCase(anyString())).willReturn(Optional.of(testUser));
    given(jwtTokenServiceMock.generateJwtTokenPair(any(DinoAuthentication.class))).willReturn(
        new JwtTokenPair("ho", "hot"));
    given(refreshTokenRepositoryMock.findByUserId(anyLong())).willReturn(
        Optional.of(testRefreshToken));
    given(passwordEncoderMock.matches(anyString(), anyString())).willReturn(true);

    // when
    JwtTokenPair actual = authService.login(new LoginRequest("da", "net"));

    // then
    assertThat(actual).isNotNull();
    assertThat(actual.getAccessToken()).isNotNull().isNotEmpty();
    assertThat(actual.getRefreshToken()).isNotNull().isNotEmpty();

    verify(userRepositoryMock).findByEmailIgnoreCase(anyString());
    verify(passwordEncoderMock).matches(anyString(), anyString());
    verify(jwtTokenServiceMock).generateJwtTokenPair(any(DinoAuthentication.class));
    verify(refreshTokenRepositoryMock).findByUserId(anyLong());

    verifyNoMoreInteractions(userRepositoryMock, refreshTokenRepositoryMock);
  }

  @Test
  @DisplayName("login throw exception on invalid creds provided")
  void shouldNotLoginWithIncorrectCredentialsException() {
    // given
    LoginRequest testLoginRequest = new LoginRequest(testUser.getEmail(), testUser.getPassword());
    given(userRepositoryMock.findByEmailIgnoreCase(anyString())).willReturn(Optional.of(testUser));
    given(passwordEncoderMock.matches(anyString(), anyString())).willReturn(false);

    // when
    assertThatThrownBy(() -> authService.login(testLoginRequest))
        // then
        .isInstanceOf(ServiceException.class)
        .hasFieldOrPropertyWithValue("errorCode", INCORRECT_CREDENTIALS).hasNoCause();

    verify(userRepositoryMock).findByEmailIgnoreCase(anyString());
    verify(passwordEncoderMock).matches(anyString(), anyString());

    verifyNoMoreInteractions(userRepositoryMock, userInfoRepositoryMock);
    verifyNoInteractions(refreshTokenRepositoryMock);
  }

  @Test
  @DisplayName("login throw exception on not existing user")
  void shouldNotLoginWithUserNotFoundException() {
    // given
    LoginRequest testLoginRequest = new LoginRequest(testUser.getEmail(), testUser.getPassword());
    given(userRepositoryMock.findByEmailIgnoreCase(anyString())).willReturn(Optional.empty());

    // when
    assertThatThrownBy(() -> authService.login(testLoginRequest))
        // then
        .isInstanceOf(ServiceException.class)
        .hasFieldOrPropertyWithValue("errorCode", USER_NOT_FOUND).hasNoCause();

    verify(userRepositoryMock).findByEmailIgnoreCase(anyString());

    verifyNoMoreInteractions(userRepositoryMock);
    verifyNoInteractions(userInfoRepositoryMock, refreshTokenRepositoryMock, passwordEncoderMock,
        jwtTokenServiceMock);
  }

  @Test
  @DisplayName("sign up successfully")
  void shouldSignup() {
    // given
    UserDto testUserDto = UserDto.builder()
        .id(1L)
        .email(testUser.getEmail())
        .role(ROLE_REGULAR)
        .isConfirmed(true)
        .registeredAt(LocalDateTime.now(ZoneOffset.UTC))
        .isArchived(false)
        .archivedAt(null)
        .build();
    given(userRepositoryMock.findByEmailIgnoreCase(anyString())).willReturn(Optional.empty());
    given(userRepositoryMock.saveAndFlush(any(User.class))).willReturn(testUser);
    given(userInfoRepositoryMock.saveAndFlush(any(UserInfo.class))).willReturn(testUserInfo);
    given(passwordEncoderMock.encode(testUser.getPassword())).willReturn("p4sswOrD");
    given(userConverterMock.toUserDto(any(User.class))).willReturn(testUserDto);
    given(tempConfirmationServiceMock.createTempConfirmationFor(any(User.class)))
        .willReturn(new TempConfirmation(
            UUID.randomUUID(), LocalDateTime.now(ZoneOffset.UTC), testUser));

    // when
    var actual = authService.signup(
        new SignupRequest(testUser.getEmail(), testUser.getPassword(), "testName", "testSurname"));

    // then
    assertThat(actual).isNotNull();
    assertThat(actual.getEmail()).isNotNull().isEqualTo(testUser.getEmail());

    verify(userRepositoryMock).findByEmailIgnoreCase(anyString());
    verify(userRepositoryMock).saveAndFlush(any(User.class));
    verify(userInfoRepositoryMock).saveAndFlush(any(UserInfo.class));
    verify(passwordEncoderMock).encode(anyString());
    verify(tempConfirmationServiceMock).createTempConfirmationFor(any(User.class));
    verify(notificationClientMock).emailConfirmationNotification(any(UUID.class), anyString());

    verifyNoMoreInteractions(userRepositoryMock, userInfoRepositoryMock, refreshTokenRepositoryMock,
        jwtTokenServiceMock, passwordEncoderMock, notificationClientMock);
  }

  @Test
  @DisplayName("signup throw user already exists exception")
  void shouldNotSignupWithUserAlreadyExistsException() {
    // given
    SignupRequest testSignupRequest = new SignupRequest(testUser.getEmail(), testUser.getPassword(),
        "Name", "Surname");
    given(userRepositoryMock.findByEmailIgnoreCase(anyString())).willReturn(Optional.of(testUser));

    // when
    assertThatThrownBy(() -> authService.signup(testSignupRequest))
        // then
        .isInstanceOf(ServiceException.class)
        .hasFieldOrPropertyWithValue("errorCode", USER_ALREADY_EXISTS).hasNoCause();

    verify(userRepositoryMock).findByEmailIgnoreCase(anyString());

    verifyNoMoreInteractions(userRepositoryMock);
    verifyNoInteractions(userInfoRepositoryMock, refreshTokenRepositoryMock, jwtTokenServiceMock,
        passwordEncoderMock);
  }

  @Test
  @DisplayName("refresh token successfully")
  void shouldRefreshToken() {
    // given
    RefreshToken testRefreshToken = new RefreshToken(10L, "refreshToken", testUser.getId());
    JwtTokenPair testJwtTokenPair = new JwtTokenPair("accessToken", testRefreshToken.getValue());
    given(refreshTokenRepositoryMock.findByValue(anyString())).willReturn(
        Optional.of(testRefreshToken));
    given(userRepositoryMock.findByIdAndIsConfirmedTrueAndIsArchivedFalse(anyLong()))
        .willReturn(Optional.of(testUser));
    given(jwtTokenServiceMock.isTokenValid(anyString())).willReturn(true);
    given(jwtTokenServiceMock.generateJwtTokenPair(any(DinoAuthentication.class))).willReturn(
        testJwtTokenPair);

    // when
    JwtTokenPair actual = authService.refresh(new RefreshTokenRequest("old.refresh.token"));

    // then
    assertThat(actual).isNotNull();
    assertThat(actual.getAccessToken()).isNotNull().isNotEmpty();
    assertThat(actual.getRefreshToken()).isNotNull().isNotEmpty();

    verify(refreshTokenRepositoryMock).findByValue(anyString());
    verify(userRepositoryMock).findByIdAndIsConfirmedTrueAndIsArchivedFalse(anyLong());
    verify(jwtTokenServiceMock).generateJwtTokenPair(any(DinoAuthentication.class));
    verify(jwtTokenServiceMock).isTokenValid(anyString());

    verifyNoMoreInteractions(userRepositoryMock, refreshTokenRepositoryMock, jwtTokenServiceMock);
    verifyNoInteractions(userInfoRepositoryMock, passwordEncoderMock);
  }

  @Test
  @DisplayName("refresh token throw invalid token exception")
  void shouldNotRefreshTokenWithInvalidTokenProvidedException() {
    // given
    RefreshToken testRefreshToken = new RefreshToken(10L, "refreshToken", testUser.getId());
    RefreshTokenRequest testRefreshTokenRequest = new RefreshTokenRequest("old.refresh.token");
    given(refreshTokenRepositoryMock.findByValue(anyString())).willReturn(Optional.empty());

    // when
    assertThatThrownBy(() -> authService.refresh(testRefreshTokenRequest))
        // then
        .isInstanceOf(ServiceException.class)
        .hasFieldOrPropertyWithValue("errorCode", INVALID_TOKEN_PROVIDED).hasNoCause();

    // then
    verify(refreshTokenRepositoryMock).findByValue(anyString());

    verifyNoMoreInteractions(refreshTokenRepositoryMock);
    verifyNoInteractions(userRepositoryMock, userInfoRepositoryMock, jwtTokenServiceMock,
        passwordEncoderMock);
  }

  @Test
  @DisplayName("refresh token throw invalid token exception on user not found")
  void shouldNotRefreshTokenWithInvalidTokenProvidedExceptionWhenUserNotFound() {
    // given
    RefreshToken testRefreshToken = new RefreshToken(10L, "refreshToken", testUser.getId());
    RefreshTokenRequest testRefreshTokenRequest = new RefreshTokenRequest("old.refresh.token");
    given(refreshTokenRepositoryMock.findByValue(anyString())).willReturn(
        Optional.of(testRefreshToken));
    given(userRepositoryMock.findByIdAndIsConfirmedTrueAndIsArchivedFalse(anyLong())).willReturn(
        Optional.empty());

    // when
    assertThatThrownBy(() -> authService.refresh(testRefreshTokenRequest))
        // then
        .isInstanceOf(ServiceException.class)
        .hasFieldOrPropertyWithValue("errorCode", INVALID_TOKEN_PROVIDED).hasNoCause();

    // then
    verify(refreshTokenRepositoryMock).findByValue(anyString());
    verify(userRepositoryMock).findByIdAndIsConfirmedTrueAndIsArchivedFalse(anyLong());

    verifyNoMoreInteractions(refreshTokenRepositoryMock, userRepositoryMock);
    verifyNoInteractions(userInfoRepositoryMock, jwtTokenServiceMock, passwordEncoderMock);
  }

  @Test
  @DisplayName("refresh token throw invalid token exception on invalid exception value")
  void shouldNotRefreshTokenWithInvalidTokenProvidedExceptionWhenTokenExpired() {
    // given
    RefreshToken testRefreshToken = new RefreshToken(10L, "refreshToken", testUser.getId());
    RefreshTokenRequest testRefreshTokenRequest = new RefreshTokenRequest("old.refresh.token");
    given(refreshTokenRepositoryMock.findByValue(anyString())).willReturn(
        Optional.of(testRefreshToken));
    given(userRepositoryMock.findByIdAndIsConfirmedTrueAndIsArchivedFalse(anyLong()))
        .willReturn(Optional.of(testUser));
    given(jwtTokenServiceMock.isTokenValid(anyString())).willReturn(false);

    // when
    assertThatThrownBy(() -> authService.refresh(testRefreshTokenRequest))
        // then
        .isInstanceOf(ServiceException.class)
        .hasFieldOrPropertyWithValue("errorCode", INVALID_TOKEN_PROVIDED).hasNoCause();

    // then
    verify(refreshTokenRepositoryMock).findByValue(anyString());
    verify(userRepositoryMock).findByIdAndIsConfirmedTrueAndIsArchivedFalse(anyLong());
    verify(jwtTokenServiceMock).isTokenValid(anyString());

    verifyNoMoreInteractions(refreshTokenRepositoryMock, userRepositoryMock, jwtTokenServiceMock);
    verifyNoInteractions(userInfoRepositoryMock, passwordEncoderMock);
  }

  @Test
  @DisplayName("confirm email success")
  void shouldConfirmEmail() {
    // given
    RefreshToken testRefreshToken = new RefreshToken(10L, "refreshToken", testUser.getId());
    JwtTokenPair testJwtTokenPair = new JwtTokenPair("accessToken", testRefreshToken.getValue());
    var tmpConfirmation = new TempConfirmation(UUID.randomUUID(),
        LocalDateTime.now(ZoneOffset.UTC).minus(5, ChronoUnit.MINUTES), testUser);
    given(tempConfirmationServiceMock.validateTempConfirmationByCodeAndDelete(any(UUID.class)))
        .willReturn(Optional.of(tmpConfirmation));
    given(userRepositoryMock.findById(anyLong())).willReturn(Optional.of(testUser));
    given(userRepositoryMock.saveAndFlush(any(User.class))).willReturn(testUser);
    given(jwtTokenServiceMock.generateJwtTokenPair(any(DinoAuthentication.class))).willReturn(
        testJwtTokenPair);
    given(refreshTokenRepositoryMock.findByUserId(testUser.getId()))
        .willReturn(Optional.of(testRefreshToken));

    // when
    JwtTokenPair actual = authService.confirmEmail(tmpConfirmation.getId());

    // then
    assertThat(actual).isNotNull();
    assertThat(actual.getAccessToken()).isNotNull().isNotEmpty();
    assertThat(actual.getRefreshToken()).isNotNull().isNotEmpty();

    verify(tempConfirmationServiceMock).validateTempConfirmationByCodeAndDelete(
        tmpConfirmation.getId());
    verify(userRepositoryMock).findById(anyLong());
    verify(userRepositoryMock).saveAndFlush(any(User.class));
    verify(notificationClientMock).registrationWelcomeNotification(testUser.getEmail());
    verify(jwtTokenServiceMock).generateJwtTokenPair(any(DinoAuthentication.class));
    verify(refreshTokenRepositoryMock).findByUserId(testUser.getId());

    verifyNoMoreInteractions(userRepositoryMock, refreshTokenRepositoryMock, jwtTokenServiceMock);
    verifyNoInteractions(userInfoRepositoryMock, passwordEncoderMock);
  }

}
