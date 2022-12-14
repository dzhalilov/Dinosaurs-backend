package com.rmr.dinosaurs.domain.auth.service.impl;

import static com.rmr.dinosaurs.domain.auth.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import com.rmr.dinosaurs.domain.auth.security.model.DinoPrincipal;
import com.rmr.dinosaurs.domain.auth.service.UserService;
import com.rmr.dinosaurs.domain.auth.utils.converter.UserConverter;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.Authority;
import com.rmr.dinosaurs.domain.notification.client.NotificationClient;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  public static final Supplier<RuntimeException> NO_USER_FOUND_EXCEPTION_SUPPLIER = () ->
      new ServiceException(USER_NOT_FOUND);

  private final UserRepository userRepository;
  private final UserConverter userConverter;

  private final NotificationClient notificationClient;

  @Value("${tempconfirmation.code.ttl}")
  private Long tempCodeTtl;


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
    return userRepository.findAllByIsConfirmedTrue()
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
      notificationClient.roleChangedNotification(isModerator, user.getEmail());
    }
    user = userRepository.save(user);
    return userConverter.toUserDto(user);
  }

  @Override
  public UserDto getUserByEmail(String email) {
    var user = userRepository.findByEmailIgnoreCase(email)
        .orElseThrow(NO_USER_FOUND_EXCEPTION_SUPPLIER);
    return userConverter.toUserDto(user);
  }

  @Scheduled(cron = "@midnight")
  @Transactional
  public void deleteNotConfirmedEmailUsers() {
    log.info("Deleting all users with not confirmed emails");
    userRepository.deleteAllNotConfirmedEmailBefore(
        LocalDateTime.now(ZoneOffset.UTC).minus(tempCodeTtl, ChronoUnit.MINUTES));
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
