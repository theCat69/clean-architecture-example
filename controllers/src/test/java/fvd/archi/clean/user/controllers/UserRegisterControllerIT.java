package fvd.archi.clean.user.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fvd.archi.clean.RestIT;
import fvd.archi.clean.user.usecases.boundaries.UserRegisterDsGateway;
import fvd.archi.clean.user.usecases.models.UserDsRequestModel;
import fvd.archi.clean.user.usecases.models.UserDsResponseModel;
import fvd.archi.clean.user.usecases.models.UserRequestModel;
import fvd.archi.clean.user.usecases.models.UserResponseModel;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestIT
class UserRegisterControllerIT {
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  MockMvc mockMvc;
  @Autowired
  UserRegisterDsGateway userRegisterDsGateway;

  @Test
  @SneakyThrows
  void create_withValidBody_shouldCallRegisterGateway() {
    //given
    var userRequestModel = new UserRequestModel("login", "password");
    var userRequestModelJson = objectMapper.writeValueAsString(userRequestModel);
    var userDsRequestModelCaptor = ArgumentCaptor.forClass(UserDsRequestModel.class);
    when(userRegisterDsGateway.existsByName(anyString())).thenReturn(false);
    // when
    mockMvc.perform(
        post("/user")
          .contentType(MediaType.APPLICATION_JSON)
          .content(userRequestModelJson)
      ).andDo(print())
      .andExpect(status().isOk());
    //then
    verify(userRegisterDsGateway, times(1)).save(userDsRequestModelCaptor.capture());
    assertThat(userDsRequestModelCaptor.getValue())
      .returns("login", UserDsRequestModel::getName)
      .returns("password", UserDsRequestModel::getPassword);
  }

  @Test
  @SneakyThrows
  void create_withAlreadyExistingUser_shouldFailWithConflict() {
    //given
    var userRequestModel = new UserRequestModel("login", "password");
    var userRequestModelJson = objectMapper.writeValueAsString(userRequestModel);
    when(userRegisterDsGateway.existsByName(anyString())).thenReturn(true);
    // when
    mockMvc.perform(
        post("/user")
          .contentType(MediaType.APPLICATION_JSON)
          .content(userRequestModelJson)
      ).andDo(print())
      .andExpect(status().isConflict());
    //then
    verify(userRegisterDsGateway, never()).save(any(UserDsRequestModel.class));
  }

  @Test
  @SneakyThrows
  void create_withNotValidPassword_shouldFailWithConflict() {
    //given
    var userRequestModel = new UserRequestModel("login", "pas");
    var userRequestModelJson = objectMapper.writeValueAsString(userRequestModel);
    when(userRegisterDsGateway.existsByName(anyString())).thenReturn(false);
    // when
    mockMvc.perform(
        post("/user")
          .contentType(MediaType.APPLICATION_JSON)
          .content(userRequestModelJson)
      ).andDo(print())
      .andExpect(status().isConflict());
    //then
    verify(userRegisterDsGateway, never()).save(any(UserDsRequestModel.class));
  }

  @Test
  @SneakyThrows
  void findAll_shouldReturnListOfUsers() {
    //given
    var userDsResponseModels = List.of(
      new UserDsResponseModel("login1", LocalDateTime.of(2024, 1, 1, 12, 10, 5)),
      new UserDsResponseModel("login2", LocalDateTime.of(2024, 2, 2, 13, 10, 5))
      );
    when(userRegisterDsGateway.findAll()).thenReturn(userDsResponseModels);
    //when
    var result = mockMvc.perform(
      get("/user")
    ).andDo(print())
      .andExpect(status().isOk()).andReturn();
    //then
    var resultList = objectMapper.readValue(result.getResponse().getContentAsString(),
      new TypeReference<List<UserResponseModel>>() {});
    assertThat(resultList).hasSize(2);
    assertThat(resultList.get(0))
      .returns("login1", UserResponseModel::getLogin)
      .returns("12:10:05", UserResponseModel::getCreationTime);
    assertThat(resultList.get(1))
      .returns("login2", UserResponseModel::getLogin)
      .returns("13:10:05", UserResponseModel::getCreationTime);
  }

  @Test
  @SneakyThrows
  void findById_userExist_shouldReturnUser() {
    //given
    var userDsResponseModel = new UserDsResponseModel("login1", LocalDateTime.of(2024, 1, 1, 12, 10, 5));
    when(userRegisterDsGateway.existsByName(anyString())).thenReturn(true);
    when(userRegisterDsGateway.findById(anyString())).thenReturn(userDsResponseModel);
    //when
    var result = mockMvc.perform(
        get("/user/{id}", "login1")
      ).andDo(print())
      .andExpect(status().isOk()).andReturn();
    //then
    var resultObject = objectMapper.readValue(result.getResponse().getContentAsString(),
      UserResponseModel.class);
    assertThat(resultObject)
      .returns("login1", UserResponseModel::getLogin)
      .returns("12:10:05", UserResponseModel::getCreationTime);
  }

  @Test
  @SneakyThrows
  void findById_userDoesNotExist_shouldReturnConflict() {
    //given
    var userDsResponseModel = new UserDsResponseModel("login1", LocalDateTime.of(2024, 1, 1, 12, 10, 5));
    when(userRegisterDsGateway.existsByName(anyString())).thenReturn(false);
    //when & then
    var result = mockMvc.perform(
        get("/user/{id}", "login1")
      ).andDo(print())
      .andExpect(status().isConflict());
  }
}