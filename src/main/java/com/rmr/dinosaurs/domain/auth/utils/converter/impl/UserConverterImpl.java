package com.rmr.dinosaurs.domain.auth.utils.converter.impl;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import com.rmr.dinosaurs.domain.auth.utils.converter.UserConverter;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl implements UserConverter {

  @Override
  public UserDto toUserDto(User user) {
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .role(user.getRole())
        .isConfirmed(user.getIsConfirmed())
        .registeredAt(user.getRegisteredAt())
        .isArchived(user.getIsArchived())
        .archivedAt(Objects.nonNull(user.getArchivedAt())
            ? user.getArchivedAt() : null)
        .build();
  }

}
