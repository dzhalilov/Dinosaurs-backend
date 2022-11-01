package com.rmr.dinosaurs.domain.userinfo.utils.converter;

import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import com.rmr.dinosaurs.domain.userinfo.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.domain.userinfo.model.dto.UserInfoDto;

public interface UserInfoConverter {

  UserInfoDto toUserInfoDto(UserInfo userInfo);

  ShortUserInfoDto toShortUserInfoDto(UserInfo userInfo);

}
