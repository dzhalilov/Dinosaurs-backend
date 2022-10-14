package com.rmr.dinosaurs.core.auth.security;

import com.rmr.dinosaurs.core.model.Authority;
import java.security.Principal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DinoPrincipal implements Principal {

  private Long id;
  private String email;
  private Authority role;

  @Override
  public String getName() {
    return this.email;
  }
}
