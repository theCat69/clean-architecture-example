package fvd.archi.clean.user.presenters;

import fvd.archi.clean.user.usecases.models.UserResponseModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class UserResponseFormatterTest {

  UserResponseFormatter userResponseFormatter = new UserResponseFormatter();

  @Test
  void givenDateAnd3HourTime_whenPrepareSuccessView_thenReturnOnly3HourTime() {
    UserResponseModel modelResponse = new UserResponseModel("baeldung", "2020-12-20T03:00:00.000");
    UserResponseModel formattedResponse = userResponseFormatter.prepareSuccessView(modelResponse);

    assertThat(formattedResponse.getCreationTime()).isEqualTo("03:00:00");
  }
}