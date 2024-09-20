package fvd.archi.clean.user.usecases.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserDsRequestModel {

  String name;
  String password;
  LocalDateTime creationTime;

}
