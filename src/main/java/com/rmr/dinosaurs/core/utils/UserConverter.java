package com.rmr.dinosaurs.core.utils;

import com.rmr.dinosaurs.core.model.User;
import com.rmr.dinosaurs.core.model.dto.UserDto;

public interface UserConverter {

  UserDto toUserDto(User user);

}
