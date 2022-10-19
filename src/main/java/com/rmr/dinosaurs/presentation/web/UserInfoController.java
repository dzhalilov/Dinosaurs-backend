package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.core.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.core.model.dto.UserInfoDto;
import com.rmr.dinosaurs.core.service.UserInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class UserInfoController {

  private final UserInfoService userInfoService;

  @GetMapping("/my")
  UserInfoDto myProfile() {
    return userInfoService.getMyProfile();
  }

  @GetMapping("/{id}")
  @ModeratorPermission
  UserInfoDto getUserById(@PathVariable Long id) {
    return userInfoService.getUserById(id);
  }

  @GetMapping()
  @ModeratorPermission
  List<ShortUserInfoDto> getUsers() {
    return userInfoService.getAllUsers();
  }

  @GetMapping("/moderators")
  @ModeratorPermission
  List<ShortUserInfoDto> getModerators() {
    return userInfoService.getAllModerators();
  }

}
