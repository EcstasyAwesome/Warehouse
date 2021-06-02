package com.github.ecstasyawesome.warehouse.controller.impl.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.getFieldText;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.setFieldText;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.repository.UserRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.UserRepositoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EditUserController extends AbstractConfiguredController<User> {

  private final UserRepository userRepository = UserRepositoryService.getInstance();
  private final WindowManager windowManager = WindowManager.getInstance();
  private final Logger logger = LogManager.getLogger(EditUserController.class);
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
      user.setSurname(getFieldText(surnameField));
      user.setName(getFieldText(nameField));
      user.setSecondName(getFieldText(secondNameField));
      user.getPersonContact().setPhone(getFieldText(phoneField));
      user.getPersonContact().setEmail(getFieldText(emailField));
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
  public void apply(User user) {
    this.user = user;
    setFieldText(surnameField, user.getSurname());
    setFieldText(nameField, user.getName());
    setFieldText(secondNameField, user.getSecondName());
    setFieldText(phoneField, user.getPersonContact().getPhone());
    setFieldText(emailField, user.getPersonContact().getEmail());
    setFieldText(loginField, user.getPersonSecurity().getLogin());
    setFieldText(passwordField, user.getPersonSecurity().getPassword());
    accessChoiceBox.setValue(user.getPersonSecurity().getAccess());
  }
}
