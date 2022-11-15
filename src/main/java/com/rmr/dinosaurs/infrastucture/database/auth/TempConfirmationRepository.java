package com.rmr.dinosaurs.infrastucture.database.auth;

import com.rmr.dinosaurs.domain.auth.model.TempConfirmation;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TempConfirmationRepository extends JpaRepository<TempConfirmation, UUID> {

  @Modifying
  @Query(value = "DELETE FROM tempconfirmation"
      + " WHERE (issued_at <= cast(:localDateTime as timestamp))",
      nativeQuery = true)
  void deleteAllIssuedBefore(LocalDateTime localDateTime);

}
