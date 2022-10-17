package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

}