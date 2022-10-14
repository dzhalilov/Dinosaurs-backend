package com.rmr.dinosaurs.core.utils.converters.impl;

import com.rmr.dinosaurs.core.model.User;
import com.rmr.dinosaurs.core.model.dto.UserDto;
import com.rmr.dinosaurs.core.utils.converters.UserConverter;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl implements UserConverter {

  @Override
  public UserDto toUserDto(User user) {
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .role(user.getRole())
        .build();
  }

}
