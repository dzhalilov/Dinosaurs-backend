package com.rmr.dinosaurs.domain.auth.security.model.dto;

import com.rmr.dinosaurs.domain.core.model.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DinoAuthenticationDto {

  private Long id;
  private String email;
  private Authority role;

}
