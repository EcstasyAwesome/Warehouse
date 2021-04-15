package com.github.ecstasyawesome.warehouse.controller;

import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.core.Controller;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.User;
import java.net.URL;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;

public class Authorization {

  public static final URL FXML = Authorization.class.getResource("/model/Authorization.fxml");
  private final WindowManager windowManager = WindowManager.getInstance();
  private static User user; // TODO default local user

  @FXML
  private TextField loginField;

  @FXML
  private PasswordField passwordField;

  public static Optional<User> getCurrentUser() {
    return Optional.ofNullable(user);
  }

  public static boolean isAccessGranted(Controller controller) {
    var access = controller.access;
    if (access == Access.GUEST) {
      return true;
    }
    if (user == null) {
      return false;
    }
    return user.getAccess().level <= access.level;
  }

  @FXML
  void login() {
    var colorEffect = new ColorAdjust(0, 0.1, 0, 0);
    if (loginField.getText().equals("")) { // TODO local user and DB users
      loginField.setEffect(null);
      if (passwordField.getText().equals("")) { // TODO local user and DB users
        passwordField.setEffect(null);
        user = new User(); // TODO remove (here for test only)
        user.setSurname("Surname");
        user.setFirstName("FirstName");
        user.setSecondName("SecondName");
        user.setAccess(Access.ADMIN);
        WindowManager.getInstance().showScene(Controller.MAIN);
      } else {
        passwordField.setEffect(colorEffect);
      }
    } else {
      loginField.setEffect(colorEffect);
    }
  }

  void logout() {
    user = null;
  }
}

