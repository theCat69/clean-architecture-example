package fvd.archi.clean.user.entities;

public interface User {
  boolean passwordIsValid();

  String getName();

  String getPassword();
}