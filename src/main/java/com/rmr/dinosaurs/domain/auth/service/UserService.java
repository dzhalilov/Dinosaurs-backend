package com.rmr.dinosaurs.domain.auth.service;

import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import java.util.List;

public interface UserService {

  UserDto getCurrentUserDto();

  UserDto getUserById(Long id);

  List<UserDto> getAllUsers();

  UserDto setUserModerator(Long id, Boolean isModerator);

}
