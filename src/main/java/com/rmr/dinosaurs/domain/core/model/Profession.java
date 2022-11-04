package com.rmr.dinosaurs.domain.core.model;

import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
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

import lombok.*;

@Entity
@Table(name = "profession")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profession {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  Long id;

  @Column(name = "name", nullable = false, unique = true)
  String name;

  @Column(name = "cover_url", nullable = true)
  String coverUrl;

  @Column(name = "short_description", nullable = true)
  String shortDescription;

  @Column(name = "description", nullable = true, length = 4096)
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
