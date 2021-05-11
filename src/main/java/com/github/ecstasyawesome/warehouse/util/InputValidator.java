package com.github.ecstasyawesome.warehouse.util;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.impl.UserDaoService;
import java.util.regex.Pattern;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;

public final class InputValidator {

  public static final Pattern PHONE = Pattern.compile("^((\\+?3)?8)?\\d{10}$");
  public static final Pattern STRING = Pattern.compile("^[A-ZА-Я][a-zа-я]{1,19}$");
  public static final Pattern LOGIN = Pattern.compile("^[a-z_0-9]{5,20}$");
  public static final Pattern PASSWORD = Pattern.compile("^.{8,20}$");
  public static final ColorAdjust RED_ADJUST = new ColorAdjust(0, 0.1, 0, 0);
  public static final ColorAdjust NO_ADJUST = new ColorAdjust(0, 0, 0, 0);

  private InputValidator() {
  }

  public static boolean arePasswordsEqual(final TextField password1, final TextField password2) {
    var pass1 = password1.getText();
    var pass2 = password2.getText();
    if (pass1.isBlank() || pass2.isBlank() || !pass1.equals(pass2)) {
      password1.setEffect(RED_ADJUST);
      password2.setEffect(RED_ADJUST);
      return false;
    }
    password1.setEffect(NO_ADJUST);
    password2.setEffect(NO_ADJUST);
    return true;
  }

  public static boolean isFieldValid(final TextField field, final Pattern pattern) {
    if (field.getText().matches(pattern.pattern())) {
      field.setEffect(NO_ADJUST);
      return true;
    }
    field.setEffect(RED_ADJUST);
    return false;
  }

  public static boolean isFieldValid(final ChoiceBox<?> checkBox) {
    if (checkBox.getValue() != null) {
      checkBox.setEffect(NO_ADJUST);
      return true;
    }
    checkBox.setEffect(RED_ADJUST);
    return false;
  }

  public static boolean isLoginValid(final TextField field) {
    var userDao = UserDaoService.getInstance();
    var windowManager = WindowManager.getInstance();
    var result = false;
    try {
      result = userDao.isLoginPresent(field.getText());
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
    if (result) {
      field.setEffect(RED_ADJUST);
      return false;
    }
    field.setEffect(NO_ADJUST);
    return true;
  }
}
