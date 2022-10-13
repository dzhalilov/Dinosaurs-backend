package com.rmr.dinosaurs.infrastucture.database;

import com.rmr.dinosaurs.core.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

}
