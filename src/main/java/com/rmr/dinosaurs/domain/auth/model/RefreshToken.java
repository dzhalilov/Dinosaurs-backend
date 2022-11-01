package com.rmr.dinosaurs.domain.auth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "refresh_token_id", unique = true, insertable = false,
      updatable = false, nullable = false)
  Long id;

  @Column(name = "value", nullable = false, unique = true)
  String value;

  @Column(name = "user_id", nullable = false, unique = true)
  Long userId;

}
