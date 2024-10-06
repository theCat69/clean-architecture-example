package fvd.archi.clean.user.usecases;

import fvd.archi.clean.user.usecases.boundaries.UserPresenter;
import fvd.archi.clean.user.usecases.boundaries.UserRegisterDsGateway;
import fvd.archi.clean.user.entities.CommonUser;
import fvd.archi.clean.user.entities.User;
import fvd.archi.clean.user.entities.UserFactory;
import fvd.archi.clean.user.usecases.models.UserDsRequestModel;
import fvd.archi.clean.user.usecases.models.UserDsResponseModel;
import fvd.archi.clean.user.usecases.models.UserRequestModel;
import fvd.archi.clean.user.usecases.models.UserResponseModel;
import fvd.archi.clean.user.usecases.interactors.UserRegisterInteractor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    when(userDsGateway.existsByName(anyString())).thenReturn(false);
    when(userFactory.create(anyString(), anyString()))
      .thenReturn(new CommonUser(user.getName(), user.getPassword()));

    interactor.create(userRequestModel);

    verify(userDsGateway, times(1)).save(any(UserDsRequestModel.class));
    verify(userPresenter, times(1)).prepareSuccessView(any(UserResponseModel.class));
  }

  @Test
  void create_ifUserAlreadyExist_shouldPrepareFailView() {
    User user = new CommonUser("login", "password");
    UserRequestModel userRequestModel = new UserRequestModel(user.getName(), user.getPassword());
    when(userDsGateway.existsByName(anyString())).thenReturn(true);

    interactor.create(userRequestModel);

    verify(userDsGateway, never()).save(any(UserDsRequestModel.class));
    verify(userPresenter, never()).prepareSuccessView(any(UserResponseModel.class));
    verify(userPresenter, times(1)).prepareFailView(anyString());
  }

  @Test
  void create_ifPasswordInvalid_shouldPrepareFailView() {
    User user = new CommonUser("login", "");
    UserRequestModel userRequestModel = new UserRequestModel(user.getName(), user.getPassword());
    when(userDsGateway.existsByName(anyString())).thenReturn(false);
    when(userFactory.create(anyString(), anyString()))
      .thenReturn(new CommonUser(user.getName(), user.getPassword()));

    interactor.create(userRequestModel);

    verify(userDsGateway, never()).save(any(UserDsRequestModel.class));
    verify(userPresenter, never()).prepareSuccessView(any(UserResponseModel.class));
    verify(userPresenter, times(1)).prepareFailView(anyString());
  }

  @Test
  void findAll_shouldReturnAllUsers() {
    var findAllList = List.of(
      new UserDsResponseModel("login1", LocalDateTime.now()),
      new UserDsResponseModel("login2", LocalDateTime.now())
    );
    var presenterList = List.of(
      new UserResponseModel("login1", "12:00:00"),
      new UserResponseModel("login2", "13:10:11")
    );
    when(userDsGateway.findAll()).thenReturn(findAllList);
    when(userPresenter.prepareSuccessView(anyList())).thenReturn(presenterList);

    var result = interactor.findAll();

    assertThat(result).hasSize(2)
      .usingRecursiveFieldByFieldElementComparator()
      .containsExactlyElementsOf(presenterList);

  }

  @Test
  void findById_userExist_shouldCallSuccessView() {
    when(userDsGateway.existsByName(anyString())).thenReturn(true);
    when(userDsGateway.findById(anyString()))
      .thenReturn(new UserDsResponseModel("login", LocalDateTime.now()));

    interactor.findById("any");

    verify(userDsGateway, times(1)).findById(anyString());
    verify(userPresenter, times(1)).prepareSuccessView(any(UserDsResponseModel.class));
  }

  @Test
  void findById_userDoesNotExist_shouldCallFailView() {
    when(userDsGateway.existsByName(anyString())).thenReturn(false);


    interactor.findById("any");

    verify(userDsGateway, never()).findById(anyString());
    verify(userPresenter, never()).prepareSuccessView(any(UserResponseModel.class));
    verify(userPresenter, times(1)).prepareFailView(anyString());
  }

  @Test
  void delete_userExist_shouldCallSuccessView() {
    when(userDsGateway.existsByName(anyString())).thenReturn(true);
    when(userDsGateway.delete(anyString()))
      .thenReturn(new UserDsResponseModel("login", LocalDateTime.now()));

    interactor.delete("any");

    verify(userDsGateway, times(1)).delete(anyString());
    verify(userPresenter, times(1)).prepareSuccessView(any(UserDsResponseModel.class));
  }

  @Test
  void delete_userDoesNotExist_shouldCallFailView() {
    when(userDsGateway.existsByName(anyString())).thenReturn(false);

    interactor.delete("any");

    verify(userDsGateway, never()).delete(anyString());
    verify(userPresenter, never()).prepareSuccessView(any(UserResponseModel.class));
    verify(userPresenter, times(1)).prepareFailView(anyString());
  }

  @Test
  void update_UserExist_shouldUpdateAndPrepareSuccessView() {

    User user = new CommonUser("baeldung", "123456");
    UserRequestModel userRequestModel = new UserRequestModel(user.getName(), user.getPassword());
    when(userDsGateway.existsByName(anyString())).thenReturn(true);
    when(userFactory.create(anyString(), anyString()))
      .thenReturn(new CommonUser(user.getName(), user.getPassword()));
    when(userDsGateway.update(any(UserDsRequestModel.class)))
      .thenReturn(new UserDsResponseModel(user.getName(), LocalDateTime.now()));

    interactor.update(userRequestModel);

    verify(userDsGateway, times(1)).update(any(UserDsRequestModel.class));
    verify(userPresenter, times(1)).prepareSuccessView(any(UserDsResponseModel.class));
  }

  @Test
  void update_ifUserDoesNotExist_shouldPrepareFailView() {
    User user = new CommonUser("login", "password");
    UserRequestModel userRequestModel = new UserRequestModel(user.getName(), user.getPassword());
    when(userDsGateway.existsByName(anyString())).thenReturn(false);

    interactor.update(userRequestModel);

    verify(userDsGateway, never()).update(any(UserDsRequestModel.class));
    verify(userPresenter, never()).prepareSuccessView(any(UserDsResponseModel.class));
    verify(userPresenter, times(1)).prepareFailView(anyString());
  }

  @Test
  void update_ifPasswordInvalid_shouldPrepareFailView() {
    User user = new CommonUser("login", "");
    UserRequestModel userRequestModel = new UserRequestModel(user.getName(), user.getPassword());
    when(userDsGateway.existsByName(anyString())).thenReturn(true);
    when(userFactory.create(anyString(), anyString()))
      .thenReturn(new CommonUser(user.getName(), user.getPassword()));

    interactor.update(userRequestModel);

    verify(userDsGateway, never()).update(any(UserDsRequestModel.class));
    verify(userPresenter, never()).prepareSuccessView(any(UserDsResponseModel.class));
    verify(userPresenter, times(1)).prepareFailView(anyString());
  }

}