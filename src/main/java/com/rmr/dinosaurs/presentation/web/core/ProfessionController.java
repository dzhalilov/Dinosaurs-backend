package com.rmr.dinosaurs.presentation.web.core;

import com.rmr.dinosaurs.domain.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.domain.core.model.dto.ProfessionPageDto;
import com.rmr.dinosaurs.domain.core.service.ProfessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
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
@RequestMapping("/api/v1/professions")
@Tag(name = "Profession controller")
@RequiredArgsConstructor
public class ProfessionController {

  private final ProfessionService professionService;

  @Operation(summary = "Create profession profile data using dto")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "got created profession data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProfessionDto.class))}),
      @ApiResponse(responseCode = "400", description = "bad request",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400",
          description = "current user has no permissions to create provided profession",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping
  @ModeratorPermission
  public ResponseEntity<ProfessionDto> addProfession(@RequestBody @Valid ProfessionDto profession) {
    ProfessionDto createdProfession = professionService.addProfession(profession);
    URI createdProfessionUri = URI.create("/api/v1/professions/" + createdProfession.getId());
    return ResponseEntity
        .created(createdProfessionUri)
        .body(createdProfession);
  }

  @Operation(summary = "Get profession profile data by its id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got profession profile by id",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProfessionDto.class))}),
      @ApiResponse(responseCode = "404", description = "profession profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @GetMapping("/{professionId}")
  public ResponseEntity<ProfessionDto> getProfessionById(@PathVariable long professionId) {
    ProfessionDto profession = professionService.getProfessionById(professionId);
    return ResponseEntity
        .ok()
        .body(profession);
  }

  @Operation(summary = "Edit profession profile data using its id and dto")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got edited profession data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProfessionDto.class))}),
      @ApiResponse(responseCode = "404", description = "profession profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400", description = "bad request",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400",
          description = "current user has no permissions to edit provided profession",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PutMapping("/{professionId}")
  @ModeratorPermission
  public ResponseEntity<ProfessionDto> editProfessionById(
      @PathVariable long professionId, @RequestBody @Valid ProfessionDto professionDto) {

    ProfessionDto profession = professionService.editProfessionById(professionId, professionDto);
    return ResponseEntity
        .ok()
        .body(profession);
  }

  @Operation(summary = "Get all profession profiles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got list of profession profiles",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProfessionDto.class))})})
  @GetMapping("/all")
  public ResponseEntity<List<ProfessionDto>> getAllProfessions() {
    List<ProfessionDto> professions = professionService.getAllProfessions();
    return ResponseEntity
        .ok()
        .body(professions);
  }

  @Operation(summary = "Get page of profession profiles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got page of profession profiles",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProfessionPageDto.class))}),
      @ApiResponse(responseCode = "400", description = "not positive page number",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @GetMapping
  public ResponseEntity<ProfessionPageDto> getProfessionPage(
      @RequestParam(name = "page", required = false, defaultValue = "1") int pageNum) {

    ProfessionPageDto professionPage = professionService.getProfessionPage(pageNum);
    return ResponseEntity
        .ok()
        .body(professionPage);
  }

}
