package com.rmr.dinosaurs.domain.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rmr.dinosaurs.domain.core.model.Authority;
import io.micrometer.core.lang.Nullable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  @NotNull
  Long id;

  @NotNull
  String email;

  @NotNull
  Authority role;

  Boolean isConfirmed;

  LocalDateTime registeredAt;

  Boolean isArchived;

  @Nullable
  @JsonIgnore
  LocalDateTime archivedAt;

}
