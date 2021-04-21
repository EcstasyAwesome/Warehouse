package com.github.ecstasyawesome.warehouse.controller;

import com.github.ecstasyawesome.warehouse.core.Controller;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.module.HomeModuleFactory;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;

public class Authorization extends Controller {

  private final WindowManager windowManager = WindowManager.getInstance();

  @FXML
  private TextField loginField;

  @FXML
  private PasswordField passwordField;

  @FXML
  void login() {
    var colorEffect = new ColorAdjust(0, 0.1, 0, 0);
    if (loginField.getText().equals("")) { // TODO local user and DB users
      loginField.setEffect(null);
      if (passwordField.getText().equals("")) { // TODO local user and DB users
        passwordField.setEffect(null);
        windowManager.show(HomeModuleFactory.INSTANCE);
      } else {
        passwordField.setEffect(colorEffect);
      }
    } else {
      loginField.setEffect(colorEffect);
    }
  }
}

