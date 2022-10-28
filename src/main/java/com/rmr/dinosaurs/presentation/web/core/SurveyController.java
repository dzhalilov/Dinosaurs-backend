package com.rmr.dinosaurs.presentation.web.core;

import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import com.rmr.dinosaurs.domain.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.domain.auth.service.UserService;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.domain.core.model.dto.SurveyCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.SurveyReadDto;
import com.rmr.dinosaurs.domain.core.model.dto.SurveyResponseDto;
import com.rmr.dinosaurs.domain.core.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/survey")
@RequiredArgsConstructor
public class SurveyController {

  private final SurveyService surveyService;
  private final UserService userService;

  @Operation(description = "create survey data using dto")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "got created survey data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = SurveyCreateDto.class))}),
      @ApiResponse(responseCode = "404", description = "profession profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "404", description = "provider profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400", description = "bad request",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400",
          description = "current user has no permissions to create provided survey",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping
  @ModeratorPermission
  public ResponseEntity<SurveyCreateDto> addSurvey(@RequestBody SurveyCreateDto survey) {
    SurveyCreateDto createdSurvey = surveyService.addSurvey(survey);
    URI createdSurveyUri = URI.create("/api/v1/survey");
    return ResponseEntity
        .created(createdSurveyUri)
        .body(createdSurvey);
  }

  @Operation(description = "get survey data")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got survey data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = SurveyReadDto.class))}),
      @ApiResponse(responseCode = "404", description = "survey not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @GetMapping
  public ResponseEntity<SurveyReadDto> getSurvey() {
    SurveyReadDto survey = surveyService.getSurvey();
    return ResponseEntity
        .ok()
        .body(survey);
  }

  @Operation(description = "post survey response using dto")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got recommended profession data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProfessionDto.class))}),
      @ApiResponse(responseCode = "404", description = "question with such id not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "404", description = "answer with such id not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400", description = "bad request",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/result")
  public ResponseEntity<ProfessionDto> resultSurvey(
      @RequestBody SurveyResponseDto surveyResponseDto) {

    UserDto currentUser = userService.getCurrentUserDto();
    String userProfileEmail = currentUser.getEmail();

    ProfessionDto result = surveyService.resultSurvey(surveyResponseDto, userProfileEmail);
    return ResponseEntity
        .ok()
        .body(result);
  }

}
