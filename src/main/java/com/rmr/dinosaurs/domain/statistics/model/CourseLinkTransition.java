package com.rmr.dinosaurs.domain.statistics.model;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.core.model.Course;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "course_transition")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseLinkTransition {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  UUID id;

  @ManyToMany(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  List<Course> courses;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "user_id")
  User user;

  @CreationTimestamp
  @Column(name = "transitionedAt", nullable = false)
  LocalDateTime transitionedAt;

}
