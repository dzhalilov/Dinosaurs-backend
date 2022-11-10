package com.rmr.dinosaurs.infrastucture.database.core;

import com.rmr.dinosaurs.domain.core.model.Profession;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long> {

  Optional<Profession> findByName(String professionName);

  Page<Profession> findByOrderByNameAsc(Pageable pageable);

  @Query("SELECT p FROM Profession p "
      + " INNER JOIN CourseAndProfession cap ON cap.profession.id = p.id"
      + " WHERE cap.course.id = :courseId")
  Set<Profession> findByCourseId(Long courseId);
}
