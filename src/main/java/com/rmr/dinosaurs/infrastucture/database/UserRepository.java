package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
