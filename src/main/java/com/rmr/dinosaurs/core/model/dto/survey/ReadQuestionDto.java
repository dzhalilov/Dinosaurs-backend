package com.rmr.dinosaurs.core.model.dto.survey;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadQuestionDto {

  Long questionId;

  String question;

  List<ReadAnswerDto> answers;

}
