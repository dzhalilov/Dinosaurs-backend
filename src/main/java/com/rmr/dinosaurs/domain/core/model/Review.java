package com.rmr.dinosaurs.domain.core.model;

import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  Long id;

  @Column(name = "rating", nullable = false)
  @Min(1)
  @Max(5)
  Integer rating;

  @Column(name = "text_review")
  @Length(max = 1000)
  String textReview;

  @ManyToOne
  @JoinColumn(name = "course_id")
  Course course;

  @ManyToOne
  @JoinColumn(name = "user_info_id")
  UserInfo userInfo;

}
