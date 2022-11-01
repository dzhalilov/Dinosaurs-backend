package com.rmr.dinosaurs.domain.auth.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenPair {

  @NonNull
  private String accessToken;

  @NonNull
  private String refreshToken;

}
