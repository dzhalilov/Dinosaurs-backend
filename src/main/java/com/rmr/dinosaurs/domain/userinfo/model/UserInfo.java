package com.rmr.dinosaurs.domain.userinfo.model;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.core.model.Profession;
import com.rmr.dinosaurs.domain.core.model.Review;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "user_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  Long id;

  @Column(name = "name", nullable = false)
  String name;

  @Column(name = "surname", nullable = false)
  String surname;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "ui_user_id", updatable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ui_profession_id")
  Profession recommendedProfession;

  @OneToMany(fetch = FetchType.LAZY)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  Set<Review> reviews;

  public UserInfo(Long id, String name, String surname, User user,
      Profession recommendedProfession) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.user = user;
    this.recommendedProfession = recommendedProfession;
  }
}
