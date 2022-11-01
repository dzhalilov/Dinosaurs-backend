package com.rmr.dinosaurs.domain.userinfo.utils.converter.impl;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import com.rmr.dinosaurs.domain.userinfo.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.domain.userinfo.model.dto.UserInfoDto;
import com.rmr.dinosaurs.domain.userinfo.utils.converter.UserInfoConverter;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class UserInfoConverterImpl implements UserInfoConverter {

  @Override
  public UserInfoDto toUserInfoDto(UserInfo userInfo) {
    User user = userInfo.getUser();
    return UserInfoDto.builder()
        .id(userInfo.getId())
        .email(user.getEmail())
        .role(user.getRole())
        .name(userInfo.getName())
        .surname(userInfo.getSurname())
        .isConfirmed(user.getIsConfirmed())
        .registeredAt(user.getRegisteredAt())
        .isArchived(user.getIsArchived())
        .archivedAt(Objects.isNull(user.getArchivedAt()) ? null
            : user.getArchivedAt())
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
