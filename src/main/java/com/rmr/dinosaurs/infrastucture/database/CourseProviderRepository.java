package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.CourseProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseProviderRepository extends JpaRepository<CourseProvider, Long> {

  Optional<CourseProvider> findByUrl(String url);

}