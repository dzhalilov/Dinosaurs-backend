package com.rmr.dinosaurs.domain.core.model;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {

  ROLE_ADMIN, ROLE_MODERATOR, ROLE_REGULAR;

  @Override
  public String getAuthority() {
    return this.toString();
  }

}
