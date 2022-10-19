package com.rmr.dinosaurs.core.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  Long id;

  @Column(name = "name", nullable = false)
  String name;

  @Column(name = "surname", nullable = false)
  String surname;

  @Column(name = "registered_at", nullable = false)
  LocalDateTime registeredAt;

  @Column(name = "is_confirmed_user", nullable = false)
  Boolean isConfirmedUser;

  @Column(name = "archived_at")
  LocalDateTime archivedAt;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "ui_user_id", updatable = false)
  User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ui_profession_id")
  Profession recommendedProfession;

}
