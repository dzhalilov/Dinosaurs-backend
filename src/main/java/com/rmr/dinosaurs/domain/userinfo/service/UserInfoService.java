package com.rmr.dinosaurs.domain.userinfo.service;

import com.rmr.dinosaurs.domain.userinfo.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.domain.userinfo.model.dto.UserInfoDto;
import java.util.List;

public interface UserInfoService {

  /**
   * get current user full profile info
   *
   * @return user profile data
   */
  UserInfoDto getMyProfile();

  /**
   * set current user profile data
   *
   * @param userInfoDto to be changed to
   * @return changed user profile data
   */
  UserInfoDto editMyProfile(UserInfoDto userInfoDto);

  /**
   * get users profile data by user info id
   *
   * @param id user info id
   * @return users profile data
   */
  UserInfoDto getUserInfoById(Long id);

  /**
   * get list of all users profiles
   *
   * @return users profile data in a short representation
   */
  List<ShortUserInfoDto> getAllUserInfos();

  /**
   * get list of all moderators profiles
   *
   * @return moderators profile data in a short representation
   */
  List<ShortUserInfoDto> getAllModerators();

  /**
   * delete current user profile
   *
   * @return updated UserInfoDto
   */
  UserInfoDto deleteMyProfile();

}
