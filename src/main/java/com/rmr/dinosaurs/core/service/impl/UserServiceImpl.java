package com.rmr.dinosaurs.core.service.impl;

import com.rmr.dinosaurs.core.model.User;
import com.rmr.dinosaurs.core.model.dto.UserDto;
import com.rmr.dinosaurs.core.service.UserService;
import com.rmr.dinosaurs.core.utils.UserConverter;
import com.rmr.dinosaurs.infrastucture.database.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserConverter userConverter;

  @Override
  public UserDto getMyProfile() {
    return null;
  }

  @Override
  public UserDto getUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(
        () -> new RuntimeException("No user found by provided id")
    );
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

}
