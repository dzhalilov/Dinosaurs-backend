package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

  @Query("SELECT c FROM Course c " +
      "WHERE (:search is null " +
      "or (lower(c.title) LIKE %:search% " +
      "or lower(c.description) LIKE %:search%))" +
      "and ((:isAdvanced is null) or (c.isAdvanced = :isAdvanced))")
  Page<Course> findByFilter(
      @Param("search") String search,
      @Param("isAdvanced") Boolean isAdvanced,
      Pageable page);

}
