package fvd.archi.clean.user.controllers;

import fvd.archi.clean.user.usecases.boundaries.UserInputBoundary;
import fvd.archi.clean.user.usecases.models.UserRequestModel;
import fvd.archi.clean.user.usecases.models.UserResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRegisterController {

  private final UserInputBoundary userInput;

  @PostMapping
  public UserResponseModel create(@RequestBody UserRequestModel requestModel) {
    return userInput.create(requestModel);
  }

  @GetMapping
  public List<UserResponseModel> findAll() {
    return userInput.findAll();
  }

  @GetMapping("{id}")
  public UserResponseModel findById(@PathVariable String id) {
    return userInput.findById(id);
  }

  @PutMapping
  public UserResponseModel update(@RequestBody UserRequestModel requestModel) {
    return userInput.update(requestModel);
  }

  @DeleteMapping("{id}")
  public UserResponseModel delete(@PathVariable String id) {
    return userInput.delete(id);
  }
}