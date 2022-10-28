package com.rmr.dinosaurs.domain.notification.email.model;

import com.rmr.dinosaurs.domain.auth.model.User;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {

  @NotNull
  private MailType mailType;

  @NotNull
  @NotEmpty
  private List<User> recipients;

}
