package com.rmr.dinosaurs.core.model;

import java.time.LocalDateTime;
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

  @Column(name = "start_msk_datetime", nullable = false)
  LocalDateTime startMskDatetime;

  @Column(name = "end_msk_datetime", nullable = false)
  LocalDateTime endMskDatetime;

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

}
