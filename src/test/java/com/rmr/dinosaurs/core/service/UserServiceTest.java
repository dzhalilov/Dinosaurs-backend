package com.rmr.dinosaurs.core.service;

import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import com.rmr.dinosaurs.domain.auth.service.impl.UserServiceImpl;
import com.rmr.dinosaurs.domain.auth.utils.converter.UserConverter;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private final User testUser = new User(1L, "correct@email.com", "stR4nGeRp4Ssw0rDHaHa",
      ROLE_ADMIN, true, LocalDateTime.now(), false, null, null, null);

  private final UserDto testUserDto = new UserDto(testUser.getId(), testUser.getEmail(),
      testUser.getRole(), testUser.isConfirmed(), testUser.getRegisteredAt(),
      testUser.getIsArchived(), null);

  @Mock
  private UserRepository userRepositoryMock;

  @Mock
  private UserConverter userConverterMock;

  @Mock
  private SecurityContext securityContextMock;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  void testGetAllUsers() {
    // given
    given(userRepositoryMock.findAll()).willReturn(List.of(testUser));
    given(userConverterMock.toUserDto(any(User.class))).willReturn(testUserDto);

    // when
    List<UserDto> actual = userService.getAllUsers();

    // then
    assertThat(actual).isNotNull().isNotEmpty().hasSameElementsAs(List.of(testUserDto));

    verify(userRepositoryMock).findAll();
    verify(userConverterMock).toUserDto(any(User.class));
    verifyNoMoreInteractions(userRepositoryMock, userConverterMock);
  }


}
