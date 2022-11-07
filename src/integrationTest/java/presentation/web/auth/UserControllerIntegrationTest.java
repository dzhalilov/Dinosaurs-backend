package presentation.web.auth;

import static com.rmr.dinosaurs.domain.auth.exception.errorcode.AuthErrorCode.ACCESS_DENIED_EXCEPTION;
import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_ADMIN;
import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_MODERATOR;
import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_REGULAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.rmr.dinosaurs.DinosaursApplication;
import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.model.dto.UserDto;
import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.domain.auth.security.model.DinoAuthentication;
import com.rmr.dinosaurs.domain.auth.security.model.DinoPrincipal;
import com.rmr.dinosaurs.domain.auth.security.service.JwtTokenProvider;
import com.rmr.dinosaurs.domain.auth.utils.converter.UserConverter;
import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.presentation.web.auth.UserController;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
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
class UserControllerIntegrationTest {

  @Container
  private static final CustomPostgresContainer container = CustomPostgresContainer.getInstance();

  private static final String BASE_URL = "http://localhost";
  private static final String TEST_PASSWORD_ENCRYPTED =
      "$2y$12$SHUzyNYC1vT57bbJLe/ub./N5z/Z2U6ENkWk9c2qkw5fjdKUJ25WO";


  private final TestRestTemplate testRestTemplate = new TestRestTemplate();
  private final HttpHeaders requestHeaders = new HttpHeaders();
  // Using encrypted 'pAssw0rd' as password value
  private final User regularUser = new User(null, "regular@email.com", TEST_PASSWORD_ENCRYPTED,
      ROLE_REGULAR, true, LocalDateTime.now(), false, null, null, null);
  private final User moderatorUser = new User(null, "moder@email.com", TEST_PASSWORD_ENCRYPTED,
      ROLE_MODERATOR, true, LocalDateTime.now(), false, null, null, null);
  private final User adminUser = new User(null, "admin@email.com", TEST_PASSWORD_ENCRYPTED,
      ROLE_ADMIN, true, LocalDateTime.now(), false, null, null, null);

  @Autowired
  private UserController userController;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserConverter userConverter;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @LocalServerPort
  private int port;

  private String endpointUrl;


  @BeforeEach
  void setUp() {
    endpointUrl = BASE_URL + ":" + port + "/api/v1/users";
    requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
    userRepository.saveAll(List.of(regularUser, moderatorUser, adminUser));
  }

  @AfterEach
  void cleanDb() {
    userRepository.deleteAll();
  }


  @Test
  void whenGetCurrentUserDataThenCurrentUserDtoReturned() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    requestHeaders.add("X-USER-TOKEN", jwtTokenPairFor.getAccessToken());
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/me");
    var expectedUserDto = userConverter.toUserDto(currentUser);

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, UserDto.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    var actual = responseEntity.getBody();
    assertNotNull(actual);
    assertThat(actual).isEqualTo(expectedUserDto);
  }

  @Test
  void whenGetUserByIdThenUserWithSuchIdReturn() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(moderatorUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    var userToBeFound = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    requestHeaders.add("X-USER-TOKEN", jwtTokenPairFor.getAccessToken());
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/" + userToBeFound.getId());
    var expectedUserDto = userConverter.toUserDto(userToBeFound);

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, UserDto.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    var actual = responseEntity.getBody();
    assertNotNull(actual);
    assertThat(actual).isEqualTo(expectedUserDto);
  }

  @Test
  void whenGetUserByEmailAsAdministratorThenUserWithSuchEmailReturn() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(adminUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    var userToBeFound = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    requestHeaders.add("X-USER-TOKEN", jwtTokenPairFor.getAccessToken());
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/find")
        .queryParam("email", userToBeFound.getEmail());
    var expectedUserDto = userConverter.toUserDto(userToBeFound);

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, UserDto.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    var actual = responseEntity.getBody();
    assertNotNull(actual);
    assertThat(actual).isEqualTo(expectedUserDto);
  }

  @Test
  void whenGetAllUsersThenAllUsersFromReturn() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(moderatorUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    List<User> users = userRepository.findAll();
    requestHeaders.add("X-USER-TOKEN", jwtTokenPairFor.getAccessToken());
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl);
    List<UserDto> expectedUsers = users.stream().map(userConverter::toUserDto).toList();

    ParameterizedTypeReference<List<UserDto>> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {
        };

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, parameterizedTypeReference);

    // then
    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    List<UserDto> actual = responseEntity.getBody();
    assertThat(actual).isNotNull().isNotEmpty().hasSameElementsAs(expectedUsers);
  }

  @Test
  void whenSetUserAsModeratorThenUserWithModeratorRoleReturned() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(adminUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    var testUser = regularUser;

    requestHeaders.add("X-USER-TOKEN", jwtTokenPairFor.getAccessToken());
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/" + testUser.getId())
        .queryParam("isModerator", true);

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT,
        requestEntity, UserDto.class);

    // then
    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    var actual = responseEntity.getBody();
    assertNotNull(actual);
    assertThat(actual.getId()).isEqualTo(testUser.getId());
    assertThat(actual.getEmail()).isEqualTo(testUser.getEmail());
    assertThat(actual.getRole()).isEqualTo(ROLE_MODERATOR);
    var actualUserInDb = userRepository.findByEmailIgnoreCase(testUser.getEmail()).orElseThrow();
    assertThat(actualUserInDb.getRole()).isEqualTo(ROLE_MODERATOR);
  }

  @Test
  void whenUnsetModeratorThenRegularUserReturned() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(adminUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    var testUser = moderatorUser;

    requestHeaders.add("X-USER-TOKEN", jwtTokenPairFor.getAccessToken());
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/" + testUser.getId())
        .queryParam("isModerator", false);

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT,
        requestEntity, UserDto.class);

    // then
    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    var actual = responseEntity.getBody();
    assertNotNull(actual);
    assertThat(actual.getId()).isEqualTo(testUser.getId());
    assertThat(actual.getEmail()).isEqualTo(testUser.getEmail());
    assertThat(actual.getRole()).isEqualTo(ROLE_REGULAR);
    var actualUserInDb = userRepository.findByEmailIgnoreCase(testUser.getEmail()).orElseThrow();
    assertThat(actualUserInDb.getRole()).isEqualTo(ROLE_REGULAR);
  }

  @Test
  void whenModeratorSetUserModeratorThenResponse403() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(moderatorUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    var testUser = regularUser;

    requestHeaders.add("X-USER-TOKEN", jwtTokenPairFor.getAccessToken());
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/" + testUser.getId())
        .queryParam("isModerator", true);

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT,
        requestEntity, ServiceException.class);

    // then
    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.FORBIDDEN);
    var actual = responseEntity.getBody();
    assertNotNull(actual);
    assertThat(actual.getCode()).isEqualTo(ACCESS_DENIED_EXCEPTION.getErrorName());
  }


  private DinoAuthentication getDinoAuthentication(User user) {
    return new DinoAuthentication(new DinoPrincipal(user.getId(), user.getEmail(), user.getRole()));
  }

  private JwtTokenPair getJwtTokenPairForUser(User user) {
    return jwtTokenProvider.generateJwtTokenPair(getDinoAuthentication(user));
  }

}
