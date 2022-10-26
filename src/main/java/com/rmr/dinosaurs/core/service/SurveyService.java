package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.core.model.dto.SurveyCreateDto;
import com.rmr.dinosaurs.core.model.dto.SurveyReadDto;
import com.rmr.dinosaurs.core.model.dto.SurveyResponseDto;

public interface SurveyService {

  SurveyCreateDto addSurvey(SurveyCreateDto survey);

  SurveyReadDto getSurvey();

  ProfessionDto resultSurvey(SurveyResponseDto response, String email);

}
