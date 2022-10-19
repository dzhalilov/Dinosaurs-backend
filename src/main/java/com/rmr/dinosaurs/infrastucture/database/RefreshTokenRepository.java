package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByValue(String value);

  Optional<RefreshToken> findByUserId(Long id);

}
