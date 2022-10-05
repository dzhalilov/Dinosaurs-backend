package com.rmr.dinosaurs.configuration;

import com.rmr.dinosaurs.configuration.containers.PostgresTestContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment {

  @Container
  public static PostgresTestContainer postgresTestContainer = PostgresTestContainer.getInstance();

}
