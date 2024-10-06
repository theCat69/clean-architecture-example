package fvd.archi.clean.user.usecases.boundaries;

import fvd.archi.clean.user.usecases.models.UserDsRequestModel;
import fvd.archi.clean.user.usecases.models.UserDsResponseModel;

import java.util.List;

public interface UserRegisterDsGateway {

  boolean existsByName(String name);

  void save(UserDsRequestModel requestModel);

  List<UserDsResponseModel> findAll();

  UserDsResponseModel findById(String id);

  UserDsResponseModel delete(String id);

  UserDsResponseModel update(UserDsRequestModel requestModel);
}