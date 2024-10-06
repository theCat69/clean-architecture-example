package fvd.archi.clean.user.usecases.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserDsRequestModel {

  public UserDsRequestModel(String name, String password) {
    this.name = name;
    this.password = password;
    this.creationTime = LocalDateTime.now();
  }

  String name;
  String password;
  LocalDateTime creationTime;

}
