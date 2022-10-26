package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.core.exception.ServiceException;
import com.rmr.dinosaurs.core.model.dto.ProfessionDto;
import com.rmr.dinosaurs.core.model.dto.ProfessionPageDto;
import com.rmr.dinosaurs.core.service.ProfessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/v1/professions")
@RequiredArgsConstructor
public class ProfessionController {

  private final ProfessionService professionService;

  @PostMapping
  @ModeratorPermission
  public ResponseEntity<ProfessionDto> createProfession(@RequestBody ProfessionDto profession) {
    ProfessionDto createdProfession = professionService.addProfession(profession);
    URI createdProfessionUri = URI.create("/api/v1/professions/" + createdProfession.getId());
    return ResponseEntity
        .created(createdProfessionUri)
        .body(createdProfession);
  }

  @Operation(description = "get profession profile data by its id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got profession profile by id",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProfessionDto.class))}),
      @ApiResponse(responseCode = "404", description = "profession profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @GetMapping("/{id}")
  public ResponseEntity<ProfessionDto> getProfessionById(@PathVariable long professionId) {
    ProfessionDto profession = professionService.getProfessionById(professionId);
    return ResponseEntity
        .ok()
        .body(profession);
  }

  @Operation(description = "edit profession profile data using its id and dto")
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
  @PutMapping("/{id}")
  @ModeratorPermission
  public ResponseEntity<ProfessionDto> editProfessionById(
      @PathVariable long professionId, @RequestBody ProfessionDto professionDto) {

    ProfessionDto profession = professionService.editProfessionById(professionId, professionDto);
    return ResponseEntity
        .ok()
        .body(profession);
  }

  @Operation(description = "get all profession profiles")
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

  @GetMapping
  public ResponseEntity<ProfessionPageDto>
  getProfessionPage(@RequestParam(name = "page") int pageNum) {

    ProfessionPageDto professionPage = professionService.getProfessionPage(pageNum);
    return ResponseEntity
        .ok()
        .body(professionPage);
  }

}
