package com.rmr.dinosaurs.infrastucture.database.core;

import com.rmr.dinosaurs.domain.core.model.CourseStudy;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseStudyRepository extends JpaRepository<CourseStudy, Long> {

  @Query("select c from CourseStudy c where c.course.id =?1 and c.userInfo.id =?2")
  Optional<CourseStudy> findByCourseIdAndUserInfoId(Long courseId, Long userInfoId);

  @Query("select c from CourseStudy c where c.userInfo.id =?1")
  List<CourseStudy> findByUserInfoId(Long userInfoId);

}
