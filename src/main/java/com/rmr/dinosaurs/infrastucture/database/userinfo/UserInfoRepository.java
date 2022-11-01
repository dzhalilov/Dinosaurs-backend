package com.rmr.dinosaurs.infrastucture.database.userinfo;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

  Optional<UserInfo> findByUser(User user);

  Optional<UserInfo> findByUser_Email(String email);

}
