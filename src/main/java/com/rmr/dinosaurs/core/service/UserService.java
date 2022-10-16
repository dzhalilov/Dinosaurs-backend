package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.UserDto;
import java.util.List;

public interface UserService {

  UserDto getMyProfile();

  UserDto getUserById(Long id);

  List<UserDto> getUsers();

  UserDto deleteUserById(Long id);

}
