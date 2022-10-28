package com.rmr.dinosaurs.domain.userinfo.model.dto;

import com.rmr.dinosaurs.domain.core.model.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortUserInfoDto {

  Long id;

  String email;

  Authority role;

  String name;

  Long userId;

}
