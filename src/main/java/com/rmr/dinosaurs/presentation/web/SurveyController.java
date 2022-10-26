package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.core.model.dto.SurveyCreateDto;
import com.rmr.dinosaurs.core.model.dto.SurveyReadDto;
import com.rmr.dinosaurs.core.model.dto.SurveyResponseDto;
import com.rmr.dinosaurs.core.model.dto.UserDto;
import com.rmr.dinosaurs.core.service.SurveyService;
import com.rmr.dinosaurs.core.service.UserService;
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

  @PostMapping
  @ModeratorPermission
  public ResponseEntity<SurveyCreateDto> createSurvey(@RequestBody SurveyCreateDto survey) {
    SurveyCreateDto createdSurvey = surveyService.createSurvey(survey);
    URI createdSurveyUri = URI.create("/api/v1/survey");
    return ResponseEntity
        .created(createdSurveyUri)
        .body(createdSurvey);
  }

  @GetMapping
  public ResponseEntity<SurveyReadDto> getSurvey() {
    SurveyReadDto survey = surveyService.getSurvey();
    return ResponseEntity
        .ok()
        .body(survey);
  }

  @PostMapping("/result")
  public ResponseEntity<ProfessionDto> resultSurvey(@RequestBody SurveyResponseDto response) {
    UserDto user = userService.getCurrentUserDto();
    String userEmail = user.getEmail();

    ProfessionDto result = surveyService.resultSurvey(response, userEmail);
    return ResponseEntity
        .ok()
        .body(result);
  }

}
