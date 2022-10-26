package com.rmr.dinosaurs.core.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderPageDto {

  Long totalElements;

  Integer totalPages;

  Integer pageSize;

  Integer pageNumber;

  List<ProviderDto> content;

}
