package presentation.web;

import static com.rmr.dinosaurs.core.model.Authority.ROLE_MODERATOR;
import static com.rmr.dinosaurs.core.model.Authority.ROLE_REGULAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rmr.dinosaurs.DinosaursApplication;
import com.rmr.dinosaurs.core.auth.security.DinoAuthentication;
import com.rmr.dinosaurs.core.auth.security.DinoPrincipal;
import com.rmr.dinosaurs.core.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.core.auth.security.JwtTokenProvider;
import com.rmr.dinosaurs.core.model.User;
import com.rmr.dinosaurs.core.model.UserInfo;
import com.rmr.dinosaurs.core.model.dto.ShortUserInfoDto;
import com.rmr.dinosaurs.core.model.dto.UserInfoDto;
import com.rmr.dinosaurs.core.utils.converters.UserInfoConverter;
import com.rmr.dinosaurs.infrastucture.database.UserInfoRepository;
import com.rmr.dinosaurs.infrastucture.database.UserRepository;
import com.rmr.dinosaurs.presentation.web.UserInfoController;
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
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DinosaursApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserInfoControllerIntegrationTest {

  private static final String USER_TOKEN_HEADER = "X-USER-TOKEN";
  private static final String BASE_URL = "http://localhost";
  private static final String TEST_PASSWORD_ENCRYPTED =
      "$2y$12$SHUzyNYC1vT57bbJLe/ub./N5z/Z2U6ENkWk9c2qkw5fjdKUJ25WO";


  private final TestRestTemplate testRestTemplate = new TestRestTemplate();
  private final HttpHeaders requestHeaders = new HttpHeaders();
  // Using encrypted 'pAssw0rd' as password value
  private final User regularUser = new User(null, "regular@email.com", TEST_PASSWORD_ENCRYPTED,
      ROLE_REGULAR, null);
  private final User moderatorUser = new User(null, "moder@email.com", TEST_PASSWORD_ENCRYPTED,
      ROLE_MODERATOR, null);

  private final UserInfo testUserInfo1 = new UserInfo(null, "testName1", "testSurname1",
      LocalDateTime.now(), true, null, null, null);
  private final UserInfo testUserInfo2 = new UserInfo(null, "testName2", "testSurname2",
      LocalDateTime.now(), true, null, null, null);


  @Autowired
  private UserInfoController userInfoController;

  @Autowired
  private UserInfoRepository userInfoRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserInfoConverter userInfoConverter;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @LocalServerPort
  private int port;

  private String endpointUrl;


  @BeforeEach
  void setUp() {
    endpointUrl = BASE_URL + ":" + port + "/api/v1/profiles";
    requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
    List<User> users = userRepository.saveAll(List.of(regularUser, moderatorUser));
    testUserInfo1.setUser(users.get(0));
    testUserInfo2.setUser(users.get(1));
    userInfoRepository.saveAll(List.of(testUserInfo1, testUserInfo2));
  }

  @AfterEach
  void cleanDb() {
    userRepository.deleteAll();
    userInfoRepository.deleteAll();
  }

  @Test
  void shouldGetCurrentUserInfoDto() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    requestHeaders.add(USER_TOKEN_HEADER, jwtTokenPairFor.getAccessToken());
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/my");

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, UserInfoDto.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    var actual = responseEntity.getBody();
    assertNotNull(actual);
    assertThat(actual.getUserId()).isEqualTo(currentUser.getId());
    assertThat(actual.getEmail()).isEqualTo(currentUser.getEmail());
    assertThat(actual.getRole()).isEqualTo(currentUser.getRole());
    assertTrue(actual.getIsConfirmed());
  }

  @Test
  void shouldEditCurrentUserInfoDto() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    requestHeaders.add(USER_TOKEN_HEADER, jwtTokenPairFor.getAccessToken());
    UserInfo userInfo = userInfoRepository.findByUser(currentUser).orElseThrow();
    UserInfoDto expectedUserInfoDto = userInfoConverter.toUserInfoDto(userInfo);
    expectedUserInfoDto.setName("Franz");
    expectedUserInfoDto.setSurname("Kafka");
    var requestEntity = new HttpEntity<>(expectedUserInfoDto, requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/my");

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST,
        requestEntity, UserInfoDto.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    var actual = responseEntity.getBody();
    assertNotNull(actual);
    assertThat(actual).isEqualTo(expectedUserInfoDto);
    UserInfo actualUserInfoInDb = userInfoRepository.findByUser(currentUser).orElseThrow();
    assertThat(actualUserInfoInDb.getName()).isEqualTo(expectedUserInfoDto.getName());
    assertThat(actualUserInfoInDb.getSurname()).isEqualTo(expectedUserInfoDto.getSurname());
  }

  @Test
  void shouldResponseWithErrorOnEditAnotherUserInfoDto() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    requestHeaders.add(USER_TOKEN_HEADER, jwtTokenPairFor.getAccessToken());
    UserInfo anotherUserInfo = userInfoRepository.findByUser(moderatorUser).orElseThrow();
    var requestEntity = new HttpEntity<>(anotherUserInfo, requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/my");

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST,
        requestEntity, UserInfoDto.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldGetUserInfoDtoByIdWithModeratorPermission() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(moderatorUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    User testUser = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    requestHeaders.add(USER_TOKEN_HEADER, jwtTokenPairFor.getAccessToken());
    var requestEntity = new HttpEntity<>(requestHeaders);
    var testUserInfo = userInfoRepository.findByUser(testUser).orElseThrow();
    var expectedUserInfoDto = userInfoConverter.toUserInfoDto(testUserInfo);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/" + testUserInfo.getId());

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, UserInfoDto.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    var actual = responseEntity.getBody();
    assertNotNull(actual);
    assertThat(actual).isEqualTo(expectedUserInfoDto);
  }

  @Test
  void shouldGetAllUserInfosAsShortUserInfoDtoWithModeratorPermission() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(moderatorUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    requestHeaders.add(USER_TOKEN_HEADER, jwtTokenPairFor.getAccessToken());
    var expectedList = userInfoRepository.findAll()
        .stream().map(userInfoConverter::toShortUserInfoDto).toList();
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl);
    ParameterizedTypeReference<List<ShortUserInfoDto>> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {
        };

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, parameterizedTypeReference);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    var actual = responseEntity.getBody();
    assertThat(actual).isNotNull().isNotEmpty();
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expectedList);
  }

  @Test
  void shouldGetAllModeratorsAsShortUserInfoDtoWithModeratorPermission() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(moderatorUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    requestHeaders.add(USER_TOKEN_HEADER, jwtTokenPairFor.getAccessToken());
    var expectedList = userInfoRepository.findAll()
        .stream()
        .filter(userInfo -> ROLE_MODERATOR.equals(userInfo.getUser().getRole()))
        .map(userInfoConverter::toShortUserInfoDto).toList();
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/moderators");
    ParameterizedTypeReference<List<ShortUserInfoDto>> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {
        };

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, parameterizedTypeReference);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    var actual = responseEntity.getBody();
    assertThat(actual).isNotNull().isNotEmpty();
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expectedList);
  }

  @Test
  void shouldResponseForbiddenWhenRegularUserGetUserInfoById() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    requestHeaders.add(USER_TOKEN_HEADER, jwtTokenPairFor.getAccessToken());
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/100");

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, UserInfoDto.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.FORBIDDEN);
    var actual = responseEntity.getBody();
    assertNull(actual);
  }

  @Test
  void shouldResponseForbiddenWhenRegularUserGetUserInfos() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    requestHeaders.add(USER_TOKEN_HEADER, jwtTokenPairFor.getAccessToken());
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl);
    ParameterizedTypeReference<List<ShortUserInfoDto>> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {
        };

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, parameterizedTypeReference);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.FORBIDDEN);
    var actual = responseEntity.getBody();
    assertNull(actual);
  }

  @Test
  void shouldResponseForbiddenWhenRegularUserGetModerators() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    requestHeaders.add(USER_TOKEN_HEADER, jwtTokenPairFor.getAccessToken());
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/moderators");
    ParameterizedTypeReference<List<ShortUserInfoDto>> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {
        };

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, parameterizedTypeReference);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.FORBIDDEN);
    var actual = responseEntity.getBody();
    assertNull(actual);
  }


  private DinoAuthentication getDinoAuthentication(User user) {
    return new DinoAuthentication(new DinoPrincipal(user.getId(), user.getEmail(), user.getRole()));
  }

  private JwtTokenPair getJwtTokenPairForUser(User user) {
    return jwtTokenProvider.generateJwtTokenPair(getDinoAuthentication(user));
  }

}
