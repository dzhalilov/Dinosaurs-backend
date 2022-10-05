package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.UserDto;
import java.util.List;
import java.util.UUID;

public interface UserService {

  UserDto getMyProfile();

  UserDto getUserById(UUID id);

  List<UserDto> getUsers();

  UserDto deleteUserById(UUID id);

}
