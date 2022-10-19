package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.model.dto.survey.CreateSurveyDto;
import com.rmr.dinosaurs.core.model.dto.survey.ReadSurveyDto;
import com.rmr.dinosaurs.core.service.SurveyService;
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

  @PostMapping
  public ResponseEntity<CreateSurveyDto> createSurvey(@RequestBody CreateSurveyDto survey) {
    CreateSurveyDto createdSurvey = surveyService.createSurvey(survey);
    URI createdSurveyUri = URI.create("/api/v1/survey");
    return ResponseEntity
        .created(createdSurveyUri)
        .body(createdSurvey);
  }

  @GetMapping
  public ResponseEntity<ReadSurveyDto> getSurvey() {
    ReadSurveyDto survey = surveyService.getSurvey();
    return ResponseEntity
        .ok()
        .body(survey);
  }

}
