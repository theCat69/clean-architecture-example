package fvd.archi.clean.user.usecases;

import fvd.archi.clean.user.usecases.boundaries.UserPresenter;
import fvd.archi.clean.user.usecases.boundaries.UserRegisterDsGateway;
import fvd.archi.clean.user.entities.CommonUser;
import fvd.archi.clean.user.entities.User;
import fvd.archi.clean.user.entities.UserFactory;
import fvd.archi.clean.user.usecases.models.UserDsRequestModel;
import fvd.archi.clean.user.usecases.models.UserRequestModel;
import fvd.archi.clean.user.usecases.models.UserResponseModel;
import fvd.archi.clean.user.usecases.interactors.UserRegisterInteractor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegisterInteractorTest {

  @Mock
  UserFactory userFactory;
  @Mock
  UserRegisterDsGateway userDsGateway;
  @Mock
  UserPresenter userPresenter;
  @InjectMocks
  UserRegisterInteractor interactor;

  @Test
  void givenBaeldungUserAnd123456Password_whenCreate_thenSaveItAndPrepareSuccessView() {

    User user = new CommonUser("baeldung", "123456");
    UserRequestModel userRequestModel = new UserRequestModel(user.getName(), user.getPassword());
    when(userFactory.create(anyString(), anyString()))
      .thenReturn(new CommonUser(user.getName(), user.getPassword()));

    interactor.create(userRequestModel);

    verify(userDsGateway, times(1)).save(any(UserDsRequestModel.class));
    verify(userPresenter, times(1)).prepareSuccessView(any(UserResponseModel.class));
  }
}