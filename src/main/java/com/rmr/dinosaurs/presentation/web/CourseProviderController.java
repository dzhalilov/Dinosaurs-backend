package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.model.dto.CourseProviderDto;
import com.rmr.dinosaurs.core.service.CourseProviderService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class CourseProviderController {

  private final CourseProviderService providerService;

  @PostMapping
  public ResponseEntity<CourseProviderDto> createProvider(@RequestBody CourseProviderDto provider) {
    CourseProviderDto createdProvider = providerService.createProvider(provider);
    URI createdProviderUri = URI.create("/api/v1/providers/" + createdProvider.getId());
    return ResponseEntity
        .created(createdProviderUri)
        .body(createdProvider);
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<CourseProviderDto> getProviderById(@PathVariable long id) {
    CourseProviderDto provider = providerService.getProviderById(id);
    return ResponseEntity
        .ok()
        .body(provider);
  }

}
