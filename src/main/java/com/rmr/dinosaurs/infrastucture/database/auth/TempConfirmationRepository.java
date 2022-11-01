package com.rmr.dinosaurs.infrastucture.database.auth;

import com.rmr.dinosaurs.domain.auth.model.TempConfirmation;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempConfirmationRepository extends JpaRepository<TempConfirmation, UUID> {

  void deleteAllByIssuedAtBefore(LocalDateTime before);

}
