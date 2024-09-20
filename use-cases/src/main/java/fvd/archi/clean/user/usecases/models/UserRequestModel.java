package fvd.archi.clean.user.usecases.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestModel {

  String name;
  String password;

}