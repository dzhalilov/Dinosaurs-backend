package com.rmr.dinosaurs.core.model;

import java.time.LocalDateTime;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  int id;

  @Column(name = "title", nullable = false)
  String title;

  @Column(name = "url", nullable = false)
  String url;

  @Column(name = "cover_url", nullable = false)
  String coverUrl;

  @Column(name = "short_description", nullable = false)
  String shortDescription;

  @Column(name = "start_datetime", nullable = false)
  LocalDateTime startDatetime;

  @Column(name = "end_datetime", nullable = false)
  LocalDateTime endDatetime;

  @Column(name = "internal_rating", nullable = false)
  int internalRating;

  @Column(name = "is_indefinite", nullable = false)
  boolean isIndefinite;

  @Column(name = "is_for_advanced_students", nullable = false)
  boolean isForAdvancedStudents;

  @Column(name = "is_archived", nullable = false)
  boolean isArchived;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "c_course_provider_id", updatable = false)
  CourseProvider provider;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "cap_course_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  Set<CourseAndProfession> courseAndProfessionRefs;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "cat_course_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  Set<CourseAndTag> courseAndTagRefs;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ci_course_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  CourseInfo info;

}
