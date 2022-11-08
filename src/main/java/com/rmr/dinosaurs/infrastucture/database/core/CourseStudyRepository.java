package com.rmr.dinosaurs.infrastucture.database.core;

import com.rmr.dinosaurs.domain.core.model.CourseStudy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseStudyRepository extends JpaRepository<CourseStudy, Long> {

}
