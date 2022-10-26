package com.rmr.dinosaurs.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateDto {

  Long answerId;

  String text;

  Long professionId;

}
