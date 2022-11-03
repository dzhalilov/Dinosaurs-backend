package com.rmr.dinosaurs.presentation.web.auth;

import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import com.rmr.dinosaurs.domain.auth.security.permission.AdminPermission;
import com.rmr.dinosaurs.domain.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.domain.auth.service.UserService;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @Operation(description = "get current user auth data")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "get user auth data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class))}),
      @ApiResponse(responseCode = "404", description = "user not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @GetMapping("/me")
  UserDto getCurrentUserData() {
    return userService.getCurrentUserDto();
  }

  @Operation(description = "get user auth data by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "get user auth data by id",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class))}),
      @ApiResponse(responseCode = "404", description = "user not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "403", description = "forbidden")})
  @GetMapping("/{id}")
  @ModeratorPermission
  UserDto getUserById(@Parameter(name = "id", description = "user id") @PathVariable Long id) {
    return userService.getUserById(id);
  }

  @Operation(description = "get user auth data by email")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "get user auth data by email",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class))}),
      @ApiResponse(responseCode = "404", description = "user not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "403", description = "forbidden")})
  @GetMapping("/find")
  @AdminPermission
  UserDto getUserByEmail(
      @Parameter(name = "email", description = "user email") @RequestParam String email) {
    return userService.getUserByEmail(email);
  }

  @Operation(description = "get all users auth data")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "get list of all users auth data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class))}),
      @ApiResponse(responseCode = "403", description = "forbidden")})
  @GetMapping()
  @ModeratorPermission
  List<UserDto> getUsers() {
    return userService.getAllUsers();
  }

  @Operation(description = "set/unset user as moderator")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "set user role to moderator/regular",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class))}),
      @ApiResponse(responseCode = "404", description = "user not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "403", description = "forbidden")})
  @PutMapping("/{id}")
  @AdminPermission
  UserDto setUserModeratorById(
      @Parameter(name = "id", description = "user id") @PathVariable Long id,
      @Parameter(description = "true to set moderator, false to unset")
      @RequestParam @NotNull Boolean isModerator) {
    return userService.setUserModerator(id, isModerator);
  }

}
