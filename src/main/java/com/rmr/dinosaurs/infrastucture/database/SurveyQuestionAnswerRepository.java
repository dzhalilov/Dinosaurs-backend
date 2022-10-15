package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.SurveyQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyQuestionAnswerRepository
    extends JpaRepository<SurveyQuestionAnswer, Long> {

}
