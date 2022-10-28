package com.rmr.dinosaurs.infrastucture.database.auth;

import com.rmr.dinosaurs.domain.auth.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmailIgnoreCase(String email);

  Optional<User> findByEmailIgnoreCaseAndIsConfirmedTrue(String email);

  List<User> findAllByIsConfirmedTrue();

}
