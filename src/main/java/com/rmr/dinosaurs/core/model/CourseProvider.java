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
@Table(name = "course_provider")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseProvider {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  Long id;

  @Column(name = "name", nullable = false)
  String name;

  @Column(name = "url", nullable = false, unique = true)
  String url;

  @Column(name = "cover_url", nullable = false)
  String coverUrl;

  @Column(name = "short_description", nullable = true)
  String shortDescription;

  @Column(name = "description", nullable = false)
  String description;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "c_course_provider_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  Set<Course> courses;

}
