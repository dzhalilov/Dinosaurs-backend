package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/survey")
@RequiredArgsConstructor
public class SurveyController {

  private final SurveyService surveyService;

}
