package com.rmr.dinosaurs.infrastucture.database.core;

import com.rmr.dinosaurs.domain.core.model.CourseStudy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseStudyRepository extends JpaRepository<CourseStudy, Long> {

  @Query("select c from CourseStudy c where c.course.id =?1 and c.userInfo.id =?2")
  Optional<CourseStudy> findByCourseIdAndUserInfoId(Long courseId, Long userInfoId);

  @Query("select c from CourseStudy c where c.userInfo.id =?1")
  List<CourseStudy> findByUserInfoId(Long userInfoId);

  @Query("SELECT cs FROM CourseStudy cs"
      + " INNER JOIN CourseAndProfession cap ON cs.course.id = cap.course.id"
      + " INNER JOIN Profession p ON cap.profession.id = p.id"
      + " WHERE ("
      + "(:courseTitle is null) or ((lower(cs.course.title) LIKE %:courseTitle%))"
      + ")"
      + " and (:profession is null) or ((lower(p.name) LIKE %:profession%))"
      + " and ((:score is null) or (cs.score >= :score))"
      + " and ((:isFinished is null) or (:isFinished = false and cs.endsAt is null)"
      + " or (:isFinished = true and cs.endsAt is not null))"
      + " and ((cast(:endsAt as timestamp) is null) or (:endsAt <= cs.endsAt))")
  Page<CourseStudy> findByFilter(String courseTitle, String profession, Long score,
      LocalDateTime endsAt, Boolean isFinished, Pageable pageable);
}
