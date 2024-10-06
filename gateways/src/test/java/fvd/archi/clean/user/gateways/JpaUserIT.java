package fvd.archi.clean.user.gateways;

import fvd.archi.clean.RepositoryIT;
import fvd.archi.clean.user.usecases.boundaries.UserRegisterDsGateway;
import fvd.archi.clean.user.usecases.models.UserDsRequestModel;
import fvd.archi.clean.user.usecases.models.UserDsResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RepositoryIT
class JpaUserIT {

  final static LocalDateTime FIXED_TIME = LocalDateTime.of(2024, 06, 07, 02, 01, 10);
  final static List<UserDataMapper> USER_DATA_MAPPERS = List.of(
    new UserDataMapper("login1", "password", FIXED_TIME),
    new UserDataMapper("login2", "password", FIXED_TIME)
  );

  @Autowired
  UserRegisterDsGateway registerDsGateway;
  @Autowired
  JpaUserRepository repository;

  @BeforeEach
  void setup() {
    repository.deleteAll();
    repository.saveAll(USER_DATA_MAPPERS);
  }

  @Test
  void existByName_ifNameExist_shouldReturnTrue() {
    //given setup
    //when
    var result = registerDsGateway.existsByName("login1");
    //then
    assertThat(result).isTrue();
  }

  @Test
  void existByName_ifNameDoesntExist_shouldReturnFalse() {
    //given setup
    //when
    var result = registerDsGateway.existsByName("loginUnknown");
    //then
    assertThat(result).isFalse();
  }

  @Test
  void save_shouldSaveNewUser() {
    //given
    var userDsRequestModel = new UserDsRequestModel("login3", "password", FIXED_TIME);
    //when
    registerDsGateway.save(userDsRequestModel);
    //then
    var result = repository.findById("login3");
    assertThat(result).isPresent().get()
      .returns("login3", UserDataMapper::getName)
      .returns("password", UserDataMapper::getPassword)
      .returns(FIXED_TIME, UserDataMapper::getCreationTime);
  }

  @Test
  void save_shouldUpdateExistingUser() {
    //given
    var userDsRequestModel = new UserDsRequestModel("login2", "newpassword", FIXED_TIME);
    //when
    registerDsGateway.save(userDsRequestModel);
    //then
    var result = repository.findById("login2");
    assertThat(result).isPresent().get()
      .returns("login2", UserDataMapper::getName)
      .returns("newpassword", UserDataMapper::getPassword)
      .returns(FIXED_TIME, UserDataMapper::getCreationTime);
  }

  @Test
  void findAll_shouldReturnAllUsers() {
    //given setup
    //when
    var result = registerDsGateway.findAll();
    //then
    assertThat(result).hasSize(2)
      .usingRecursiveFieldByFieldElementComparator()
      .containsExactly(
        new UserDsResponseModel("login1", FIXED_TIME),
        new UserDsResponseModel("login2", FIXED_TIME)
      );
  }

  @Test
  void findById_ifUserExist_shouldReturnUser() {
    //given setup
    //when
    var result = registerDsGateway.findById("login1");
    //then
    assertThat(result).isNotNull()
      .returns("login1", UserDsResponseModel::getLogin);
  }

  @Test
  void findById_ifUserDoesntExist_shouldThrow() {
    //given setup
    //when & then
    assertThatThrownBy(() -> registerDsGateway.findById("loginUnknown")).isInstanceOf(Exception.class);
  }

  @Test
  void delete_ifUserExist_shouldDeleteUser() {
    //given setup
    //when
    registerDsGateway.delete("login1");
    var result = repository.findById("login1");
    //then
    assertThat(result).isEmpty();
  }

  @Test
  void delete_ifUserDoesntExist_shouldThrow() {
    //given setup
    //when & then
    assertThatThrownBy(() -> registerDsGateway.delete("loginUnknown")).isInstanceOf(Exception.class);
  }

  @Test
  void update_shouldUpdateUser() {
    //given setup
    //when
    var result = registerDsGateway.update(new UserDsRequestModel("login1", "newPass"));
    var newPass = repository.findById("login1");
    //then
    assertThat(result).isNotNull()
      .returns("login1", UserDsResponseModel::getLogin)
      .returns(FIXED_TIME, UserDsResponseModel::getCreationTime);
    assertThat(newPass).isPresent().get()
      .returns("newPass", UserDataMapper::getPassword);
  }
}