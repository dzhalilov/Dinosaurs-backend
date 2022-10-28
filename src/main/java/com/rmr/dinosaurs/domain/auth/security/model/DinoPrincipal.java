package com.rmr.dinosaurs.domain.auth.security.model;

import com.rmr.dinosaurs.domain.core.model.Authority;
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
