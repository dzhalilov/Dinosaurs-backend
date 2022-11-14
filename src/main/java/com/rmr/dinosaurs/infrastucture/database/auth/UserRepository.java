package com.rmr.dinosaurs.infrastucture.database.auth;

import com.rmr.dinosaurs.domain.auth.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmailIgnoreCase(String email);

  Optional<User> findByIdAndIsConfirmedTrueAndIsArchivedFalse(Long id);

  List<User> findAllByIsConfirmedTrue();

  @Modifying
  @Query(value = "DELETE FROM user_auth"
      + " WHERE ((is_confirmed = false)"
      + " AND (regestered_at <= cast(:localDateTime as timestamp)))",
      nativeQuery = true)
  void deleteAllNotConfirmedEmailBefore(LocalDateTime localDateTime);

}
