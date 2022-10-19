package com.rmr.dinosaurs.core.model.dto.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadAnswerDto {

  Long answerId;

  String text;

}
