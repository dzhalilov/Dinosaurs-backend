package com.rmr.dinosaurs.domain.auth.model;

import com.rmr.dinosaurs.domain.core.model.Authority;
import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "user_auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  Long id;

  @Column(name = "email", nullable = false, unique = true)
  String email;

  @Column(name = "password", nullable = false)
  String password;

  @Column(name = "role", nullable = false)
  Authority role;

  @Column(name = "isConfirmed", nullable = false)
  Boolean isConfirmed;

  @Column(name = "regesteredAt")
  LocalDateTime registeredAt;

  @Column(name = "isArchived", nullable = false)
  Boolean isArchived;

  @Column(name = "archivedAt")
  LocalDateTime archivedAt;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ui_user_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  UserInfo userInfo;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tc_user_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  TempConfirmation tempConfirmation;

}
