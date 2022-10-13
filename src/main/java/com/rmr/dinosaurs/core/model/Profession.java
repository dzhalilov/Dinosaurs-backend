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
@Table(name = "profession")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profession {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  int id;

  @Column(name = "name", nullable = false, unique = true)
  String name;

  @Column(name = "cover_url", nullable = false)
  String coverUrl;

  @Column(name = "short_description", nullable = false)
  String shortDescription;

  @Column(name = "description", nullable = false)
  String description;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "ui_profession_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  Set<UserInfo> userInfos;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "sqa_profession_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  Set<SurveyQuestionAnswer> answers;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "cap_profession_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  Set<CourseAndProfession> courseAndProfessionRefs;

}
