package com.rmr.dinosaurs.domain.core.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionReadDto {

  Long questionId;

  String question;

  List<AnswerReadDto> answers;

}
