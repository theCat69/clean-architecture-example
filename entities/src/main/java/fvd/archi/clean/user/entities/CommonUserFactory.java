package fvd.archi.clean.user.entities;

import fvd.archi.clean.technical.UseCasesComponents;

@UseCasesComponents
public class CommonUserFactory implements UserFactory {
  public User create(String name, String password) {
    return new CommonUser(name, password);
  }
}
