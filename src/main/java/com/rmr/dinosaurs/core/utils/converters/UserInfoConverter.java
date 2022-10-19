package com.rmr.dinosaurs.core.utils.converters;

import com.rmr.dinosaurs.core.model.UserInfo;
import com.rmr.dinosaurs.core.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.core.model.dto.UserInfoDto;

public interface UserInfoConverter {

  UserInfoDto toUserInfoDto(UserInfo userInfo);

  ShortUserInfoDto toShortUserInfoDto(UserInfo userInfo);

}
