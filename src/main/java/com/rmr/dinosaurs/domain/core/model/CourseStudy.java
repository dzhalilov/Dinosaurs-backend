package com.rmr.dinosaurs.domain.core.model;

import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_study", uniqueConstraints = {
    @UniqueConstraint(name = "unique_course_per_user",
        columnNames = {"course_id", "user_info_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseStudy {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "starts_at", nullable = false)
  private LocalDateTime startsAt;

  @Column(name = "ends_at")
  private LocalDateTime endsAt;

  @Column(name = "score", nullable = true)
  @PositiveOrZero
  private Long score;

  @ManyToOne
  @JoinColumn(name = "user_info_id")
  private UserInfo userInfo;

  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;

}
