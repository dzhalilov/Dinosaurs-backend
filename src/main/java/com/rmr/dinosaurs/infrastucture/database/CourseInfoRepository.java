package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.CourseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseInfoRepository extends JpaRepository<CourseInfo, Long> {

}
