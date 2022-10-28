package com.rmr.dinosaurs.domain.auth.model;

import java.time.LocalDateTime;
import java.util.UUID;
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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "tempconfirmation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempConfirmation {

  @Id
  @Type(type = "pg-uuid")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "issuedAt", nullable = false)
  private LocalDateTime issuedAt;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "tc_user_id", unique = true)
  @OnDelete(action = OnDeleteAction.CASCADE)
  User user;

}
