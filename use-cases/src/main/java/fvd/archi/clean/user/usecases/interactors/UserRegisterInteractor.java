package fvd.archi.clean.user.usecases.interactors;

import fvd.archi.clean.user.usecases.boundaries.UserInputBoundary;
import fvd.archi.clean.user.usecases.boundaries.UserPresenter;
import fvd.archi.clean.user.usecases.boundaries.UserRegisterDsGateway;
import fvd.archi.clean.user.entities.User;
import fvd.archi.clean.user.entities.UserFactory;
import fvd.archi.clean.user.usecases.models.UserDsRequestModel;
import fvd.archi.clean.user.usecases.models.UserRequestModel;
import fvd.archi.clean.user.usecases.models.UserResponseModel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class UserRegisterInteractor implements UserInputBoundary {

  private final UserRegisterDsGateway userDsGateway;
  private final UserPresenter userPresenter;
  private final UserFactory userFactory;

  public UserResponseModel create(UserRequestModel requestModel) {
    if (userDsGateway.existsByName(requestModel.getName())) {
      return userPresenter.prepareFailView("User already exists.");
    }
    User user = userFactory.create(requestModel.getName(), requestModel.getPassword());
    if (!user.passwordIsValid()) {
      return userPresenter.prepareFailView("User password must have more than 5 characters.");
    }
    LocalDateTime now = LocalDateTime.now();
    UserDsRequestModel userDsModel = new UserDsRequestModel(user.getName(), user.getPassword(), now);

    userDsGateway.save(userDsModel);

    UserResponseModel accountResponseModel = new UserResponseModel(user.getName(), now.toString());
    return userPresenter.prepareSuccessView(accountResponseModel);
  }

  @Override
  public List<UserResponseModel> findAll() {
    return userPresenter.prepareSuccessView(userDsGateway.findAll());
  }
}