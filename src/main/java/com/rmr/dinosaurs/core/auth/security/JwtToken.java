package com.rmr.dinosaurs.core.auth.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

  @NonNull
  private String accessToken;

}
