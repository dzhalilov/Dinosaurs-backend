package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.core.exception.ServiceException;
import com.rmr.dinosaurs.core.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.core.model.dto.UserInfoDto;
import com.rmr.dinosaurs.core.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profiles")
@Tag(name = "User info controller")
@RequiredArgsConstructor
public class UserInfoController {

  private final UserInfoService userInfoService;

  @Operation(description = "get current user profile data")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "get personal user data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserInfoDto.class))}),
      @ApiResponse(responseCode = "404", description = "user not found or user profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @GetMapping("/my")
  UserInfoDto myProfile() {
    return userInfoService.getMyProfile();
  }

  @Operation(description = "edit current user profile data")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "get edited personal user data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserInfoDto.class))}),
      @ApiResponse(responseCode = "404", description = "user not found or user profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400",
          description = "current user has no permissions to edit provided profile",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/my")
  UserInfoDto editMyProfile(@RequestBody @NotNull UserInfoDto userInfoDto) {
    return userInfoService.editMyProfile(userInfoDto);
  }

  @Operation(description = "get user profile data by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "get personal user data by id",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserInfoDto.class))}),
      @ApiResponse(responseCode = "404", description = "user not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "403", description = "forbidden")})
  @GetMapping("/{id}")
  @ModeratorPermission
  UserInfoDto getUserInfoById(
      @Parameter(name = "id", description = "user profile id") @PathVariable Long id) {
    return userInfoService.getUserInfoById(id);
  }

  @Operation(description = "get all users profiles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "get personal users data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ShortUserInfoDto.class))}),
      @ApiResponse(responseCode = "403", description = "forbidden")})
  @GetMapping()
  @ModeratorPermission
  List<ShortUserInfoDto> getUserInfos() {
    return userInfoService.getAllUserInfos();
  }

  @Operation(description = "get all moderators profiles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "get personal moderators data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ShortUserInfoDto.class))}),
      @ApiResponse(responseCode = "403", description = "forbidden")})
  @GetMapping("/moderators")
  @ModeratorPermission
  List<ShortUserInfoDto> getModerators() {
    return userInfoService.getAllModerators();
  }

}
