package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.survey.CreateSurveyDto;
import com.rmr.dinosaurs.core.model.dto.survey.ReadSurveyDto;

public interface SurveyService {

  CreateSurveyDto createSurvey(CreateSurveyDto survey);

  ReadSurveyDto getSurvey();

}
