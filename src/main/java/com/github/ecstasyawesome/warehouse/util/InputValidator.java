package com.github.ecstasyawesome.warehouse.util;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.repository.UniqueField;
import java.util.regex.Pattern;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputControl;
import javafx.scene.effect.ColorAdjust;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class InputValidator {

  public static final Pattern PHONE = Pattern.compile("^((\\+?3)?8)?\\d{10}$");
  public static final Pattern STRICT_NAME = Pattern.compile("^[A-ZА-Я][a-zа-я]+$");
  public static final Pattern NAME = Pattern.compile("^[A-ZА-Я].+$");
  public static final Pattern LOGIN = Pattern.compile("^[a-z_0-9]{5,20}$");
  public static final Pattern PASSWORD = Pattern.compile("^.{8,20}$");
  public static final Pattern NUMBERS = Pattern.compile("^\\d+$");
  public static final Pattern EMAIL = Pattern.compile("^[a-z0-9._]+@[a-z0-9]+\\.[a-z]+$");
  public static final Pattern WILDCARD = Pattern.compile("^.+$");
  public static final Pattern URL = Pattern.compile("^[a-z0-9-_]+\\.[a-z]+$");
  public static final ColorAdjust RED_ADJUST = new ColorAdjust(0, 0.1, 0, 0);
  public static final ColorAdjust NO_ADJUST = new ColorAdjust(0, 0, 0, 0);
  private static final WindowManager WINDOW_MANAGER = WindowManager.getInstance();
  private static final Logger LOGGER = LogManager.getLogger(InputValidator.class);

  private InputValidator() {
  }

  public static boolean arePasswordsEqual(final PasswordField password1,
      final PasswordField password2) {
    var pass1 = password1.getText();
    var pass2 = password2.getText();
    var result = false;
    if (pass1.isBlank() || pass2.isBlank() || !pass1.equals(pass2)) {
      password1.setEffect(RED_ADJUST);
      password2.setEffect(RED_ADJUST);
    } else {
      password1.setEffect(NO_ADJUST);
      password2.setEffect(NO_ADJUST);
      result = true;
    }
    LOGGER.debug("Password fields are equal [{}]", result);
    return result;
  }

  public static boolean isFieldValid(final ChoiceBox<?> checkBox) {
    if (checkBox.getValue() != null) {
      checkBox.setEffect(NO_ADJUST);
      return true;
    }
    checkBox.setEffect(RED_ADJUST);
    return false;
  }

  public static boolean isFieldValid(final DatePicker datePicker) {
    if (datePicker.getValue() != null) {
      datePicker.setEffect(NO_ADJUST);
      return true;
    }
    datePicker.setEffect(RED_ADJUST);
    return false;
  }

  public static boolean isFieldValid(final TextInputControl field, final Pattern pattern,
      final boolean empty) {
    var text = field.getText();
    var result = false;
    if ((empty && text.isEmpty()) || text.matches(pattern.pattern())) {
      field.setEffect(NO_ADJUST);
      result = true;
    } else {
      field.setEffect(RED_ADJUST);
    }
    LOGGER.debug("Field with the text '{}' is being empty [{}] or matches the pattern '{}' [{}]",
        text, empty, pattern, result);
    return result;
  }

  public static boolean isFieldValid(final TextInputControl field, final String previous,
      final Pattern pattern, final UniqueField repository) {
    var text = field.getText();
    var result = false;
    if (isFieldValid(field, pattern, false)) {
      try {
        result = text.equals(previous) || repository.isFieldUnique(text);
      } catch (NullPointerException exception) {
        WINDOW_MANAGER.showDialog(exception);
      }
      field.setEffect(result ? NO_ADJUST : RED_ADJUST);
    }
    LOGGER.debug("Field with text '{}' validated successfully [{}]", text, result);
    return result;
  }

  public static boolean isFieldValid(final String text, final String previous,
      final Pattern pattern, final UniqueField repository) {
    var result = false;
    if (text.matches(pattern.pattern())) {
      LOGGER.debug("Text '{}' matches pattern '{}' [{}]", text, pattern, true);
      try {
        result = text.equals(previous) || repository.isFieldUnique(text);
      } catch (NullPointerException exception) {
        WINDOW_MANAGER.showDialog(exception);
      }
    }
    LOGGER.debug("Text '{}' validated successfully [{}]", text, result);
    return result;
  }

  public static String getTextOrEmpty(final String text) {
    return text == null ? "" : text;
  }

  public static String getNullOrText(final String text) {
    return text == null || text.isEmpty() ? null : text;
  }

  public static void setFieldText(final TextInputControl field, final String text) {
    field.setText(getTextOrEmpty(text));
  }

  public static String getFieldText(final TextInputControl field) {
    return getNullOrText(field.getText());
  }
}
