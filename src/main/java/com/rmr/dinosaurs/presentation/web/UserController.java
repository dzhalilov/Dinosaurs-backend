package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.auth.security.permission.AdminPermission;
import com.rmr.dinosaurs.core.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.core.model.dto.UserDto;
import com.rmr.dinosaurs.core.service.UserService;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  UserDto getCurrentUserData() {
    return userService.getCurrentUserDto();
  }

  @GetMapping("/{id}")
  @ModeratorPermission
  UserDto getUserById(@PathVariable Long id) {
    return userService.getUserById(id);
  }

  @GetMapping()
  @ModeratorPermission
  List<UserDto> getUsers() {
    return userService.getAllUsers();
  }

  @PutMapping("/{id}")
  @AdminPermission
  UserDto deleteUser(@PathVariable Long id, @RequestParam @NotNull Boolean isModerator) {
    return userService.setUserModerator(id, isModerator);
  }

}
