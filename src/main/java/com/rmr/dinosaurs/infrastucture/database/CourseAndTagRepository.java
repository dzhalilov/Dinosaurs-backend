package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.CourseAndTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseAndTagRepository extends JpaRepository<CourseAndTag, Long> {

}
