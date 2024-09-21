package fvd.archi.clean.user.gateways;

import fvd.archi.clean.user.usecases.boundaries.UserRegisterDsGateway;
import fvd.archi.clean.user.usecases.models.UserDsRequestModel;
import fvd.archi.clean.user.usecases.models.UserDsResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}