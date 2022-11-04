package presentation.web;

import com.rmr.dinosaurs.DinosaursApplication;
import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.security.service.JwtTokenProvider;
import com.rmr.dinosaurs.domain.core.service.impl.CourseServiceImpl;
import com.rmr.dinosaurs.domain.core.utils.mapper.ProfessionEntityDtoMapper;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.infrastucture.database.core.CourseAndProfessionRepository;
import com.rmr.dinosaurs.infrastucture.database.core.CourseProviderRepository;
import com.rmr.dinosaurs.infrastucture.database.core.CourseRepository;
import com.rmr.dinosaurs.infrastucture.database.core.ProfessionRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import testcontainers.CustomPostgresContainer;

import java.time.LocalDateTime;

import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_MODERATOR;
import static com.rmr.dinosaurs.domain.core.model.Authority.ROLE_REGULAR;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DinosaursApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractControllerIntegrationTest {

  @Container
  protected static final CustomPostgresContainer container = CustomPostgresContainer.getInstance();

  protected final String baseUrl = "http://localhost";
  protected final String professionApiUrl = "/api/v1/professions";
  protected final String COURSES_API_URL = "/api/v1/courses";
  // encrypted "pAssw0rd"
  protected static final String ENCRYPTED_PASSWORD =
      "$2y$12$SHUzyNYC1vT57bbJLe/ub./N5z/Z2U6ENkWk9c2qkw5fjdKUJ25WO";
  protected final User regularUser = new User(null, "regular@email.com", ENCRYPTED_PASSWORD,
      ROLE_REGULAR, true, LocalDateTime.now(), false, null, null, null);
  protected final User moderatorUser = new User(null, "moder@email.com", ENCRYPTED_PASSWORD,
      ROLE_MODERATOR, true, LocalDateTime.now(), false, null, null, null);

  protected final TestRestTemplate testRestTemplate = new TestRestTemplate();
  protected final HttpHeaders requestHeaders = new HttpHeaders();

  @LocalServerPort
  protected int port;

  @Autowired
  protected UserRepository userRepository;
  @Autowired
  protected JwtTokenProvider jwtTokenProvider;

  @Autowired
  protected ProfessionRepository professionRepo;
  @Autowired
  protected CourseRepository courseRepository;
  @Autowired
  protected CourseProviderRepository courseProviderRepository;
  @Autowired
  protected CourseAndProfessionRepository courseAndProfessionRepository;
  @Autowired
  protected ProfessionEntityDtoMapper professionMapper;
  @Autowired
  protected CourseServiceImpl courseService;

  protected String endpointUrl;

}
