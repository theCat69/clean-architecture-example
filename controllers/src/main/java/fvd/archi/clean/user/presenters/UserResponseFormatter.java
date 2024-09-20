package fvd.archi.clean.user.presenters;

import fvd.archi.clean.user.usecases.boundaries.UserPresenter;
import fvd.archi.clean.user.usecases.models.UserDsResponseModel;
import fvd.archi.clean.user.usecases.models.UserResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserResponseFormatter implements UserPresenter {

  @Override
  public UserResponseModel prepareSuccessView(UserResponseModel response) {
    LocalDateTime responseTime = LocalDateTime.parse(response.getCreationTime());
    response.setCreationTime(responseTime.format(DateTimeFormatter.ofPattern("hh:mm:ss")));
    return response;
  }

  @Override
  public List<UserResponseModel> prepareSuccessView(List<UserDsResponseModel> users) {
    return users.stream().map(userDsResponseModel ->
        new UserResponseModel(userDsResponseModel.getLogin(),
          userDsResponseModel.getCreationTime().format(DateTimeFormatter.ofPattern("hh:mm:ss"))))
      .toList();
  }

  @Override
  public UserResponseModel prepareFailView(String error) {
    throw new ResponseStatusException(HttpStatus.CONFLICT, error);
  }
}