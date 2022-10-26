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
@Table(name = "survey_question_answer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyQuestionAnswer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  Long id;

  @Column(name = "text", nullable = false)
  String text;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sqa_question_id", nullable = false, updatable = false)
  SurveyQuestion question;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sqa_profession_id", nullable = false, updatable = false)
  Profession profession;

}
