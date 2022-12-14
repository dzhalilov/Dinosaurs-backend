package com.rmr.dinosaurs.infrastucture.database.core;

import com.rmr.dinosaurs.domain.core.model.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  @Query("SELECT r FROM Review r where r.course.id =?1 and r.userInfo.id =?2")
  Optional<Review> findReviewByCourseAndUserInfoId(Long courseId, Long userInfoId);

  @Query("SELECT r FROM Review r where r.course.id =?1 order by r.created desc ")
  List<Review> findByCourseId(Long courseId);

}
