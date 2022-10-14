package com.rmr.dinosaurs.core.auth.security;

import com.rmr.dinosaurs.core.model.Authority;
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
