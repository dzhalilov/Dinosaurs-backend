package com.rmr.dinosaurs.core.model;

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
@Table(name = "course_and_profession")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseAndProfession {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cap_course_id", updatable = false)
  Course course;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cap_profession_id", updatable = false)
  Profession profession;

}
