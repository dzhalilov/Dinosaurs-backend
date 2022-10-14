package com.rmr.dinosaurs.core.utils.converters.impl;

import com.rmr.dinosaurs.core.model.UserInfo;
import com.rmr.dinosaurs.core.model.dto.UserInfoDto;
import com.rmr.dinosaurs.core.utils.converters.UserInfoConverter;
import org.springframework.stereotype.Component;

@Component
public class UserInfoConverterImpl implements UserInfoConverter {

  @Override
  public UserInfoDto toUserInfoDto(UserInfo userInfo) {
    return null;
  }

}
