package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.core.model.dto.ProviderDto;
import com.rmr.dinosaurs.core.model.dto.ProviderPageDto;
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
  @ModeratorPermission
  public ResponseEntity<ProviderDto> createProvider(@RequestBody ProviderDto provider) {
    ProviderDto createdProvider = providerService.createProvider(provider);
    URI createdProviderUri = URI.create("/api/v1/providers/" + createdProvider.getId());
    return ResponseEntity
        .created(createdProviderUri)
        .body(createdProvider);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProviderDto> getProviderById(@PathVariable long id) {
    ProviderDto provider = providerService.getProviderById(id);
    return ResponseEntity
        .ok()
        .body(provider);
  }

  @PutMapping("/{id}")
  @ModeratorPermission
  public ResponseEntity<ProviderDto> updateProviderById(
      @PathVariable long id, @RequestBody ProviderDto dto) {

    ProviderDto provider = providerService.updateProviderById(id, dto);
    return ResponseEntity
        .ok()
        .body(provider);
  }

  @GetMapping("/all")
  public ResponseEntity<List<ProviderDto>> getAllProviders() {
    List<ProviderDto> providers = providerService.getAllProviders();
    return ResponseEntity
        .ok()
        .body(providers);
  }

  @GetMapping
  public ResponseEntity<ProviderPageDto>
  getProviderPage(@RequestParam(name = "page") int pageNum) {

    ProviderPageDto provider = providerService.getProviderPage(pageNum);
    return ResponseEntity
        .ok()
        .body(provider);
  }

}
