package com.rmr.dinosaurs.core.utils.converters.impl;

import com.rmr.dinosaurs.core.model.User;
import com.rmr.dinosaurs.core.model.UserInfo;
import com.rmr.dinosaurs.core.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.core.model.dto.UserInfoDto;
import com.rmr.dinosaurs.core.utils.converters.UserInfoConverter;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class UserInfoConverterImpl implements UserInfoConverter {

  @Override
  public UserInfoDto toUserInfoDto(UserInfo userInfo) {
    return UserInfoDto.builder()
        .id(userInfo.getId())
        .email(userInfo.getUser().getEmail())
        .role(userInfo.getUser().getRole())
        .name(userInfo.getName())
        .surname(userInfo.getSurname())
        .isConfirmed(userInfo.getIsConfirmedUser()) // confusing
        .registeredAt(userInfo.getRegisteredAt())
        .archivedAt(Objects.isNull(userInfo.getArchivedAt()) ? null
            : userInfo.getArchivedAt())
        .userId(userInfo.getUser().getId())
        .professionId(Objects.isNull(userInfo.getRecommendedProfession()) ? null
            : userInfo.getRecommendedProfession().getId())
        .build();
  }

  @Override
  public ShortUserInfoDto toShortUserInfoDto(UserInfo userInfo) {
    User user = userInfo.getUser();
    return ShortUserInfoDto.builder()
        .id(userInfo.getId())
        .email(user.getEmail())
        .role(user.getRole())
        .name(userInfo.getName())
        .userId(user.getId())
        .build();
  }

}
