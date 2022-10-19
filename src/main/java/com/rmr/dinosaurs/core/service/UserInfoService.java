package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.core.model.dto.UserInfoDto;
import java.util.List;

public interface UserInfoService {

  UserInfoDto getMyProfile();

  UserInfoDto getUserById(Long id);

  List<ShortUserInfoDto> getAllUsers();

  List<ShortUserInfoDto> getAllModerators();
}
