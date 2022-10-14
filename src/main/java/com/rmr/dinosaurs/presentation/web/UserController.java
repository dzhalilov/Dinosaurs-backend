package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.auth.security.permission.AdminPermission;
import com.rmr.dinosaurs.core.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.core.model.dto.UserDto;
import com.rmr.dinosaurs.core.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  UserDto myProfile() {
    return userService.getMyProfile();
  }

  @GetMapping("/{id}")
  UserDto getUserById(@PathVariable Long id) {
    return userService.getUserById(id);
  }

  @GetMapping()
  @ModeratorPermission
  List<UserDto> getUsers() {
    return userService.getUsers();
  }

  @DeleteMapping("/{id}")
  @AdminPermission
  UserDto deleteUser(@PathVariable Long id) {
    return userService.deleteUserById(id);
  }

}
