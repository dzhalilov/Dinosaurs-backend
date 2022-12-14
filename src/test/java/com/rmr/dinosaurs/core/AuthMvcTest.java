package com.rmr.dinosaurs.core;

import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_REGULAR;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import com.rmr.dinosaurs.domain.auth.model.requests.LoginRequest;
import com.rmr.dinosaurs.domain.auth.model.requests.SignupRequest;
import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.domain.auth.service.AuthService;
import com.rmr.dinosaurs.presentation.web.auth.AuthController;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = AuthController.class)
class AuthMvcTest {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  private MockMvc mockMvc;
  @MockBean
  private AuthService authServiceMock;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  @DisplayName("valid login")
  void shouldLogin() throws Exception {
    // given
    var loginRequest = new LoginRequest("super@email.com", "p4sSwoRd");
    var jwtTokenPair = new JwtTokenPair("ttttttttto.ke.n", "mokkk.eee.n");
    var loginRequestAsString = objectMapper.writeValueAsString(loginRequest);
    given(authServiceMock.login(loginRequest))
        .willReturn(jwtTokenPair);

    // when
    mockMvc.perform(post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginRequestAsString))

        // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(jwtTokenPair)));

    verify(authServiceMock, times(1)).login(loginRequest);
    verifyNoMoreInteractions(authServiceMock);
  }

  @Test
  @DisplayName("valid signup")
  void shouldSignup() throws Exception {
    // given
    var signupRequest = new SignupRequest(
        "super@email.com",
        "p4sSwoRd",
        "My",
        "Name"
    );
    UserDto userDto = new UserDto(1L, signupRequest.email(), ROLE_REGULAR,
        false, LocalDateTime.now(ZoneOffset.UTC), false, null);
    var signupRequestAsString = objectMapper.writeValueAsString(signupRequest);
    given(authServiceMock.signup(signupRequest))
        .willReturn(userDto);

    // when
    mockMvc.perform(post("/api/v1/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(signupRequestAsString))

        // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(userDto)));

    verify(authServiceMock).signup(signupRequest);
    verifyNoMoreInteractions(authServiceMock);
  }

}
