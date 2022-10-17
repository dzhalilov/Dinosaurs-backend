package com.rmr.dinosaurs.core.model.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {

  Long id;

  String name;

  String surname;

  LocalDateTime registeredAt;

  Boolean isConfirmed;

  LocalDateTime archivedAt;

  Integer userId;

  Integer professionId;

}
