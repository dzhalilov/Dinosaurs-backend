package com.rmr.dinosaurs.core.service.impl;

import com.rmr.dinosaurs.core.auth.security.DinoPrincipal;
import com.rmr.dinosaurs.core.model.User;
import com.rmr.dinosaurs.core.model.dto.UserDto;
import com.rmr.dinosaurs.core.service.UserService;
import com.rmr.dinosaurs.core.utils.converters.UserConverter;
import com.rmr.dinosaurs.core.utils.converters.UserInfoConverter;
import com.rmr.dinosaurs.infrastucture.database.UserInfoRepository;
import com.rmr.dinosaurs.infrastucture.database.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserInfoRepository userInfoRepository;
  private final UserConverter userConverter;
  private final UserInfoConverter userInfoConverter;

  @Override
  public UserDto getMyProfile() {
    return getUserById(getCurrentUserPrincipal().getId());
  }

  @Override
  public UserDto getUserById(Long id) {
    var user = getUserFromRepositoryById(id);
    return userConverter.toUserDto(user);
  }

  @Override
  public List<UserDto> getUsers() {
    return userRepository.findAll()
        .stream()
        .map(userConverter::toUserDto)
        .toList();
  }

  @Override
  public UserDto deleteUserById(Long id) {
    return null;
  }

  @Override
  public UserDto setUserModerator(Long id, Boolean isModerator) {
    return null;
  }

  private User getUserFromRepositoryById(Long id) {
    return userRepository.findById(id).orElseThrow(
        () -> new RuntimeException("No user found by provided id")
    );
  }

  private DinoPrincipal getCurrentUserPrincipal() {
    return (DinoPrincipal) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
  }

}
