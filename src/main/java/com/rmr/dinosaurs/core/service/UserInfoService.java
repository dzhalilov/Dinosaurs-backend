package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.core.model.dto.UserInfoDto;
import java.util.List;

public interface UserInfoService {

  UserInfoDto getMyProfile();

  UserInfoDto getUserInfoById(Long id);

  List<ShortUserInfoDto> getAllUserInfos();

  List<ShortUserInfoDto> getAllModerators();
}
