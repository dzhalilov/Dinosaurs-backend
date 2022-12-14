package com.rmr.dinosaurs.presentation.web.userinfo;

import com.rmr.dinosaurs.domain.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.dto.study.CourseStudyResponseDto;
import com.rmr.dinosaurs.domain.core.service.CourseService;
import com.rmr.dinosaurs.domain.userinfo.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.domain.userinfo.model.dto.UserInfoDto;
import com.rmr.dinosaurs.domain.userinfo.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@Slf4j
public class UserInfoController {

  private final UserInfoService userInfoService;
  private final CourseService courseService;

  @Operation(summary = "Get current user profile data")
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

  @Operation(summary = "Edit current user profile data")
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

  @Operation(summary = "Delete current user profile data")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "get updated personal user data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserInfoDto.class))}),
      @ApiResponse(responseCode = "404", description = "user not found or user profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "403",
          description = "current user has no permissions to delete profile",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @DeleteMapping("/my")
  UserInfoDto deleteMyProfile() {
    return userInfoService.deleteMyProfile();
  }

  @Operation(summary = "Get user profile data by id")
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

  @Operation(summary = "Get all users profiles")
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

  @Operation(summary = "Get all moderators profiles")
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

  @Operation(summary = "Get my studying")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "get my course study info",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = CourseStudyResponseDto.class))}),
      @ApiResponse(responseCode = "404", description = "user not found or user profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @GetMapping("/my/study")
  public ResponseEntity<List<CourseStudyResponseDto>> getMyCourseStudy(Principal principal) {
    String email = principal.getName();
    log.info("Get course study information belong user={}", email);
    List<CourseStudyResponseDto> courseStudyResponseDtoList = courseService.getMyCourseStudy(
        principal);
    return ResponseEntity.ok(courseStudyResponseDtoList);
  }

}
