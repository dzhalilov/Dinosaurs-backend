package presentation.web.statistics;

import static org.assertj.core.api.Assertions.assertThat;

import com.rmr.dinosaurs.DinosaursApplication;
import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.domain.auth.security.model.DinoAuthentication;
import com.rmr.dinosaurs.domain.auth.security.model.DinoPrincipal;
import com.rmr.dinosaurs.domain.auth.security.service.JwtTokenProvider;
import com.rmr.dinosaurs.domain.core.model.Authority;
import com.rmr.dinosaurs.domain.core.model.Course;
import com.rmr.dinosaurs.domain.core.model.CourseAndProfession;
import com.rmr.dinosaurs.domain.core.model.CourseProvider;
import com.rmr.dinosaurs.domain.core.model.Profession;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionDto;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.infrastucture.database.core.CourseAndProfessionRepository;
import com.rmr.dinosaurs.infrastucture.database.core.CourseProviderRepository;
import com.rmr.dinosaurs.infrastucture.database.core.CourseRepository;
import com.rmr.dinosaurs.infrastucture.database.core.ProfessionRepository;
import java.time.LocalDateTime;
import java.util.List;
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
public class StatisticsControllerIntegrationTest {

  @Container
  private static final CustomPostgresContainer container = CustomPostgresContainer.getInstance();

  private static final String BASE_URL = "http://localhost";

  private final TestRestTemplate testRestTemplate = new TestRestTemplate();
  private final HttpHeaders requestHeaders = new HttpHeaders();
  // Using encrypted 'pAssw0rd' as password value
  private final User regularUser = new User(123L, "regularuser@email.com",
      "$2y$12$SHUzyNYC1vT57bbJLe/ub./N5z/Z2U6ENkWk9c2qkw5fjdKUJ25WO", Authority.ROLE_REGULAR, true,
      LocalDateTime.now(), false, null, null, null);
  private final User moderatorUser = new User(125L, "moderatoruser@email.com",
      "$2y$12$SHUzyNYC1vT57bbJLe/ub./N5z/Z2U6ENkWk9c2qkw5fjdKUJ25WO", Authority.ROLE_MODERATOR,
      true, LocalDateTime.now(), false, null, null, null);
  private final Profession profession = Profession.builder().id(null)
      .coverUrl("https://altcon.ru/uploaded/article/pic_53.jpg").description("Это человек...")
      .name("Аналитик").shortDescription(null).build();
  private final CourseProvider provider = CourseProvider.builder().id(null)
      .coverUrl("https://geekhacker.ru/wp-content/uploads/2020/08/jandeks-praktikum-logo-2.png")
      .description("Освойте востребованную IT-профессию").name("Яндекс Практикум")
      .shortDescription(null).url("https://practicum.yandex.ru/").build();
  private final Course course = Course.builder().id(null).coverUrl(
          "https://248006.selcdn.ru/LandGen/desktop_2_103cf52042897badbad23dc0b52adb121c2c92e0.webp")
      .description("Бизнес-аналитик изучает процессы...").endsAt(LocalDateTime.now().plusDays(100))
      .internalRating(100).isAdvanced(false).isArchived(false).isIndefinite(true)
      .shortDescription(null).startsAt(LocalDateTime.now().plusDays(10))
      .title("Профессия Бизнес-аналитик")
      .url("https://skillbox.ru/course/profession-business-analyst/").provider(provider).votes(0L)
      .averageRating(5.0).build();
  @Autowired
  CourseProviderRepository courseProviderRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CourseRepository courseRepository;
  @Autowired
  private ProfessionRepository professionRepository;
  @Autowired
  private CourseAndProfessionRepository courseAndProfessionRepository;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @LocalServerPort
  private int port;

  private String endpointUrl;


  @BeforeEach
  void setUp() {
    endpointUrl = BASE_URL + ":" + port + "/api/v1/statistics";
    requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
    userRepository.saveAll(List.of(regularUser, moderatorUser));
    professionRepository.saveAndFlush(profession);
    courseProviderRepository.saveAndFlush(provider);
    courseRepository.save(course);
    CourseAndProfession courseAndProfession1 = new CourseAndProfession(null, course, profession);
    courseAndProfessionRepository.save(courseAndProfession1);
  }

  @AfterEach
  void cleanDb() {
    userRepository.deleteAll();
    courseAndProfessionRepository.deleteAll();
    courseRepository.deleteAll();
    courseProviderRepository.deleteAll();
    professionRepository.deleteAll();
  }


  @Test
  @DisplayName("course link transition")
  void whenCreateCourseLinkTransitionThenStatisticsSaved() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);
    requestHeaders.add("X-USER-TOKEN", jwtTokenPairFor.getAccessToken());
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/course/" + course.getId());

    // when
    var responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST,
        requestEntity, CourseLinkTransitionDto.class);

    // then
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
  }


  private DinoAuthentication getDinoAuthentication(User user) {
    return new DinoAuthentication(new DinoPrincipal(user.getId(), user.getEmail(), user.getRole()));
  }

  private JwtTokenPair getJwtTokenPairForUser(User user) {
    return jwtTokenProvider.generateJwtTokenPair(getDinoAuthentication(user));
  }

}
