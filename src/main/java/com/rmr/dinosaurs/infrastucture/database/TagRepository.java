package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
