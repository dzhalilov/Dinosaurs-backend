package com.rmr.dinosaurs.core.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateDto {

  Long questionId;

  String question;

  List<AnswerCreateDto> answers;

}
