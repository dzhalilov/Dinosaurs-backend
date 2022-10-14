package configuration;

import configuration.containers.PostgresTestContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment {

  protected ContainersEnvironment() {}

  @Container
  static PostgresTestContainer postgresTestContainer = PostgresTestContainer.getInstance();

}
