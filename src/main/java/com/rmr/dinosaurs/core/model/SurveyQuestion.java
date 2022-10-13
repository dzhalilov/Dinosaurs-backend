package com.rmr.dinosaurs.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "survey_question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyQuestion {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  int id;

  @Column(name = "text", nullable = false)
  String text;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sq_survey_id", updatable = false)
  Survey survey;

}
