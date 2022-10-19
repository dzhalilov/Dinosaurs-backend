package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.model.dto.provider.CourseProviderDto;
import com.rmr.dinosaurs.core.model.dto.provider.CourseProviderPageDto;
import com.rmr.dinosaurs.core.service.CourseProviderService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @PutMapping(path = "/{id}")
  public ResponseEntity<CourseProviderDto> updateProviderById(
      @PathVariable long id, @RequestBody CourseProviderDto dto) {

    CourseProviderDto provider = providerService.updateProviderById(id, dto);
    return ResponseEntity
        .ok()
        .body(provider);
  }

  @GetMapping(path = "/all")
  public ResponseEntity<List<CourseProviderDto>> getAllProviders() {
    List<CourseProviderDto> providers = providerService.getAllProviders();
    return ResponseEntity
        .ok()
        .body(providers);
  }

  @GetMapping
  public ResponseEntity<CourseProviderPageDto>
      getProviderPage(@RequestParam(name = "page") int pageNum) {

    CourseProviderPageDto provider = providerService.getProviderPage(pageNum);
    return ResponseEntity
        .ok()
        .body(provider);
  }

}
