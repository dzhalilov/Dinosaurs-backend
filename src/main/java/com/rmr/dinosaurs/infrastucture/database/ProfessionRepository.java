package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.Profession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Integer> {

}
