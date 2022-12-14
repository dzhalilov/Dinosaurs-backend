package com.rmr.dinosaurs.domain.core.model;

import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "review", uniqueConstraints = {
    @UniqueConstraint(name = "one_vote_for_user_per_course",
        columnNames = {"course_id", "user_info_id"})},
    indexes = {@Index(name = "fn_course_id", columnList = "course_id")})
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
  @CreationTimestamp
  private LocalDateTime created;

}
