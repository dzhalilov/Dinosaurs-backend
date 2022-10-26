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
@Table(name = "course_and_tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseAndTag {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cat_tag_id", nullable = false, updatable = false)
  Tag tag;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cat_course_id", nullable = false, updatable = false)
  Course course;

}
