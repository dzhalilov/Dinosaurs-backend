package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.Profession;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long> {

  Optional<Profession> findByName(String professionName);

}
