package com.rmr.dinosaurs.core.model.dto;

import com.rmr.dinosaurs.core.model.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  Long id;

  String email;

  Authority role;

  String firstName;

}
