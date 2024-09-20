package fvd.archi.clean.user.usecases.boundaries;

import fvd.archi.clean.user.usecases.models.UserDsResponseModel;
import fvd.archi.clean.user.usecases.models.UserResponseModel;

import java.util.List;

public interface UserPresenter {
  UserResponseModel prepareSuccessView(UserResponseModel user);

  List<UserResponseModel> prepareSuccessView(List<UserDsResponseModel> users);

  UserResponseModel prepareFailView(String error);
}