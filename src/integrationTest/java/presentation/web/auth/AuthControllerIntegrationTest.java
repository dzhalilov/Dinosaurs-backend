package presentation.web.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rmr.dinosaurs.DinosaursApplication;
import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.model.requests.LoginRequest;
import com.rmr.dinosaurs.domain.auth.model.requests.SignupRequest;
import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.domain.core.model.Authority;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.presentation.web.auth.AuthController;
import java.time.LocalDateTime;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import testcontainers.CustomPostgresContainer;


@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DinosaursApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class AuthControllerIntegrationTest {

  @Container
  private static final CustomPostgresContainer container = CustomPostgresContainer.getInstance();

  private static final String BASE_URL = "http://localhost";
  private static final String TEST_PASSWORD = "pAssw0rd";


  private final TestRestTemplate testRestTemplate = new TestRestTemplate();
  private final HttpHeaders requestHeaders = new HttpHeaders();
  // Using encrypted 'pAssw0rd' as password value
  private final User testUser = new User(123L, "test@email.com",
      "$2y$12$SHUzyNYC1vT57bbJLe/ub./N5z/Z2U6ENkWk9c2qkw5fjdKUJ25WO", Authority.ROLE_REGULAR,
      true, LocalDateTime.now(), false, null, null, null);

  @Autowired
  private AuthController authController;

  @Autowired
  private UserRepository userRepository;

  @LocalServerPort
  private int port;

  private String endpointUrl;

  @BeforeEach
  void setUp() {
    endpointUrl = BASE_URL + ":" + port + "/api/v1/auth";
    requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
    userRepository.save(testUser);
  }

  @AfterEach
  void cleanDb() {
    userRepository.deleteAll();
  }


  @Test
  @DisplayName("login as existing confirmed user")
  void testLogin() {
    // given
    var loginRequest = new LoginRequest(testUser.getEmail(), TEST_PASSWORD);
    var requestEntity = new HttpEntity<>(loginRequest, requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/login");

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST,
        requestEntity, JwtTokenPair.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    var actual = responseEntity.getBody();
    assertNotNull(actual);
    assertTrue(Strings.isNotEmpty(actual.getAccessToken()));
    assertTrue(Strings.isNotEmpty(actual.getRefreshToken()));
  }

  @Test
  @DisplayName("sign up as a new valid user")
  void testSignup() {
    // given
    var signupRequest = new SignupRequest("newuser@email.com", TEST_PASSWORD, "TestName",
        "TestSurname");
    final var usersInDbBefore = userRepository.findAll().size();
    var requestEntity = new HttpEntity<>(signupRequest, requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/signup");

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST,
        requestEntity, JwtTokenPair.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    var actual = responseEntity.getBody();
    assertNotNull(actual);
    assertThat(userRepository.findAll()).hasSize(usersInDbBefore + 1);
  }

  @Test
  @DisplayName("sign up as existing user")
  void testSignupAsExistingUser() {
    // given
    var signupRequest = new SignupRequest(testUser.getEmail(), TEST_PASSWORD, "TestName",
        "TestSurname");
    var requestEntity = new HttpEntity<>(signupRequest, requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/signup");

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST,
        requestEntity, JwtTokenPair.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("sign up with invalid user email")
  void testSignupWithInvalidEmail() {
    // given
    var signupRequest = new SignupRequest("123581321", TEST_PASSWORD, "TestName", "TestSurname");
    var requestEntity = new HttpEntity<>(signupRequest, requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/signup");

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST,
        requestEntity, JwtTokenPair.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("sign up with invalid user password")
  void testSignupWithInvalidPassword() {
    // given
    var signupRequest = new SignupRequest("correct@email.com", "abc", "Username", "Usersurname");
    var requestEntity = new HttpEntity<>(signupRequest, requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/signup");

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST,
        requestEntity, JwtTokenPair.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

}
