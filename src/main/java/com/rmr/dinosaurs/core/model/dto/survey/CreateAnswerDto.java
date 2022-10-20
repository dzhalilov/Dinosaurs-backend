package com.rmr.dinosaurs.core.model.dto.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnswerDto {

  Long answerId;

  String text;

  Long professionId;

}
