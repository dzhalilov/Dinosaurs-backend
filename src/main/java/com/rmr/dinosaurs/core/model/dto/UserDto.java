package com.rmr.dinosaurs.core.model.dto;

import com.rmr.dinosaurs.core.model.Authority;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  UUID id;

  String email;

  Authority role;

  String firstName;

}
