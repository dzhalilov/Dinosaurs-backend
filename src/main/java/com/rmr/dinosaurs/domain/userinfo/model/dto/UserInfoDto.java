package com.rmr.dinosaurs.domain.userinfo.model.dto;

import com.rmr.dinosaurs.domain.core.model.Authority;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

  Long id;

  String email;

  Authority role;

  String name;

  String surname;

  Boolean isConfirmed;

  LocalDateTime registeredAt;

  Boolean isArchived;

  LocalDateTime archivedAt;

  Long userId;

  Long professionId;


}
