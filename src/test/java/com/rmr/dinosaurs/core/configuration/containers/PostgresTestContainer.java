package com.rmr.dinosaurs.configuration.containers;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

  public static final String IMAGE_VERSION = "postgres:14.5-alpine";
  public static final String DB_NAME = "dinodb-test";
  public static PostgresTestContainer container;

  public PostgresTestContainer() {
    super(IMAGE_VERSION);
  }

  public static PostgresTestContainer getInstance() {
    if (container == null) {
      synchronized (PostgresTestContainer.class) {
        if (container == null) {
          container = new PostgresTestContainer().withDatabaseName(DB_NAME);
        }
      }
    }
    return container;
  }

  @Override
  public void start() {
    super.start();
    // data from application.yml
    System.setProperty("jdbc:postgresql://localhost:5432/dinodb", container.getJdbcUrl());
    System.setProperty("postgres", container.getUsername());
    System.setProperty("password", container.getPassword());
  }

  @Override
  public void stop() {
  }

}
