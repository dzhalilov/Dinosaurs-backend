package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

}
