package com.rmr.dinosaurs.core.utils.converters;

import com.rmr.dinosaurs.core.model.User;
import com.rmr.dinosaurs.core.model.dto.UserDto;

public interface UserConverter {

  UserDto toUserDto(User user);

}
