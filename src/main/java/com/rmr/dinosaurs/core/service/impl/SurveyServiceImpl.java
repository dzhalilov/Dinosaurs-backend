package com.rmr.dinosaurs.core.service.impl;

import com.rmr.dinosaurs.core.model.Profession;
import com.rmr.dinosaurs.core.model.Survey;
import com.rmr.dinosaurs.core.model.SurveyQuestion;
import com.rmr.dinosaurs.core.model.SurveyQuestionAnswer;
import com.rmr.dinosaurs.core.model.dto.survey.AnswerDto;
import com.rmr.dinosaurs.core.model.dto.survey.CreateSurveyDto;
import com.rmr.dinosaurs.core.model.dto.survey.QuestionDto;
import com.rmr.dinosaurs.core.service.SurveyService;
import com.rmr.dinosaurs.core.service.exceptions.ProfessionNotFoundException;
import com.rmr.dinosaurs.core.utils.mapper.SurveyEntityDtoMapper;
import com.rmr.dinosaurs.infrastucture.database.ProfessionRepository;
import com.rmr.dinosaurs.infrastucture.database.SurveyQuestionAnswerRepository;
import com.rmr.dinosaurs.infrastucture.database.SurveyQuestionRepository;
import com.rmr.dinosaurs.infrastucture.database.SurveyRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

  private final SurveyEntityDtoMapper mapper;

  private final SurveyRepository surveyRepo;
  private final SurveyQuestionRepository questionRepo;
  private final SurveyQuestionAnswerRepository answerRepo;
  private final ProfessionRepository professionRepo;

  private Long surveyId;

  @Override
  @Transactional
  public CreateSurveyDto createSurvey(CreateSurveyDto dto) {
    Survey savedSurvey = saveAndFlushSurvey(dto);
    saveAndFlushSurveyQuestionsAndTheirsAnswers(savedSurvey, dto.getSurvey());
    surveyId = savedSurvey.getId();
    return dto;
  }

  private Survey saveAndFlushSurvey(CreateSurveyDto dto) {
    Survey s = mapper.toSurvey(dto);
    Survey savedSurvey = surveyRepo.saveAndFlush(s);
    dto.setSurveyId(savedSurvey.getId());
    return savedSurvey;
  }

  private void saveAndFlushSurveyQuestionsAndTheirsAnswers(
      Survey survey, List<QuestionDto> qdtoList) {

    Map<Long, Profession> professionCache = new HashMap<>();
    for (QuestionDto qdto : qdtoList) {
      SurveyQuestion savedQuestion = saveAndFlushSurveyQuestion(survey, qdto);
      saveAndFlushSurveyQuestionAnswers(savedQuestion, qdto.getAnswers(), professionCache);
    }
  }

  private SurveyQuestion saveAndFlushSurveyQuestion(Survey survey, QuestionDto qdto) {
    SurveyQuestion q = mapper.toSurveyQuestion(qdto);
    q.setSurvey(survey);
    SurveyQuestion savedQuestion = questionRepo.saveAndFlush(q);
    qdto.setQuestionId(savedQuestion.getId());
    return savedQuestion;
  }

  private void saveAndFlushSurveyQuestionAnswers(
      SurveyQuestion question,
      List<AnswerDto> adtoList,
      Map<Long, Profession> professionCache) {

    for (AnswerDto adto : adtoList) {
      Long professionId = adto.getProfessionId();
      Profession profession = cacheProfessionOrGet(professionId, professionCache);
      saveAndFlushSurveyQuestionAnswer(question, profession, adto);
    }
  }

  private Profession cacheProfessionOrGet
      (Long professionId, Map<Long, Profession> professionCache) {

    Profession profession;
    if (!professionCache.containsKey(professionId)) {
      Profession cachingProfession = professionRepo.findById(professionId)
          .orElseThrow(ProfessionNotFoundException::new);
      profession = professionCache.put(professionId, cachingProfession);
    } else {
      profession = professionCache.get(professionId);
    }

    return profession;
  }

  private void saveAndFlushSurveyQuestionAnswer(
      SurveyQuestion question, Profession profession,
      AnswerDto adto) {

    SurveyQuestionAnswer a = mapper.toSurveyQuestionAnswer(adto);
    a.setQuestion(question);
    a.setProfession(profession);
    SurveyQuestionAnswer savedAnswer = answerRepo.saveAndFlush(a);
    adto.setAnswerId(savedAnswer.getId());
  }

}
