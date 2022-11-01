package com.rmr.dinosaurs.domain.auth.service.impl;

import com.rmr.dinosaurs.domain.auth.exception.errorcode.UserErrorCode;
import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import com.rmr.dinosaurs.domain.auth.security.model.DinoPrincipal;
import com.rmr.dinosaurs.domain.auth.service.UserService;
import com.rmr.dinosaurs.domain.auth.utils.converter.UserConverter;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.Authority;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  public static final Supplier<RuntimeException> NO_USER_FOUND_EXCEPTION_SUPPLIER = () ->
      new ServiceException(UserErrorCode.USER_NOT_FOUND);

  private final UserRepository userRepository;
  private final UserConverter userConverter;

  @Override
  public UserDto getCurrentUserDto() {
    return getUserById(getCurrentUserPrincipal().getId());
  }

  @Override
  public UserDto getUserById(Long id) {
    var user = getUserFromRepositoryById(id);
    return userConverter.toUserDto(user);
  }

  @Override
  public List<UserDto> getAllUsers() {
    return userRepository.findAll()
        .stream()
        .map(userConverter::toUserDto)
        .toList();
  }

  @Override
  public UserDto setUserModerator(Long id, Boolean isModerator) {
    var user = getUserFromRepositoryById(id);
    if (!Authority.ROLE_ADMIN.equals(user.getRole())) {
      if (Boolean.TRUE.equals(isModerator)) {
        user.setRole(Authority.ROLE_MODERATOR);
      } else {
        user.setRole(Authority.ROLE_REGULAR);
      }
    }
    user = userRepository.save(user);
    return userConverter.toUserDto(user);
  }

  @Scheduled(cron = "midnight")
  private void deleteNotConfirmedEmailUsers() {
    userRepository.deleteAllByIsConfirmedIsFalseAndRegisteredAtIsBefore(
        LocalDateTime.now().minus(12, ChronoUnit.HOURS));
  }

  private User getUserFromRepositoryById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(NO_USER_FOUND_EXCEPTION_SUPPLIER);
  }

  private DinoPrincipal getCurrentUserPrincipal() {
    return (DinoPrincipal) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
  }

}
