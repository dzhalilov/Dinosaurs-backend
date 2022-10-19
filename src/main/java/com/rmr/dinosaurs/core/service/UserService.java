package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.UserDto;
import java.util.List;

public interface UserService {

  UserDto getCurrentUserDto();

  UserDto getUserById(Long id);

  List<UserDto> getAllUsers();

  UserDto setUserModerator(Long id, Boolean isModerator);

}
