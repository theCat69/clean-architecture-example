package fvd.archi.clean;

import fvd.archi.clean.user.usecases.boundaries.UserRegisterDsGateway;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

  @MockBean
  UserRegisterDsGateway userRegisterDsGateway;
}
