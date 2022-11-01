package com.rmr.dinosaurs.domain.auth.security.model;

import com.rmr.dinosaurs.domain.core.model.Authority;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
public class DinoAuthentication implements Authentication {

  private final transient DinoPrincipal dinoPrincipal;

  private String token;

  public DinoAuthentication(DinoPrincipal dinoPrincipal) {
    this.dinoPrincipal = dinoPrincipal;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(dinoPrincipal.getRole());
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return this.dinoPrincipal;
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    // skip
  }

  @Override
  public String getName() {
    return this.dinoPrincipal.getName();
  }

  public Long getId() {
    return this.dinoPrincipal.getId();
  }

  public void setId(Long id) {
    this.dinoPrincipal.setId(id);
  }

  public Authority getRole() {
    return this.dinoPrincipal.getRole();
  }

  public void setRole(Authority role) {
    this.dinoPrincipal.setRole(role);
  }

  public String getEmail() {
    return this.dinoPrincipal.getEmail();
  }

  public void setEmail(String email) {
    this.dinoPrincipal.setEmail(email);
  }

}
