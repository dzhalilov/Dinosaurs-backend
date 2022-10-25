package com.rmr.dinosaurs.core.service.impl;

import static com.rmr.dinosaurs.core.exception.errorcode.UserErrorCode.USER_NOT_FOUND;
import static com.rmr.dinosaurs.core.model.Authority.ROLE_ADMIN;
import static com.rmr.dinosaurs.core.model.Authority.ROLE_MODERATOR;
import static com.rmr.dinosaurs.core.model.Authority.ROLE_REGULAR;

import com.rmr.dinosaurs.core.auth.security.DinoPrincipal;
import com.rmr.dinosaurs.core.exception.ServiceException;
import com.rmr.dinosaurs.core.model.User;
import com.rmr.dinosaurs.core.model.dto.UserDto;
import com.rmr.dinosaurs.core.service.UserService;
import com.rmr.dinosaurs.core.utils.converters.UserConverter;
import com.rmr.dinosaurs.infrastucture.database.UserRepository;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  public static final Supplier<RuntimeException> NO_USER_FOUND_EXCEPTION_SUPPLIER = () ->
      new ServiceException(USER_NOT_FOUND);

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
    if (!ROLE_ADMIN.equals(user.getRole())) {
      if (Boolean.TRUE.equals(isModerator)) {
        user.setRole(ROLE_MODERATOR);
      } else {
        user.setRole(ROLE_REGULAR);
      }
    }
    user = userRepository.save(user);
    return userConverter.toUserDto(user);
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
