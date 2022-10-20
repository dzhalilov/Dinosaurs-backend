package com.rmr.dinosaurs.core.utils;

import static com.rmr.dinosaurs.core.model.Authority.ROLE_REGULAR;
import static org.assertj.core.api.Assertions.assertThat;

import com.rmr.dinosaurs.core.model.User;
import com.rmr.dinosaurs.core.model.dto.UserDto;
import com.rmr.dinosaurs.core.utils.converters.impl.UserConverterImpl;
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
        ROLE_REGULAR, null);

    // when
    UserDto actual = userConverter.toUserDto(testUser);

    // then
    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isNotNull().isEqualTo(testUser.getId());
    assertThat(actual.getEmail()).isNotNull().isEqualTo(testUser.getEmail());
    assertThat(actual.getRole()).isNotNull().isEqualTo(testUser.getRole());
  }

}
