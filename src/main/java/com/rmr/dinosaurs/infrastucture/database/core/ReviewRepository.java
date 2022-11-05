package com.rmr.dinosaurs.infrastucture.database.core;

import com.rmr.dinosaurs.domain.core.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  @Query("select r from Review r where r.course.id =?1 and r.userInfo.id =?2")
  Optional<Review> findReviewByCourseAndUserInfoId(Long courseId, Long InfoId);
}
