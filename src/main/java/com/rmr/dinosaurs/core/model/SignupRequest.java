package com.rmr.dinosaurs.core.model;

public record SignupRequest(
    String email,
    String password,
    String name,
    String surname) {

}
