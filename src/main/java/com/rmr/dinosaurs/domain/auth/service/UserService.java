package com.rmr.dinosaurs.domain.auth.service;

import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import java.util.List;

public interface UserService {

  /**
   * get current user
   *
   * @return UserDto
   */
  UserDto getCurrentUserDto();

  /**
   * get user by id
   *
   * @param id user id
   * @return UserDto
   */
  UserDto getUserById(Long id);

  /**
   * return all confirmed users
   *
   * @return list of UserDto
   */
  List<UserDto> getAllUsers();

  /**
   * set or unset user as moderator
   *
   * @param id          user id
   * @param isModerator flag to be set
   * @return updated UserDto
   */
  UserDto setUserModerator(Long id, Boolean isModerator);

}
