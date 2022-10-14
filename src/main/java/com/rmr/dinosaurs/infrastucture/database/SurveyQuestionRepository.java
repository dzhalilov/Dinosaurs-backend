package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {

}
