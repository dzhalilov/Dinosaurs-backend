package com.rmr.dinosaurs.core.utils;

import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_REGULAR;
import static org.assertj.core.api.Assertions.assertThat;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import com.rmr.dinosaurs.domain.userinfo.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.domain.userinfo.model.dto.UserInfoDto;
import com.rmr.dinosaurs.domain.userinfo.utils.converter.impl.UserInfoConverterImpl;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserInfoConverterTest {

  private final User testUser = new User(1L, "super@email.com", "stR4nGeRp4Ssw0rDHaHa",
      ROLE_REGULAR, true, LocalDateTime.now(), false, null, null, null);

  @InjectMocks
  private UserInfoConverterImpl userInfoConverter;

  @Test
  void testToUserInfoDto() {
    // given
    UserInfo testUserInfo = new UserInfo(2L, "James", "Gosling", testUser, null);

    // when
    UserInfoDto actual = userInfoConverter.toUserInfoDto(testUserInfo);

    // then
    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isNotNull().isEqualTo(testUserInfo.getId());
    assertThat(actual.getEmail()).isNotNull().isEqualTo(testUser.getEmail());
    assertThat(actual.getRole()).isNotNull().isEqualTo(testUser.getRole());
    assertThat(actual.getName()).isNotNull().isEqualTo(testUserInfo.getName());
    assertThat(actual.getSurname()).isNotNull().isEqualTo(testUserInfo.getSurname());
    assertThat(actual.getIsConfirmed()).isNotNull().isEqualTo(testUser.getIsConfirmed());
    assertThat(actual.getRegisteredAt()).isNotNull().isEqualTo(testUser.getRegisteredAt());
    assertThat(actual.getArchivedAt()).isNull();
    assertThat(actual.getUserId()).isNotNull().isEqualTo(testUser.getId());
    assertThat(actual.getProfessionId()).isNull();
  }

  @Test
  void testToShortUserInfoDto() {
    // given
    UserInfo testUserInfo = new UserInfo(2L, "James", "Gosling", testUser, null);

    // when
    ShortUserInfoDto actual = userInfoConverter.toShortUserInfoDto(testUserInfo);

    // then
    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isNotNull().isEqualTo(testUserInfo.getId());
    assertThat(actual.getEmail()).isNotNull().isEqualTo(testUser.getEmail());
    assertThat(actual.getRole()).isNotNull().isEqualTo(testUser.getRole());
    assertThat(actual.getName()).isNotNull().isEqualTo(testUserInfo.getName());
    assertThat(actual.getUserId()).isNotNull().isEqualTo(testUser.getId());
  }


}
