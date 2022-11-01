package com.rmr.dinosaurs.domain.auth.utils.converter;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;

public interface UserConverter {

  UserDto toUserDto(User user);

}
