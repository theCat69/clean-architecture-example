package fvd.archi.clean.user.usecases.boundaries;

import fvd.archi.clean.user.usecases.models.UserRequestModel;
import fvd.archi.clean.user.usecases.models.UserResponseModel;

import java.util.List;

public interface UserInputBoundary {
  UserResponseModel create(UserRequestModel requestModel);
  List<UserResponseModel> findAll();
}