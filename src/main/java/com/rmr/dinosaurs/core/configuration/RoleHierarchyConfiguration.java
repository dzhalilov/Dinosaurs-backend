package com.rmr.dinosaurs.core.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class RoleHierarchyConfiguration {

  private final String roleHierarchyStr;


  @Autowired
  public RoleHierarchyConfiguration(@Value("${role.hierarchy}") String roleHierarchyStr) {
    this.roleHierarchyStr = roleHierarchyStr;
  }

  @Bean
  RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy(roleHierarchyStr);
    return roleHierarchy;
  }

}
