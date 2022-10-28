package com.rmr.dinosaurs.domain.core.model;

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
  Long id;

  @Column(name = "title", nullable = false)
  String title;

  @Column(name = "url", nullable = false)
  String url;

  @Column(name = "cover_url", nullable = false)
  String coverUrl;

  @Column(name = "short_description", nullable = true)
  String shortDescription;

  @Column(name = "description", nullable = false, length = 4096)
  String description;

  @Column(name = "starts_at", nullable = false)
  LocalDateTime startsAt;

  @Column(name = "ends_at", nullable = false)
  LocalDateTime endsAt;

  @Column(name = "internal_rating", nullable = false)
  Integer internalRating;

  @Column(name = "is_indefinite", nullable = false)
  Boolean isIndefinite;

  @Column(name = "is_advanced", nullable = false)
  Boolean isAdvanced;

  @Column(name = "is_archived", nullable = false)
  Boolean isArchived;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "c_course_provider_id", nullable = false, updatable = true)
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

}
