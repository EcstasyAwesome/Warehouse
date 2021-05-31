package com.github.ecstasyawesome.warehouse.controller.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.repository.UserRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.UserRepositoryService;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EditUser extends ConfiguredFeedbackController<User> {

  private final UserRepository userRepository = UserRepositoryService.getInstance();
  private final WindowManager windowManager = WindowManager.getInstance();
  private final Logger logger = LogManager.getLogger(EditUser.class);
  private User user;
  @FXML
  private TextField surnameField;

  @FXML
  private TextField nameField;

  @FXML
  private TextField secondNameField;

  @FXML
  private TextField phoneField;

  @FXML
  private TextField emailField;

  @FXML
  private ChoiceBox<Access> accessChoiceBox;

  @FXML
  private TextField loginField;

  @FXML
  private TextField passwordField;

  @FXML
  private void initialize() {
    accessChoiceBox.setItems(Access.getAccesses());
  }

  @FXML
  private void save(ActionEvent event) {
    if (isFieldValid(surnameField, STRICT_NAME, false) & isFieldValid(nameField, STRICT_NAME, false)
        & isFieldValid(secondNameField, STRICT_NAME, false) & isFieldValid(phoneField, PHONE, false)
        & isFieldValid(emailField, EMAIL, true) & isFieldValid(accessChoiceBox)) {
      var userCopy = new User(user);
      user.setSurname(surnameField.getText());
      user.setName(nameField.getText());
      user.setSecondName(secondNameField.getText());
      user.getPersonContact().setPhone(phoneField.getText());
      user.getPersonContact().setEmail(emailField.getText());
      user.getPersonSecurity().setAccess(accessChoiceBox.getValue());
      if (!user.equals(userCopy)) {
        try {
          userRepository.update(user);
          logger.info("Edited user profile with id={}", user.getId());
          closeCurrentStage(event);
        } catch (NullPointerException exception) {
          user.setSurname(userCopy.getSurname());
          user.setName(userCopy.getName());
          user.setSecondName(userCopy.getSecondName());
          user.setPersonContact(userCopy.getPersonContact());
          user.setPersonSecurity(userCopy.getPersonSecurity());
          windowManager.showDialog(exception);
        }
      }
    }
  }

  @Override
  public void accept(User user) {
    this.user = user;
    surnameField.setText(user.getSurname());
    nameField.setText(user.getName());
    secondNameField.setText(user.getSecondName());
    phoneField.setText(user.getPersonContact().getPhone());
    emailField.setText(Objects.requireNonNullElse(user.getPersonContact().getEmail(), ""));
    accessChoiceBox.setValue(user.getPersonSecurity().getAccess());
    loginField.setText(user.getPersonSecurity().getLogin());
    passwordField.setText(user.getPersonSecurity().getPassword());
  }

  @Override
  public User get() {
    return user;
  }
}
