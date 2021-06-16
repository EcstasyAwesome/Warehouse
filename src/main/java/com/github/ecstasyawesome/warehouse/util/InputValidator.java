package com.github.ecstasyawesome.warehouse.util;

import java.util.Objects;
import java.util.regex.Pattern;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputControl;
import javafx.scene.effect.ColorAdjust;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class InputValidator {

  public static final Pattern PHONE = Pattern.compile("^((\\+?3)?8)?0\\d{9}$");
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
  private static final Logger LOGGER = LogManager.getLogger(InputValidator.class);

  private InputValidator() {
  }

  public static boolean arePasswordsEqual(final PasswordField field1, final PasswordField field2) {
    Objects.requireNonNull(field1);
    Objects.requireNonNull(field2);
    var pass1 = field1.getText();
    var pass2 = field2.getText();
    var result = false;
    if (pass1.isBlank() || pass2.isBlank() || !pass1.equals(pass2)) {
      field1.setEffect(RED_ADJUST);
      field2.setEffect(RED_ADJUST);
    } else {
      field1.setEffect(NO_ADJUST);
      field2.setEffect(NO_ADJUST);
      result = true;
    }
    LOGGER.debug("Password fields are equal [{}]", result);
    return result;
  }

  public static boolean isFieldValid(final ChoiceBox<?> checkBox) {
    Objects.requireNonNull(checkBox);
    var result = checkBox.getValue() != null;
    checkBox.setEffect(result ? NO_ADJUST : RED_ADJUST);
    LOGGER.debug("Checkbox is valid [{}]", result);
    return result;
  }

  public static boolean isFieldValid(final DatePicker datePicker) {
    Objects.requireNonNull(datePicker);
    var result = datePicker.getValue() != null;
    datePicker.setEffect(result ? NO_ADJUST : RED_ADJUST);
    LOGGER.debug("Datepicker is valid [{}]", result);
    return result;
  }

  public static boolean isFieldValid(final TextInputControl field, final Pattern pattern,
      final boolean empty) {
    Objects.requireNonNull(field);
    var result = isFieldValid(field.getText(), pattern, empty);
    field.setEffect(result ? NO_ADJUST : RED_ADJUST);
    return result;
  }

  public static boolean isFieldValid(final String text, final Pattern pattern,
      final boolean empty) {
    var result = text != null && ((empty && text.isEmpty()) || text.matches(pattern.pattern()));
    LOGGER.debug("Field with the text '{}' is being empty [{}] or matches the pattern '{}' [{}]",
        text, empty, pattern, result);
    return result;
  }
}
