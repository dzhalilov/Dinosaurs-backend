package presentation.web.core;

import com.rmr.dinosaurs.domain.core.model.dto.CourseReadPageDto;
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

import static org.assertj.core.api.Assertions.assertThat;

public class CourseControllerIntegrationTest extends AbstractControllerIntegrationTest {

  @BeforeEach
  void setUp() {
    endpointUrl = baseUrl + ":" + port + COURSES_API_URL;
    requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
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
    assertThat(result.getTotalElements()).isEqualTo(8L);
  }

}
