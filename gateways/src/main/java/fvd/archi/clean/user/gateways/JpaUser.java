package fvd.archi.clean.user.gateways;

import fvd.archi.clean.user.usecases.boundaries.UserRegisterDsGateway;
import fvd.archi.clean.user.usecases.models.UserDsRequestModel;
import fvd.archi.clean.user.usecases.models.UserDsResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JpaUser implements UserRegisterDsGateway {

  private final JpaUserRepository repository;

  @Override
  public boolean existsByName(String name) {
    return repository.existsById(name);
  }

  @Override
  public void save(UserDsRequestModel requestModel) {
    UserDataMapper accountDataMapper = new UserDataMapper(requestModel.getName(), requestModel.getPassword(), requestModel.getCreationTime());
    repository.save(accountDataMapper);
  }

  @Override
  public List<UserDsResponseModel> findAll() {
    return repository.findAll().stream().map(userDataMapper -> new UserDsResponseModel(userDataMapper.getName(),
      userDataMapper.getCreationTime()))
      .toList();
  }

  @Override
  public UserDsResponseModel findById(String id) {
    return repository.findById(id).map(userDataMapper -> new UserDsResponseModel(userDataMapper.getName(),
      userDataMapper.getCreationTime())).orElseThrow();
  }

  @Override
  public UserDsResponseModel delete(String id) {
    Optional<UserDataMapper> userDataMapper = repository.findById(id);
    repository.deleteById(id);
    return userDataMapper.map(userData -> new UserDsResponseModel(userData.getName(),
      userData.getCreationTime())).orElseThrow();
  }

  @Override
  public UserDsResponseModel update(UserDsRequestModel requestModel) {
    UserDataMapper userDataMapper = repository.findById(requestModel.getName()).orElseThrow();
    UserDataMapper userDataMapperSave = repository.save(new UserDataMapper(requestModel.getName(),
      requestModel.getPassword(), userDataMapper.getCreationTime()));
    return new UserDsResponseModel(userDataMapperSave.getName(), userDataMapperSave.getCreationTime());
  }
}