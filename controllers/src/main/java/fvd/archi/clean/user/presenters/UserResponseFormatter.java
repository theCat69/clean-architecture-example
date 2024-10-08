package fvd.archi.clean.user.presenters;

import fvd.archi.clean.user.usecases.boundaries.UserPresenter;
import fvd.archi.clean.user.usecases.models.UserDsResponseModel;
import fvd.archi.clean.user.usecases.models.UserResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserResponseFormatter implements UserPresenter {

  @Override
  public UserResponseModel prepareSuccessView(UserResponseModel response) {
    LocalDateTime responseTime = LocalDateTime.parse(response.getCreationTime());
    response.setCreationTime(formatDate(responseTime));
    return response;
  }

  @Override
  public UserResponseModel prepareSuccessView(UserDsResponseModel dsResponse) {
    return new UserResponseModel(dsResponse.getLogin(), formatDate(dsResponse.getCreationTime()));
  }

  @Override
  public List<UserResponseModel> prepareSuccessView(List<UserDsResponseModel> users) {
    return users.stream().map(userDsResponseModel ->
        new UserResponseModel(userDsResponseModel.getLogin(),
          formatDate(userDsResponseModel.getCreationTime())))
      .toList();
  }

  @Override
  public UserResponseModel prepareFailView(String error) {
    throw new ResponseStatusException(HttpStatus.CONFLICT, error);
  }

  private String formatDate(LocalDateTime date){
    return date.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
  }
}
