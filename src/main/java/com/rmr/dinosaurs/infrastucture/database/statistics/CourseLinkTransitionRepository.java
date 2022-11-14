package com.rmr.dinosaurs.infrastucture.database.statistics;

import com.rmr.dinosaurs.domain.statistics.model.CourseLinkTransition;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseLinkTransitionRepository extends JpaRepository<CourseLinkTransition, UUID> {

  @Query("SELECT ctl FROM CourseLinkTransition ctl"
      + " INNER JOIN Course c on ctl.course.id = c.id"
      + " INNER JOIN User u on ctl.user.id = u.id"
      + " WHERE ("
      + " (((:coursesIds) is null) or (c.id IN (:coursesIds)))"
      + " and ((:userEmail is null) or ((lower(u.email) LIKE lower(:userEmail))))"
      + " and (cast(:transitionedFrom as timestamp) <= ctl.transitionedAt)"
      + " and (cast(:transitionedTo as timestamp) >= ctl.transitionedAt)"
      + ")")
  Page<CourseLinkTransition> findAllByFilter(Set<Long> coursesIds, String userEmail,
      LocalDateTime transitionedFrom, LocalDateTime transitionedTo,
      Pageable pageable);

  @Query("SELECT ctl FROM CourseLinkTransition ctl"
      + " INNER JOIN Course c on ctl.course.id = c.id"
      + " INNER JOIN User u on ctl.user.id = u.id"
      + " WHERE ("
      + " (((:coursesIds) is null) or (c.id IN (:coursesIds)))"
      + " and ((:userEmail is null) or ((lower(u.email) LIKE lower(:userEmail))))"
      + " and (cast(:transitionedFrom as timestamp) <= ctl.transitionedAt)"
      + " and (cast(:transitionedTo as timestamp) >= ctl.transitionedAt)"
      + ")")
  List<CourseLinkTransition> getAllByFilterAsList(Set<Long> coursesIds, String userEmail,
      LocalDateTime transitionedFrom, LocalDateTime transitionedTo);

}
