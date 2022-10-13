package com.rmr.dinosaurs.core.model;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "survey")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Survey {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  int id;

  @Column(name = "title", nullable = false)
  String title;

  @Column(name = "short_description", nullable = false)
  String shortDescription;

  @Column(name = "description", nullable = false)
  String description;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "sq_survey_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  Set<SurveyQuestion> questions;

}
