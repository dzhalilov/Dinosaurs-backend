package com.rmr.dinosaurs.core.service.impl;

import com.rmr.dinosaurs.core.model.Profession;
import com.rmr.dinosaurs.core.model.Survey;
import com.rmr.dinosaurs.core.model.SurveyQuestion;
import com.rmr.dinosaurs.core.model.SurveyQuestionAnswer;
import com.rmr.dinosaurs.core.model.UserInfo;
import com.rmr.dinosaurs.core.model.dto.profession.ProfessionDto;
import com.rmr.dinosaurs.core.model.dto.survey.CreateAnswerDto;
import com.rmr.dinosaurs.core.model.dto.survey.CreateQuestionDto;
import com.rmr.dinosaurs.core.model.dto.survey.CreateSurveyDto;
import com.rmr.dinosaurs.core.model.dto.survey.ReadAnswerDto;
import com.rmr.dinosaurs.core.model.dto.survey.ReadQuestionDto;
import com.rmr.dinosaurs.core.model.dto.survey.ReadSurveyDto;
import com.rmr.dinosaurs.core.model.dto.survey.SurveyQuestionResponseDto;
import com.rmr.dinosaurs.core.model.dto.survey.SurveyResponseDto;
import com.rmr.dinosaurs.core.service.SurveyService;
import com.rmr.dinosaurs.core.service.exceptions.ProfessionNotFoundException;
import com.rmr.dinosaurs.core.service.exceptions.SurveyNotFoundException;
import com.rmr.dinosaurs.core.utils.mapper.SurveyEntityDtoMapper;
import com.rmr.dinosaurs.infrastucture.database.ProfessionRepository;
import com.rmr.dinosaurs.infrastucture.database.SurveyQuestionAnswerRepository;
import com.rmr.dinosaurs.infrastucture.database.SurveyQuestionRepository;
import com.rmr.dinosaurs.infrastucture.database.SurveyRepository;
import com.rmr.dinosaurs.infrastucture.database.UserInfoRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

  private final SurveyEntityDtoMapper surveyMapper;

  private final SurveyRepository surveyRepo;
  private final SurveyQuestionRepository questionRepo;
  private final SurveyQuestionAnswerRepository answerRepo;
  private final ProfessionRepository professionRepo;
  private final UserInfoRepository userInfoRepo;

  private Long singletonSurveyId;

  @Override
  @Transactional
  public CreateSurveyDto createSurvey(CreateSurveyDto dto) {
    Survey savedSurvey = saveAndFlushSurvey(dto);
    saveAndFlushSurveyQuestionsAndTheirsAnswers(savedSurvey, dto.getSurvey());
    singletonSurveyId = savedSurvey.getId();
    return dto;
  }

  @Override
  @Transactional
  public ReadSurveyDto getSurvey() {
    Survey s = surveyRepo.findById(singletonSurveyId)
        .orElseThrow(SurveyNotFoundException::new);
    return toReadSurveyDto(s);
  }

  @Override
  @Transactional
  public ProfessionDto resultSurvey(SurveyResponseDto response) {
    List<Long> answerIds = response.getSurvey().stream()
        .map(SurveyQuestionResponseDto::getAnswerId)
        .toList();
    List<SurveyQuestionAnswer> answers = answerRepo.findAllById(answerIds);

    Profession recommendedProfession = recommendProfession(answers);
    attachRecommendedProfessionToUser(email, recommendedProfession);

    Map.Entry<Profession, Integer> maxRepetitionEntry = professionRepetitionsMap
  }

  private Survey saveAndFlushSurvey(CreateSurveyDto dto) {
    Survey s = surveyMapper.toSurvey(dto);
    Survey savedSurvey = surveyRepo.saveAndFlush(s);
    dto.setSurveyId(savedSurvey.getId());
    return savedSurvey;
  }

  private void saveAndFlushSurveyQuestionsAndTheirsAnswers(
      Survey survey, List<CreateQuestionDto> qdtoList) {

    Map<Long, Profession> professionCache = new HashMap<>();
    for (CreateQuestionDto qdto : qdtoList) {
      SurveyQuestion savedQuestion = saveAndFlushSurveyQuestion(survey, qdto);
      saveAndFlushSurveyQuestionAnswers(savedQuestion, qdto.getAnswers(), professionCache);
    }
  }

  private SurveyQuestion saveAndFlushSurveyQuestion(Survey survey, CreateQuestionDto qdto) {
    SurveyQuestion q = surveyMapper.toSurveyQuestion(qdto);
    q.setSurvey(survey);
    SurveyQuestion savedQuestion = questionRepo.saveAndFlush(q);
    qdto.setQuestionId(savedQuestion.getId());
    return savedQuestion;
  }

  private void saveAndFlushSurveyQuestionAnswers(
      SurveyQuestion question,
      List<CreateAnswerDto> adtoList,
      Map<Long, Profession> professionCache) {

    for (CreateAnswerDto adto : adtoList) {
      Long professionId = adto.getProfessionId();
      Profession profession = cacheProfessionOrGet(professionId, professionCache);
      saveAndFlushSurveyQuestionAnswer(question, profession, adto);
    }
  }

  private Profession cacheProfessionOrGet(
      Long professionId, Map<Long, Profession> professionCache) {

    if (!professionCache.containsKey(professionId)) {
      Profession cachingProfession = professionRepo.findById(professionId)
          .orElseThrow(ProfessionNotFoundException::new);
      professionCache.put(professionId, cachingProfession);
    }

    return professionCache.get(professionId);
  }

  private void saveAndFlushSurveyQuestionAnswer(
      SurveyQuestion question, Profession profession,
      CreateAnswerDto adto) {

    SurveyQuestionAnswer a = surveyMapper.toSurveyQuestionAnswer(adto);
    a.setQuestion(question);
    a.setProfession(profession);
    SurveyQuestionAnswer savedAnswer = answerRepo.saveAndFlush(a);
    adto.setAnswerId(savedAnswer.getId());
  }

  private ReadSurveyDto toReadSurveyDto(Survey survey) {
    Set<SurveyQuestion> sqSet = survey.getQuestions();
    List<ReadQuestionDto> qdtoList = new ArrayList<>(sqSet.size());
    for (SurveyQuestion sq : sqSet) {

      Set<SurveyQuestionAnswer> sqaSet = sq.getAnswers();
      List<ReadAnswerDto> adtoList = new ArrayList<>(sqaSet.size());
      for (SurveyQuestionAnswer sqa : sqaSet) {
        adtoList.add(surveyMapper.toReadAnswerDto(sqa));
      }

      ReadQuestionDto qdto = surveyMapper.toReadQuestionDto(sq);
      qdto.setAnswers(adtoList);
      qdtoList.add(qdto);
    }

    ReadSurveyDto surveyDto = new ReadSurveyDto();
    surveyDto.setSurveyId(survey.getId());
    surveyDto.setTitle(survey.getTitle());
    surveyDto.setDescription(survey.getDescription());
    surveyDto.setSurvey(qdtoList);

    return surveyDto;
  }

  private Profession recommendProfession(List<SurveyQuestionAnswer> answers) {
    Map<Profession, Integer> professionRepetitionsMap = new HashMap<>();
    for (SurveyQuestionAnswer sqa : answers) {
      Profession profession = sqa.getProfession();
      if (!professionRepetitionsMap.containsKey(profession)) {
        professionRepetitionsMap.put(profession, 1);
      } else {
        professionRepetitionsMap.put(
            profession,
            professionRepetitionsMap.get(profession) + 1);
      }
    }

    Map.Entry<Profession, Integer> maxRepetitionEntry = professionRepetitionsMap
        .entrySet().stream()
        .max(Map.Entry.comparingByValue()).orElse(null);

    Profession result;
    if (maxRepetitionEntry == null) {
      result = null;
    } else {
      result = maxRepetitionEntry.getKey();
    }

    return result;
  }

  private void attachRecommendedProfessionToUser(String email, Profession recommendedProfession) {
    UserInfo userInfo = userInfoRepo.findByUser_Email(email).orElse(null);
    if (userInfo != null) {
      userInfo.setRecommendedProfession(recommendedProfession);
      userInfoRepo.save(userInfo);
    }
  }

}
