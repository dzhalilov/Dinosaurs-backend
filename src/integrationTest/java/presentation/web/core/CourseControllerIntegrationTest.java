package presentation.web.core;

import com.rmr.dinosaurs.DinosaursApplication;
import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.security.JwtTokenPair;
import com.rmr.dinosaurs.domain.auth.security.model.DinoAuthentication;
import com.rmr.dinosaurs.domain.auth.security.model.DinoPrincipal;
import com.rmr.dinosaurs.domain.auth.security.service.JwtTokenProvider;
import com.rmr.dinosaurs.domain.core.model.*;
import com.rmr.dinosaurs.domain.core.model.dto.CourseReadDto;
import com.rmr.dinosaurs.domain.core.model.dto.CourseReadPageDto;
import com.rmr.dinosaurs.domain.core.model.dto.ReviewCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.ReviewResponseDto;
import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.infrastucture.database.core.*;
import com.rmr.dinosaurs.infrastucture.database.userinfo.UserInfoRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import testcontainers.CustomPostgresContainer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_REGULAR;
import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DinosaursApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class CourseControllerIntegrationTest {

  @Container
  private static final CustomPostgresContainer container = CustomPostgresContainer.getInstance();

  private static final String BASE_URL = "http://localhost";
  private static final LocalDateTime NOW_TIME = LocalDateTime.now();

  private final User regularUser = new User(null, "regular@email.com", "$2y$12$SHUzyNYC1vT57bbJLe/ub./N5z/Z2U6ENkWk9c2qkw5fjdKUJ25WO",
      ROLE_REGULAR, true, LocalDateTime.now(), false, null, null, null);
  private final UserInfo userInfo = UserInfo.builder()
      .name("Hero")
      .id(null)
      .surname("GOD")
      .user(regularUser)
      .build();

  private final TestRestTemplate testRestTemplate = new TestRestTemplate();

  private final HttpHeaders requestHeaders = new HttpHeaders();
  private final Profession profession = Profession.builder()
      .id(null)
      .coverUrl("https://altcon.ru/uploaded/article/pic_53.jpg")
      .description("Это человек...")
      .name("Аналитик")
      .shortDescription(null)
      .build();
  private final CourseProvider provider = CourseProvider.builder()
      .id(null)
      .coverUrl("https://geekhacker.ru/wp-content/uploads/2020/08/jandeks-praktikum-logo-2.png")
      .description("Освойте востребованную IT-профессию")
      .name("Яндекс Практикум")
      .shortDescription(null)
      .url("https://practicum.yandex.ru/")
      .build();
  private final Course course1 = Course.builder()
      .id(null)
      .coverUrl(
          "https://248006.selcdn.ru/LandGen/desktop_2_103cf52042897badbad23dc0b52adb121c2c92e0.webp")
      .description("Бизнес-аналитик изучает процессы...")
      .endsAt(NOW_TIME.plusDays(100))
      .internalRating(100)
      .isAdvanced(false)
      .isArchived(false)
      .isIndefinite(true)
      .shortDescription(null)
      .startsAt(NOW_TIME.plusDays(10))
      .title("Профессия Бизнес-аналитик")
      .url("https://skillbox.ru/course/profession-business-analyst/")
      .provider(provider)
      .votes(0L)
      .averageRating(5.0)
      .build();
  private final Course course2 = Course.builder()
      .id(null)
      .coverUrl(
          "https://248006.selcdn.ru/LandGen/desktop_2_103cf52042897badbad23dc0b52adb121c2c92e0.webp")
      .description("Бизнес-аналитик изучает процессы и не только")
      .endsAt(NOW_TIME.plusDays(100))
      .internalRating(100)
      .isAdvanced(true)
      .isArchived(false)
      .isIndefinite(true)
      .shortDescription(null)
      .startsAt(NOW_TIME.plusDays(30))
      .title("Профессия Бизнес-аналитик PRO")
      .url("https://skillbox.ru/course/profession-business-analyst/")
      .provider(provider)
      .votes(0L)
      .averageRating(5.0)
      .build();
  private final Course startedCourse = Course.builder()
      .id(null)
      .coverUrl(
          "https://248006.selcdn.ru/LandGen/desktop_2_103cf52042897badbad23dc0b52adb121c2c92e0.webp")
      .description("Бизнес-аналитик изучает процессы...")
      .endsAt(NOW_TIME.plusDays(100))
      .internalRating(100)
      .isAdvanced(false)
      .isArchived(false)
      .isIndefinite(true)
      .shortDescription(null)
      .startsAt(NOW_TIME.minusDays(2))
      .title("Профессия Бизнес-аналитик")
      .url("https://skillbox.ru/course/profession-business-analyst/")
      .provider(provider)
      .votes(0L)
      .averageRating(5.0)
      .build();
  private final Review review1 = Review.builder()
      .id(null)
      .rating(4)
      .textReview("Some review1")
      .course(course1)
      .build();
  private final Review review2 = Review.builder()
      .id(null)
      .rating(4)
      .textReview("Some review2")
      .course(course1)
      .build();

  @LocalServerPort
  private int port;
  @Autowired
  private ProfessionRepository professionRepository;
  @Autowired
  private CourseRepository courseRepository;
  @Autowired
  private CourseProviderRepository courseProviderRepository;
  @Autowired
  private CourseAndProfessionRepository courseAndProfessionRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserInfoRepository userInfoRepository;
  @Autowired
  private ReviewRepository reviewRepository;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;
  private String endpointUrl;

  @BeforeEach
  void setUp() {
    endpointUrl = BASE_URL + ":" + port + "/api/v1/courses";
    requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
    userRepository.saveAndFlush(regularUser);
    userInfoRepository.saveAndFlush(userInfo);
    professionRepository.saveAndFlush(profession);
    courseProviderRepository.saveAndFlush(provider);
    courseRepository.saveAll(List.of(course1, course2, startedCourse));
    CourseAndProfession courseAndProfession1 = new CourseAndProfession(null, course1, profession);
    CourseAndProfession courseAndProfession2 = new CourseAndProfession(null, course2, profession);
    CourseAndProfession courseAndProfession3 = new CourseAndProfession(null, startedCourse,
        profession);
    courseAndProfessionRepository.saveAll(
        List.of(courseAndProfession1, courseAndProfession2, courseAndProfession3));
    reviewRepository.saveAll(List.of(review1, review2));
  }

  @AfterEach
  void cleanDb() {
    reviewRepository.deleteAll();
    courseAndProfessionRepository.deleteAll();
    courseRepository.deleteAll();
    courseProviderRepository.deleteAll();
    professionRepository.deleteAll();
    userRepository.deleteAll();
    userInfoRepository.deleteAll();
  }

  @Test
  void getFilteredCoursesWithDefaultFilter_200() {
    // given
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl);
    ParameterizedTypeReference<CourseReadPageDto> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {
        };
    Pageable pageable = PageRequest.of(1, 9, Sort.by(Sort.Order.asc("startsAt")));

    // when
    var responseEntity = testRestTemplate.exchange(
        uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, parameterizedTypeReference
    );

    // then
    var result = responseEntity.getBody();
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    assert result != null;
    assertThat(result.getTotalElements()).isEqualTo(2L);
  }

  @Test
  void getAllCourses_200() {
    // given
    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/all");
    ParameterizedTypeReference<List<CourseReadDto>> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {
        };

    // when
    var responseEntity = testRestTemplate.exchange(
        uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, parameterizedTypeReference
    );

    // then
    var result = responseEntity.getBody();
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    assert result != null;
    assertThat(result.size()).isEqualTo(3L);
  }

  @Test
  void getAllReviewsByCourseId_200() {
    // given
    var currentUser = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);

    requestHeaders.add("X-USER-TOKEN", jwtTokenPairFor.getAccessToken());

    var requestEntity = new HttpEntity<>(requestHeaders);
    Optional<Review> reviewOptional = reviewRepository.findAll().stream().findFirst();
    assert reviewOptional.isPresent();
    Optional<Course> courseOptional = courseRepository.findById(reviewOptional.get().getCourse().getId());
    assert courseOptional.isPresent();
    Long courseId = courseOptional.get().getId();

    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl + "/" + courseId +"/reviews");
    ParameterizedTypeReference<List<ReviewResponseDto>> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {
        };

    // when
    var responseEntity = testRestTemplate.exchange(
        uriBuilder.toUriString(), HttpMethod.GET,
        requestEntity, parameterizedTypeReference
    );

    // then
    var result = responseEntity.getBody();
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    assert result != null;
    assertThat(result.size()).isEqualTo(2L);
  }

  @Test
  void addCourseReview_201() {
    // given
    Optional<Course> courseForUpdate = courseRepository.findAll().stream().findAny();
    assert courseForUpdate.isPresent();
    Long courseId = courseForUpdate.get().getId();
    ReviewCreateDto reviewDto = new ReviewCreateDto(4, "Some review");

    var currentUser = userRepository.findByEmailIgnoreCase(regularUser.getEmail()).orElseThrow();
    var jwtTokenPairFor = getJwtTokenPairForUser(currentUser);

    requestHeaders.add("X-USER-TOKEN", jwtTokenPairFor.getAccessToken());

    var requestEntity = new HttpEntity<>(reviewDto, requestHeaders);
    var uriBuilder = UriComponentsBuilder
        .fromHttpUrl(endpointUrl + "/" + courseId + "/reviews");
    ParameterizedTypeReference<ReviewResponseDto> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {
        };

    // when
    var responseEntity = testRestTemplate.exchange(
        uriBuilder.toUriString(), HttpMethod.POST,
        requestEntity, parameterizedTypeReference
    );

    // then
    Optional<Course> courseOptional = courseRepository.findById(courseId);
    assert courseOptional.isPresent();
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    assertThat(courseOptional.get().getAverageRating()).isEqualTo(4.0);
  }

  private DinoAuthentication getDinoAuthentication(User user) {
    return new DinoAuthentication(new DinoPrincipal(user.getId(), user.getEmail(), user.getRole()));
  }

  private JwtTokenPair getJwtTokenPairForUser(User user) {
    return jwtTokenProvider.generateJwtTokenPair(getDinoAuthentication(user));
  }

}
