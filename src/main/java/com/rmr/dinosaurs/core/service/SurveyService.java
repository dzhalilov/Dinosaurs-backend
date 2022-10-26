package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.core.model.dto.SurveyCreateDto;
import com.rmr.dinosaurs.core.model.dto.SurveyReadDto;
import com.rmr.dinosaurs.core.model.dto.SurveyResponseDto;

public interface SurveyService {

  /**
   * create survey data
   *
   * @param surveyDto to be created
   * @return created survey data
   */
  SurveyCreateDto addSurvey(SurveyCreateDto surveyDto);

  /**
   * get current survey
   *
   * @return survey data
   */
  SurveyReadDto getSurvey();

  /**
   * get recommended profession profile by survey response
   *
   * @param surveyResponseDto survey response
   * @param userProfileEmail  user profile getting recommended profession
   * @return recommended profession profile
   */
  ProfessionDto resultSurvey(SurveyResponseDto surveyResponseDto, String userProfileEmail);

}
