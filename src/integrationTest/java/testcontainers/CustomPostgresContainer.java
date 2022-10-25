package testcontainers;

import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPostgresContainer extends PostgreSQLContainer<CustomPostgresContainer> {

  private static final String IMAGE_VERSION = "postgres:14.5-alpine";
  private static CustomPostgresContainer container;

  private CustomPostgresContainer() {
    super(IMAGE_VERSION);
  }

  public static synchronized CustomPostgresContainer getInstance() {
    if (container == null) {
      container = new CustomPostgresContainer();
    }
    return container;
  }

  @Override
  public void start() {
    super.start();
    System.setProperty("SPRING_DATASOURCE_URL", container.getJdbcUrl());
    System.setProperty("SPRING_DATASOURCE_USERNAME", container.getUsername());
    System.setProperty("SPRING_DATASOURCE_PASSWORD", container.getPassword());
  }

  @Override
  public void stop() {
    // skip impl in favor of JVM handling
  }

}
