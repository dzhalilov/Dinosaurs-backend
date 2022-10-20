package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.profession.ProfessionDto;
import com.rmr.dinosaurs.core.model.dto.survey.CreateSurveyDto;
import com.rmr.dinosaurs.core.model.dto.survey.ReadSurveyDto;
import com.rmr.dinosaurs.core.model.dto.survey.SurveyResponseDto;

public interface SurveyService {

  CreateSurveyDto createSurvey(CreateSurveyDto survey);

  ReadSurveyDto getSurvey();

  ProfessionDto resultSurvey(SurveyResponseDto response, String email);

}
