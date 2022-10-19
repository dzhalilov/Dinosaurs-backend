package com.rmr.dinosaurs.core.model.dto;

import com.rmr.dinosaurs.core.model.Authority;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {

  Long id;

  String email;

  Authority role;

  String name;

  String surname;

  Boolean isConfirmed;

  LocalDateTime registeredAt;

  LocalDateTime archivedAt;

  Long userId;

  Long professionId;


}
