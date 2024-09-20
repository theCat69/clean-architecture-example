package fvd.archi.clean.user.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommonUserTest {
  @Test
  void given123Password_whenPasswordIsNotValid_thenIsFalse() {
    User user = new CommonUser("Baeldung", "123");

    assertThat(user.passwordIsValid()).isFalse();
  }

}