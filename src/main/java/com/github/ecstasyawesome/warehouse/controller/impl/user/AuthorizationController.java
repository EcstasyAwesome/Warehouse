package com.github.ecstasyawesome.warehouse.controller.impl.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.NO_ADJUST;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.RED_ADJUST;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.setFieldText;

import com.github.ecstasyawesome.warehouse.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.provider.impl.HomeProvider;
import com.github.ecstasyawesome.warehouse.provider.impl.user.AdministratorRegistrationProvider;
import com.github.ecstasyawesome.warehouse.repository.UserRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.UserRepositoryService;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthorizationController extends AbstractController {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final UserRepository userRepository = UserRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(AuthorizationController.class);

  @FXML
  private TextField loginField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private void initialize() {
    try {
      if (!userRepository.hasTableRecords()) {
        var result = windowManager.showAndGet(AdministratorRegistrationProvider.getInstance());
        if (result.isPresent()) {
          setFieldText(loginField, result.get().getLogin());
        } else {
          windowManager.shutdown();
        }
      }
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
      windowManager.shutdown();
    }
  }

  @FXML
  private void login() {
    var login = loginField.getText();
    try {
      var user = userRepository.select(login);
      loginField.setEffect(NO_ADJUST);
      if (passwordField.getText().equals(user.getPersonSecurity().getPassword())) {
        passwordField.setEffect(NO_ADJUST);
        SessionManager.store("currentUser", user);
        logger.info("Log in '{}'", login);
        windowManager.show(HomeProvider.getInstance());
      } else {
        passwordField.setEffect(RED_ADJUST);
        logger.info("Try to log in with login '{}' and incorrect password", login);
      }
    } catch (NullPointerException exception) {
      loginField.setEffect(RED_ADJUST);
      logger.info("Try to log in with incorrect login '{}'", login);
    }
  }
}

