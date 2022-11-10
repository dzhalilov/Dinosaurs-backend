package com.rmr.dinosaurs.infrastucture.database.statistics;

import com.rmr.dinosaurs.domain.statistics.model.CourseLinkTransition;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseLinkTransitionRepository extends JpaRepository<CourseLinkTransition, UUID> {

}
