package fvd.archi.clean.user.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommonUser implements User {
  String name;
  String password;

  @Override
  public boolean passwordIsValid() {
    return password != null && password.length() > 5;
  }
}
