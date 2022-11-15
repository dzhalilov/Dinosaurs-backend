package presentation.web.core;

import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_MODERATOR;
import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_REGULAR;
import static org.assertj.core.api.Assertions.assertThat;

import com.rmr.dinosaurs.DinosaursApplication;
import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.domain.auth.security.model.DinoAuthentication;
import com.rmr.dinosaurs.domain.auth.security.model.DinoPrincipal;
import com.rmr.dinosaurs.domain.auth.security.service.JwtTokenProvider;
import com.rmr.dinosaurs.domain.core.model.Profession;
import com.rmr.dinosaurs.domain.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.domain.core.utils.mapper.ProfessionEntityDtoMapper;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.infrastucture.database.core.ProfessionRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
public class ProfessionControllerIntegrationTest {

  @Container
  private static final CustomPostgresContainer container = CustomPostgresContainer.getInstance();
  // encrypted "pAssw0rd"
  private static final String ENCRYPTED_PASSWORD =
      "$2y$12$SHUzyNYC1vT57bbJLe/ub./N5z/Z2U6ENkWk9c2qkw5fjdKUJ25WO";
  private final String baseUrl = "http://localhost";
  private final String professionApiUrl = "/api/v1/professions";
  private final User regularUser = new User(null, "regular@email.com", ENCRYPTED_PASSWORD,
      ROLE_REGULAR, true, LocalDateTime.now(ZoneOffset.UTC), false, null, null, null);
  private final User moderatorUser = new User(null, "moder@email.com", ENCRYPTED_PASSWORD,
      ROLE_MODERATOR, true, LocalDateTime.now(ZoneOffset.UTC), false, null, null, null);

  private final TestRestTemplate testRestTemplate = new TestRestTemplate();
  private final HttpHeaders requestHeaders = new HttpHeaders();

  @LocalServerPort
  private int port;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private ProfessionRepository professionRepo;
  @Autowired
  private ProfessionEntityDtoMapper professionMapper;

  private String endpointUrl;


  @BeforeEach
  void setUp() {
    endpointUrl = baseUrl + ":" + port + professionApiUrl;
    requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);

    userRepository.saveAll(List.of(regularUser, moderatorUser));

    Profession profession = new Profession(null, "Разработчик",
        "https://proforientator.ru/upload/img/publications/stati/testirovshik1.jpg",
        null, "IT-разработчик — специалист, который занимаемся созданием приложений",
        null, null, null);
    professionRepo.save(profession);
  }

  @AfterEach
  void cleanDb() {
    userRepository.deleteAll();
    professionRepo.deleteAll();
  }

  @Test
  void getAllProfessions_with_regular_user_permission_returns_ListProfessionDto() {
    // given
    var currUser = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currUser);
    requestHeaders.add("X-USER-TOKEN", jwtTokenPairFor.getAccessToken());
    //
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/all");
    ParameterizedTypeReference<List<ProfessionDto>> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {
        };
    //
    var expectedProfessionDtoList = professionRepo.findAll()
        .stream().map(professionMapper::toDto).toList();

    // when
    var responseEntity = testRestTemplate.exchange(
        uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, parameterizedTypeReference
    );

    // then
    var a = responseEntity.getBody();
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    assertThat(a).isNotNull().isNotEmpty();
    assertThat(a).containsExactlyInAnyOrderElementsOf(expectedProfessionDtoList);
  }

  private JwtTokenPair getJwtTokenPairForUser(User user) {
    return jwtTokenProvider.generateJwtTokenPair(getDinoAuthentication(user));
  }

  private DinoAuthentication getDinoAuthentication(User user) {
    return new DinoAuthentication(
        new DinoPrincipal(user.getId(), user.getEmail(), user.getRole())
    );
  }

}
