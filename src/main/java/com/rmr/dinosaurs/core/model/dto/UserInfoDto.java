package com.rmr.dinosaurs.core.model.dto;

import com.rmr.dinosaurs.core.model.Authority;
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

  LocalDateTime archivedAt;

  Long userId;

  Long professionId;


}
