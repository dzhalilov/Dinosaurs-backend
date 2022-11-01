package com.rmr.dinosaurs.core.utils;

import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_REGULAR;
import static org.assertj.core.api.Assertions.assertThat;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import com.rmr.dinosaurs.domain.auth.utils.converter.impl.UserConverterImpl;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserConverterTest {

  @InjectMocks
  private UserConverterImpl userConverter;

  @Test
  void testToUserDto() {
    // given
    User testUser = new User(1L, "super@email.com", "stR4nGeRp4Ssw0rDHaHa",
        ROLE_REGULAR, true, LocalDateTime.now(), false, null, null, null);

    // when
    UserDto actual = userConverter.toUserDto(testUser);

    // then
    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isNotNull().isEqualTo(testUser.getId());
    assertThat(actual.getEmail()).isNotNull().isEqualTo(testUser.getEmail());
    assertThat(actual.getRole()).isNotNull().isEqualTo(testUser.getRole());
    assertThat(actual.getIsConfirmed()).isNotNull().isEqualTo(testUser.getIsConfirmed());
    assertThat(actual.getRegisteredAt()).isNotNull()
        .isBetween(LocalDateTime.now().minus(1, ChronoUnit.MINUTES), LocalDateTime.now());
    assertThat(actual.getIsArchived()).isNotNull().isEqualTo(testUser.getIsArchived());
    assertThat(actual.getArchivedAt()).isNull();
  }

}
