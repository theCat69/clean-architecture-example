package fvd.archi.clean.user.entities;

import fvd.archi.clean.technical.UseCasesBean;

@UseCasesBean
public class CommonUserFactory implements UserFactory {
  public User create(String name, String password) {
    return new CommonUser(name, password);
  }
}
