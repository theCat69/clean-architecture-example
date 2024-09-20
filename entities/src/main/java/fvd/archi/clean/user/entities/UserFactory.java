package fvd.archi.clean.user.entities;

public interface UserFactory {
  User create(String name, String password);
}
