package com.rmr.dinosaurs.infrastucture.database.core;

import com.rmr.dinosaurs.domain.core.model.CourseProvider;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseProviderRepository extends JpaRepository<CourseProvider, Long> {

  Optional<CourseProvider> findByUrl(String url);

  Page<CourseProvider> findByOrderByNameAsc(Pageable pageable);

}
