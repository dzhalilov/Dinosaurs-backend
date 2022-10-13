package com.rmr.dinosaurs.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_provider")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseProvider {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  int id;

  @Column(name = "name", nullable = false)
  String name;

  @Column(name = "url", nullable = false)
  String url;

  @Column(name = "cover_url", nullable = false)
  String coverUrl;

  @Column(name = "short_description", nullable = false)
  String shortDescription;

  @Column(name = "description", nullable = false)
  String description;

}
