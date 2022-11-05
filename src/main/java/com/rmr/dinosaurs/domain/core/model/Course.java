package com.rmr.dinosaurs.domain.core.model;

import lombok.*;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

  @Column(name = "average_rating", nullable = false)
  double averageRating;

  @Column(name = "votes")
  Long votes;

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

  @OneToMany(fetch = FetchType.LAZY)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  Set<Review> reviews;

  @PostConstruct
  void init() {
    votes = 0L;
    averageRating = 5.0;
  }

}
