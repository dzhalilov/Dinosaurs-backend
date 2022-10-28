package com.rmr.dinosaurs.infrastucture.database.core;

import com.rmr.dinosaurs.domain.core.model.CourseAndTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseAndTagRepository extends JpaRepository<CourseAndTag, Long> {

  void deleteAllByCourse_Id(Long courseId);

}
