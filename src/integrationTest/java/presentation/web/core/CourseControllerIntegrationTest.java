package presentation.web.core;

import com.rmr.dinosaurs.domain.core.model.Course;
import com.rmr.dinosaurs.domain.core.model.CourseAndProfession;
import com.rmr.dinosaurs.domain.core.model.CourseProvider;
import com.rmr.dinosaurs.domain.core.model.Profession;
import com.rmr.dinosaurs.domain.core.model.dto.CourseReadPageDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import presentation.web.AbstractControllerIntegrationTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CourseControllerIntegrationTest extends AbstractControllerIntegrationTest {

  @BeforeEach
  void setUp() {
    endpointUrl = baseUrl + ":" + port + COURSES_API_URL;
    requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
    Profession profession = Profession.builder()
        .id(null)
        .coverUrl("https://altcon.ru/uploaded/article/pic_53.jpg")
        .description("Это человек...")
        .name("Аналитик")
        .shortDescription(null)
        .build();
    professionRepo.saveAndFlush(profession);

    CourseProvider provider = CourseProvider.builder()
        .id(null)
        .coverUrl("https://geekhacker.ru/wp-content/uploads/2020/08/jandeks-praktikum-logo-2.png")
        .description("Освойте востребованную IT-профессию")
        .name("Яндекс Практикум")
        .shortDescription(null)
        .url("https://practicum.yandex.ru/")
        .build();
    courseProviderRepository.saveAndFlush(provider);

    Course course1 = Course.builder()
        .id(null)
        .coverUrl("https://248006.selcdn.ru/LandGen/desktop_2_103cf52042897badbad23dc0b52adb121c2c92e0.webp")
        .description("Бизнес-аналитик изучает процессы...")
        .endsAt(LocalDateTime.of(2023, 5, 30, 0, 0))
        .internalRating(100)
        .isAdvanced(false)
        .isArchived(false)
        .isIndefinite(true)
        .shortDescription(null)
        .startsAt(LocalDateTime.of(2022, 11, 30, 0, 0))
        .title("Профессия Бизнес-аналитик")
        .url("https://skillbox.ru/course/profession-business-analyst/")
        .provider(provider)
        .build();

    Course course2 = Course.builder()
        .id(null)
        .coverUrl("https://248006.selcdn.ru/LandGen/desktop_2_103cf52042897badbad23dc0b52adb121c2c92e0.webp")
        .description("Бизнес-аналитик изучает процессы и не только")
        .endsAt(LocalDateTime.of(2023, 5, 30, 0, 0))
        .internalRating(100)
        .isAdvanced(true)
        .isArchived(false)
        .isIndefinite(true)
        .shortDescription(null)
        .startsAt(LocalDateTime.of(2022, 11, 30, 0, 0))
        .title("Профессия Бизнес-аналитик PRO")
        .url("https://skillbox.ru/course/profession-business-analyst/")
        .provider(provider)
        .build();

    Course startedCourse = Course.builder()
        .id(null)
        .coverUrl("https://248006.selcdn.ru/LandGen/desktop_2_103cf52042897badbad23dc0b52adb121c2c92e0.webp")
        .description("Бизнес-аналитик изучает процессы...")
        .endsAt(LocalDateTime.of(2023, 5, 30, 0, 0))
        .internalRating(100)
        .isAdvanced(false)
        .isArchived(false)
        .isIndefinite(true)
        .shortDescription(null)
        .startsAt(LocalDateTime.of(2022, 11, 1, 0, 0))
        .title("Профессия Бизнес-аналитик")
        .url("https://skillbox.ru/course/profession-business-analyst/")
        .provider(provider)
        .build();
    courseRepository.saveAll(List.of(course1, course2, startedCourse));

    CourseAndProfession courseAndProfession1 = new CourseAndProfession(null, course1, profession);
    CourseAndProfession courseAndProfession2 = new CourseAndProfession(null, course2, profession);
    CourseAndProfession courseAndProfession3 = new CourseAndProfession(null, startedCourse, profession);

    courseAndProfessionRepository.saveAll(
        List.of(courseAndProfession1, courseAndProfession2, courseAndProfession3));
  }

  @AfterEach
  void cleanDb() {
    courseAndProfessionRepository.deleteAll();
    courseRepository.deleteAll();
    courseProviderRepository.deleteAll();
    professionRepo.deleteAll();
  }

  @Test
  void getFilteredCoursesWithDefaultFilter_200() throws Exception {

    var requestEntity = new HttpEntity<>(requestHeaders);
    var uriBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl);
    ParameterizedTypeReference<CourseReadPageDto> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {
        };
    //
    Pageable pageable = PageRequest.of(1, 9, Sort.by(Sort.Order.asc("startsAt")));
    var expectedCourseReadPageDto = courseService.toReadCoursePageDto(courseRepository
        .findByFilter("", null, null, null, null,
            LocalDateTime.of(2022, 11, 4, 0, 0), pageable));

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

}
