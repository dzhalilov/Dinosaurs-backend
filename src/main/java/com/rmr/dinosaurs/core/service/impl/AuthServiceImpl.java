package com.rmr.dinosaurs.core.service.impl;

import static com.rmr.dinosaurs.core.model.Authority.ROLE_REGULAR;

import com.rmr.dinosaurs.core.auth.security.DinoAuthentication;
import com.rmr.dinosaurs.core.auth.security.DinoPrincipal;
import com.rmr.dinosaurs.core.auth.security.JwtToken;
import com.rmr.dinosaurs.core.auth.security.JwtTokenService;
import com.rmr.dinosaurs.core.model.LoginRequest;
import com.rmr.dinosaurs.core.model.SignupRequest;
import com.rmr.dinosaurs.core.model.User;
import com.rmr.dinosaurs.core.model.UserInfo;
import com.rmr.dinosaurs.core.service.AuthService;
import com.rmr.dinosaurs.infrastucture.database.UserInfoRepository;
import com.rmr.dinosaurs.infrastucture.database.UserRepository;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
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
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenService jwtTokenService;

  @Override
  public JwtToken login(@NotNull LoginRequest loginRequest) {
    var user = userRepository.findByEmailIgnoreCase(loginRequest.email())
        .orElseThrow(
            () -> new RuntimeException("user not found")
        );

    validatePasswordOrThrowException(loginRequest, user.getPassword());

    var dinoAuthentication =
        getDinoAuthenticationFromUser(user);

    return jwtTokenService.generateToken(dinoAuthentication);
  }

  @Override
  @Transactional
  public JwtToken signup(@NotNull SignupRequest signupRequest) {
    if (userRepository.findByEmailIgnoreCase(signupRequest.email()).isPresent()) {
      log.info("User {} trying to sign up, but already exists.", signupRequest.email());
      throw new RuntimeException("User with such email already exists");
    }
    var user = new User();
    user.setEmail(signupRequest.email().toLowerCase());
    user.setPassword(passwordEncoder.encode(signupRequest.password()));
    user.setRole(ROLE_REGULAR);
    user.setUserInfo(null);
    var savedUser = userRepository.save(user);
    UserInfo userInfo = new UserInfo();
    userInfo.setName(signupRequest.name());
    userInfo.setSurname(signupRequest.surname());
    userInfo.setRegisteredAt(LocalDateTime.now());
    userInfo.setIsConfirmedUser(true);
    userInfo.setUser(savedUser);
    userInfoRepository.save(userInfo);
    return jwtTokenService
        .generateToken(getDinoAuthenticationFromUser(savedUser));
  }

  private void validatePasswordOrThrowException(LoginRequest loginRequest,
      String userPassword) {
    if (!passwordEncoder.matches(loginRequest.password(), userPassword)) {
      log.info("User {} provided incorrect password", loginRequest.email());
      throw new RuntimeException("Incorrect password provided");
    }
  }

  private DinoAuthentication getDinoAuthenticationFromUser(final User user) {
    var principal = new DinoPrincipal(
        user.getId(),
        user.getEmail(),
        user.getRole()
    );
    return new DinoAuthentication(principal);
  }

}
