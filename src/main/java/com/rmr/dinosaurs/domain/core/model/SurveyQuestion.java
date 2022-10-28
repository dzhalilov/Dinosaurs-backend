package com.rmr.dinosaurs.domain.core.model;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "survey_question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyQuestion {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  Long id;

  @Column(name = "text", nullable = false)
  String text;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sq_survey_id", nullable = false, updatable = false)
  Survey survey;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "sqa_question_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  Set<SurveyQuestionAnswer> answers;

}
