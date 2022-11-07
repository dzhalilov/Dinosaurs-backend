package com.rmr.dinosaurs.domain.core.service.impl;

import static com.rmr.dinosaurs.domain.core.exception.errorcode.ProfessionErrorCode.PROFESSION_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.SurveyErrorCode.SURVEY_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.SurveyErrorCode.SURVEY_QUESTION_ANSWER_WITH_NO_PROFESSION_ID;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.SurveyErrorCode.SURVEY_RESPONSE_NOT_MATCHING_ACTUAL_SURVEY;

import com.rmr.dinosaurs.domain.auth.security.model.DinoPrincipal;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.Profession;
import com.rmr.dinosaurs.domain.core.model.Survey;
import com.rmr.dinosaurs.domain.core.model.SurveyQuestion;
import com.rmr.dinosaurs.domain.core.model.SurveyQuestionAnswer;
import com.rmr.dinosaurs.domain.core.model.dto.AnswerCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.AnswerReadDto;
import com.rmr.dinosaurs.domain.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.domain.core.model.dto.QuestionCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.QuestionReadDto;
import com.rmr.dinosaurs.domain.core.model.dto.SurveyCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.SurveyReadDto;
import com.rmr.dinosaurs.domain.core.model.dto.SurveyResponseDto;
import com.rmr.dinosaurs.domain.core.model.dto.SurveyResponseQuestionDto;
import com.rmr.dinosaurs.domain.core.service.SurveyService;
import com.rmr.dinosaurs.domain.core.utils.mapper.ProfessionEntityDtoMapper;
import com.rmr.dinosaurs.domain.core.utils.mapper.SurveyEntityDtoMapper;
import com.rmr.dinosaurs.infrastucture.database.core.ProfessionRepository;
import com.rmr.dinosaurs.infrastucture.database.core.SurveyQuestionAnswerRepository;
import com.rmr.dinosaurs.infrastucture.database.core.SurveyQuestionRepository;
import com.rmr.dinosaurs.infrastucture.database.core.SurveyRepository;
import com.rmr.dinosaurs.infrastucture.database.userinfo.UserInfoRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

  private final SurveyEntityDtoMapper surveyMapper;
  private final ProfessionEntityDtoMapper professionMapper;

  private final SurveyRepository surveyRepo;
  private final SurveyQuestionRepository questionRepo;
  private final SurveyQuestionAnswerRepository answerRepo;
  private final ProfessionRepository professionRepo;
  private final UserInfoRepository userInfoRepo;

  private Long singletonSurveyId;

  @Override
  @Transactional
  public SurveyCreateDto addSurvey(SurveyCreateDto dto) {
    validateSurveyCreateDto(dto);

    Survey savedSurvey = saveAndFlushSurvey(dto);
    saveAndFlushSurveyQuestionsAndTheirsAnswers(savedSurvey, dto.getSurvey());

    singletonSurveyId = savedSurvey.getId();

    return dto;
  }

  @Override
  @Transactional
  public SurveyReadDto getSurvey() {
    Survey s;
    if (singletonSurveyId == null) {
      s = surveyRepo.findTop1By();
    } else {
      s = surveyRepo.findById(singletonSurveyId)
          .orElseThrow(() -> new ServiceException(SURVEY_NOT_FOUND));
    }

    SurveyReadDto dto = toReadSurveyDto(s);
    List<QuestionReadDto> sortedSurveyByQuestionId = dto.getSurvey().stream()
        .sorted(Comparator.comparingLong(QuestionReadDto::getQuestionId))
        .toList();
    dto.setSurvey(sortedSurveyByQuestionId);
    return dto;
  }

  @Override
  @Transactional
  public ProfessionDto resultSurvey(SurveyResponseDto surveyResponseDto) {
    var survey = surveyRepo.findById(surveyResponseDto.getSurveyId()).orElseThrow(
        () -> new ServiceException(SURVEY_NOT_FOUND));
    var providedAnswerIds = surveyResponseDto.getSurvey().stream()
        .map(SurveyResponseQuestionDto::getAnswerId)
        .toList();
    var answers = answerRepo.findAllById(providedAnswerIds);

    checkValidSurveyResponseContentProvided(surveyResponseDto, survey, providedAnswerIds);

    Profession recommendedProfession = recommendProfession(answers);
    attachRecommendedProfessionToCurrentUser(recommendedProfession);

    return professionMapper.toDto(recommendedProfession);
  }

  private void checkValidSurveyResponseContentProvided(SurveyResponseDto surveyResponseDto,
      Survey survey, List<Long> providedAnswerIds) {
    var providedQuestionsIds = surveyResponseDto.getSurvey().stream()
        .map(SurveyResponseQuestionDto::getQuestionId)
        .collect(Collectors.toSet());
    if (survey.getQuestions().stream().map(SurveyQuestion::getId)
        .noneMatch(providedQuestionsIds::contains)) {
      throw new ServiceException(SURVEY_RESPONSE_NOT_MATCHING_ACTUAL_SURVEY);
    }

    var surveyAnswersIds = survey.getQuestions().stream()
        .flatMap(surveyQuestion -> surveyQuestion.getAnswers().stream())
        .map(SurveyQuestionAnswer::getId)
        .collect(Collectors.toSet());
    if (!surveyAnswersIds.containsAll(providedAnswerIds)) {
      throw new ServiceException(SURVEY_RESPONSE_NOT_MATCHING_ACTUAL_SURVEY);
    }
  }

  private Survey saveAndFlushSurvey(SurveyCreateDto dto) {
    Survey s = surveyMapper.toSurvey(dto);
    Survey savedSurvey = surveyRepo.saveAndFlush(s);
    dto.setSurveyId(savedSurvey.getId());
    return savedSurvey;
  }

  private void saveAndFlushSurveyQuestionsAndTheirsAnswers(
      Survey survey, List<QuestionCreateDto> qdtoList) {

    Map<Long, Profession> professionCache = new HashMap<>();
    for (QuestionCreateDto qdto : qdtoList) {
      SurveyQuestion savedQuestion = saveAndFlushSurveyQuestion(survey, qdto);
      saveAndFlushSurveyQuestionAnswers(savedQuestion, qdto.getAnswers(), professionCache);
    }
  }

  private SurveyQuestion saveAndFlushSurveyQuestion(Survey survey, QuestionCreateDto qdto) {
    SurveyQuestion q = surveyMapper.toSurveyQuestion(qdto);
    q.setSurvey(survey);
    SurveyQuestion savedQuestion = questionRepo.saveAndFlush(q);
    qdto.setQuestionId(savedQuestion.getId());
    return savedQuestion;
  }

  private void saveAndFlushSurveyQuestionAnswers(
      SurveyQuestion question,
      List<AnswerCreateDto> adtoList,
      Map<Long, Profession> professionCache) {

    for (AnswerCreateDto adto : adtoList) {
      Long professionId = adto.getProfessionId();
      Profession profession = cacheProfessionOrGet(professionId, professionCache);
      saveAndFlushSurveyQuestionAnswer(question, profession, adto);
    }
  }

  private Profession cacheProfessionOrGet(
      Long professionId, Map<Long, Profession> professionCache) {

    if (!professionCache.containsKey(professionId)) {
      Profession cachingProfession = professionRepo.findById(professionId)
          .orElseThrow(() -> new ServiceException(PROFESSION_NOT_FOUND));
      professionCache.put(professionId, cachingProfession);
    }

    return professionCache.get(professionId);
  }

  private void saveAndFlushSurveyQuestionAnswer(
      SurveyQuestion question, Profession profession,
      AnswerCreateDto adto) {

    SurveyQuestionAnswer a = surveyMapper.toSurveyQuestionAnswer(adto);
    a.setQuestion(question);
    a.setProfession(profession);
    SurveyQuestionAnswer savedAnswer = answerRepo.saveAndFlush(a);
    adto.setAnswerId(savedAnswer.getId());
  }

  private SurveyReadDto toReadSurveyDto(Survey survey) {
    Set<SurveyQuestion> sqSet = survey.getQuestions();
    List<QuestionReadDto> qdtoList = new ArrayList<>(sqSet.size());
    for (SurveyQuestion sq : sqSet) {

      Set<SurveyQuestionAnswer> sqaSet = sq.getAnswers();
      List<AnswerReadDto> adtoList = new ArrayList<>(sqaSet.size());
      for (SurveyQuestionAnswer sqa : sqaSet) {
        adtoList.add(surveyMapper.toReadAnswerDto(sqa));
      }

      QuestionReadDto qdto = surveyMapper.toReadQuestionDto(sq);
      qdto.setAnswers(adtoList);
      qdtoList.add(qdto);
    }

    SurveyReadDto surveyDto = new SurveyReadDto();
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

  private void attachRecommendedProfessionToCurrentUser(Profession recommendedProfession) {
    var currentUserPrincipal = getCurrentUserPrincipal();
    userInfoRepo.findByUser_Email(currentUserPrincipal.getEmail())
        .ifPresent(user -> {
              user.setRecommendedProfession(recommendedProfession);
              userInfoRepo.save(user);
            }
        );
  }

  private void validateSurveyCreateDto(SurveyCreateDto dto) {
    for (QuestionCreateDto q : dto.getSurvey()) {
      for (AnswerCreateDto a : q.getAnswers()) {
        if (a.getProfessionId() == null) {
          throw new ServiceException(SURVEY_QUESTION_ANSWER_WITH_NO_PROFESSION_ID);
        }
      }
    }
  }

  private DinoPrincipal getCurrentUserPrincipal() {
    return (DinoPrincipal) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
  }

}
