package com.rmr.dinosaurs.core.model.dto;

import com.rmr.dinosaurs.core.model.Authority;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortUserInfoDto {

  Long id;

  String email;

  Authority role;

  String name;

  Long userId;

}
