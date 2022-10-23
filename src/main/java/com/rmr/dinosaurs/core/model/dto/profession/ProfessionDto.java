package com.rmr.dinosaurs.core.model.dto.profession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionDto {

  Long id;

  String name;

  String coverUrl;

  String description;

}
