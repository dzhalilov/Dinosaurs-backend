package com.rmr.dinosaurs.presentation.web.core;

import com.rmr.dinosaurs.domain.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.dto.ProviderDto;
import com.rmr.dinosaurs.domain.core.model.dto.ProviderPageDto;
import com.rmr.dinosaurs.domain.core.service.CourseProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Course provider controller")
@RequiredArgsConstructor
public class CourseProviderController {

  private final CourseProviderService providerService;

  @Operation(summary = "Create course provider profile data using dto")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "got created provider data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProviderDto.class))}),
      @ApiResponse(responseCode = "400", description = "bad request",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400",
          description = "current user has no permissions to create provided provider",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping
  @ModeratorPermission
  public ResponseEntity<ProviderDto> addProvider(@RequestBody ProviderDto provider) {
    ProviderDto createdProvider = providerService.addProvider(provider);
    URI createdProviderUri = URI.create("/api/v1/providers/" + createdProvider.getId());
    return ResponseEntity
        .created(createdProviderUri)
        .body(createdProvider);
  }

  @Operation(summary = "Get course provider profile data by its id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got provider profile by id",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProviderDto.class))}),
      @ApiResponse(responseCode = "404", description = "provider profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @GetMapping("/{providerId}")
  public ResponseEntity<ProviderDto> getProviderById(@PathVariable long providerId) {
    ProviderDto provider = providerService.getProviderById(providerId);
    return ResponseEntity
        .ok()
        .body(provider);
  }

  @Operation(summary = "Edit course provider profile data using its id and dto")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got edited provider data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProviderDto.class))}),
      @ApiResponse(responseCode = "404", description = "provider profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400", description = "bad request",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400",
          description = "current user has no permissions to edit provided provider",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PutMapping("/{providerId}")
  @ModeratorPermission
  public ResponseEntity<ProviderDto> editProviderById(
      @PathVariable long providerId, @RequestBody ProviderDto providerDto) {

    ProviderDto provider = providerService.editProviderById(providerId, providerDto);
    return ResponseEntity
        .ok()
        .body(provider);
  }

  @Operation(summary = "Get all course provider profiles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got list of provider profiles",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProviderDto.class))})})
  @GetMapping("/all")
  public ResponseEntity<List<ProviderDto>> getAllProviders() {
    List<ProviderDto> providers = providerService.getAllProviders();
    return ResponseEntity
        .ok()
        .body(providers);
  }

  @Operation(summary = "Get page of course provider profiles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got page of provider profiles",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProviderPageDto.class))}),
      @ApiResponse(responseCode = "400", description = "not positive page number",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @GetMapping
  public ResponseEntity<ProviderPageDto> getProviderPage(
      @RequestParam(name = "page", required = false, defaultValue = "1") int pageNum) {

    ProviderPageDto provider = providerService.getProviderPage(pageNum);
    return ResponseEntity
        .ok()
        .body(provider);
  }

}
