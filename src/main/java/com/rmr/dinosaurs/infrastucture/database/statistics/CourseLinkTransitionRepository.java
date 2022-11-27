package com.rmr.dinosaurs.infrastucture.database.statistics;

import com.rmr.dinosaurs.domain.statistics.model.CourseLinkTransition;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionsUniqueStatisticsDto;
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
      + ") ORDER BY ctl.transitionedAt ASC ")
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
      + ") ORDER BY ctl.transitionedAt ASC ")
  List<CourseLinkTransition> getAllByFilterAsList(Set<Long> coursesIds, String userEmail,
      LocalDateTime transitionedFrom, LocalDateTime transitionedTo);

  @Query("SELECT ct.course.id as courseId,"
      + " count(distinct ct.user.id) as transitionsCount"
      + " FROM CourseLinkTransition ct"
      + " INNER JOIN Course c on c.id = ct.course.id"
      + " INNER JOIN User u on ct.user.id= u.id"
      + " WHERE ("
      + " (((:coursesIds) is null) or (c.id IN (:coursesIds)))"
      + " and (cast(:transitionedFrom as timestamp) <= ct.transitionedAt)"
      + " and (cast(:transitionedTo as timestamp) >= ct.transitionedAt))"
      + " GROUP BY courseId")
  List<CourseLinkTransitionsUniqueStatisticsDto> getUniqueStatisticsByFilter(Set<Long> coursesIds,
      LocalDateTime transitionedFrom, LocalDateTime transitionedTo);

}
