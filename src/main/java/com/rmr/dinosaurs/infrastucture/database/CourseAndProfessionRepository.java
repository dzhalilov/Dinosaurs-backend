package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.CourseAndProfession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseAndProfessionRepository
    extends JpaRepository<CourseAndProfession, Long> {

  boolean existsByCourse_IdAndProfession_Id(Long courseId, Long professionId);

}
