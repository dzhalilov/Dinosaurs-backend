package com.rmr.dinosaurs.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  Long id;

  @Column(name = "description", nullable = false)
  String description;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ci_course_id", updatable = false)
  Course course;

}
