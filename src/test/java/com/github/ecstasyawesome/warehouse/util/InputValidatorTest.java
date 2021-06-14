package com.github.ecstasyawesome.warehouse.util;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.LOGIN;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NO_ADJUST;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NUMBERS;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PASSWORD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.RED_ADJUST;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.URL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.arePasswordsEqual;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.repository.UniqueField;
import java.util.stream.IntStream;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class InputValidatorTest {

  private static boolean state = false;

  @AfterAll
  public static void afterAll() {
    WindowManager.destroy();
  }

  @Start
  public void start(Stage stage) {
    if (!state) {
      WindowManager.initialize(stage);
      state = true;
    }
  }

  @Test
  public void checkEqualPasswords() {
    var pass1 = new PasswordField();
    pass1.setText("qwerty");
    var pass2 = new PasswordField();
    pass2.setText("qwerty");
    assertTrue(arePasswordsEqual(pass1, pass2));
    assertEquals(NO_ADJUST, pass1.getEffect());
    assertEquals(NO_ADJUST, pass2.getEffect());
  }

  @Test
  public void checkBlankPasswords() {
    var pass1 = new PasswordField();
    pass1.setText("   ");
    var pass2 = new PasswordField();
    pass2.setText("   ");
    assertFalse(arePasswordsEqual(pass1, pass2));
    assertEquals(RED_ADJUST, pass1.getEffect());
    assertEquals(RED_ADJUST, pass2.getEffect());
  }

  @Test
  public void checkNonEqualPasswords() {
    var pass1 = new PasswordField();
    pass1.setText("qwerty");
    var pass2 = new PasswordField();
    pass2.setText("qwe");
    assertFalse(arePasswordsEqual(pass1, pass2));
    assertEquals(RED_ADJUST, pass1.getEffect());
    assertEquals(RED_ADJUST, pass2.getEffect());
  }

  @Test
  public void checkValidChoiceBox() {
    var choiceBox = new ChoiceBox<String>();
    choiceBox.setValue("some text");
    assertTrue(isFieldValid(choiceBox));
    assertEquals(NO_ADJUST, choiceBox.getEffect());
  }

  @Test
  public void checkInvalidChoiceBox() {
    var choiceBox = new ChoiceBox<String>();
    assertFalse(isFieldValid(choiceBox));
    assertEquals(RED_ADJUST, choiceBox.getEffect());
  }

  @Test
  public void checkNotEmptyTextInputControl() {
    var textField = new TextField("some text");
    assertTrue(isFieldValid(textField, WILDCARD, false));
    assertEquals(NO_ADJUST, textField.getEffect());
  }

  @Test
  public void checkEmptyTextInputControl() {
    var textField = new TextField();
    assertTrue(isFieldValid(textField, NAME, true));
    assertEquals(NO_ADJUST, textField.getEffect());
    textField.setText("   ");
    assertFalse(isFieldValid(textField, NAME, true));
    assertEquals(RED_ADJUST, textField.getEffect());
  }

  @Test
  public void checkUniqueFieldTextInputControl() {
    var mock = mock(UniqueField.class);
    when(mock.isFieldUnique(anyString())).thenReturn(true);
    var textField = new TextField("root");
    assertTrue(isFieldValid(textField, "admin", WILDCARD, mock));
    assertEquals(NO_ADJUST, textField.getEffect());
    verify(mock, only()).isFieldUnique("root");
  }

  @Test
  public void checkUniqueFieldTextInputControlWithSameValue() {
    var mock = mock(UniqueField.class);
    var textField = new TextField("root");
    assertTrue(isFieldValid(textField, "root", WILDCARD, mock));
    assertEquals(NO_ADJUST, textField.getEffect());
    verify(mock, never()).isFieldUnique(anyString());
  }

  @Test
  public void checkNotUniqueFieldTextInputControl() {
    var mock = mock(UniqueField.class);
    when(mock.isFieldUnique(anyString())).thenReturn(false);
    var textField = new TextField("root");
    assertFalse(isFieldValid(textField, "admin", WILDCARD, mock));
    assertEquals(RED_ADJUST, textField.getEffect());
    verify(mock, only()).isFieldUnique("root");
  }

  @Test
  public void checkUniqueText() {
    var mock = mock(UniqueField.class);
    when(mock.isFieldUnique(anyString())).thenReturn(true);
    assertTrue(isFieldValid("text", "admin", WILDCARD, mock));
    verify(mock, only()).isFieldUnique("text");
  }

  @Test
  public void checkUniqueTextWithSameValue() {
    var mock = mock(UniqueField.class);
    assertTrue(isFieldValid("root", "root", WILDCARD, mock));
    verify(mock, never()).isFieldUnique(anyString());
  }

  @Test
  public void checkNotUniqueText() {
    var mock = mock(UniqueField.class);
    when(mock.isFieldUnique(anyString())).thenReturn(false);
    assertFalse(isFieldValid("root", "admin", WILDCARD, mock));
    verify(mock, only()).isFieldUnique("root");
  }

  @Test
  public void testPhonePattern() {
    assertFalse("957472288".matches(PHONE.pattern()));
    assertFalse("0957472288q".matches(PHONE.pattern()));
    assertFalse("9957472288q".matches(PHONE.pattern()));
    assertTrue("0957472288".matches(PHONE.pattern()));
    assertTrue("80957472288".matches(PHONE.pattern()));
    assertTrue("380957472288".matches(PHONE.pattern()));
    assertTrue("+380957472288".matches(PHONE.pattern()));
  }

  @Test
  public void testStrictNamePattern() {
    assertTrue("Aa".matches(STRICT_NAME.pattern()));
    assertTrue("Some".matches(STRICT_NAME.pattern()));
    assertFalse("S".matches(STRICT_NAME.pattern()));
    assertFalse("some".matches(STRICT_NAME.pattern()));
  }

  @Test
  public void testNamePattern() {
    assertFalse("S".matches(NAME.pattern()));
    assertTrue("Aa".matches(NAME.pattern()));
    assertTrue("Aa wow awesome $$$".matches(NAME.pattern()));
  }

  @Test
  public void testLoginPattern() {
    assertTrue("login".matches(LOGIN.pattern()));
    assertTrue("login_1568".matches(LOGIN.pattern()));
    assertFalse("log".matches(LOGIN.pattern()));
    assertFalse("tooooooooo_long_login".matches(LOGIN.pattern()));
  }

  @Test
  public void testPasswordPattern() {
    assertTrue("07071994".matches(PASSWORD.pattern()));
    assertTrue("qwertyui".matches(PASSWORD.pattern()));
    assertTrue("qwertyui123736753".matches(PASSWORD.pattern()));
    assertFalse("qwerty".matches(PASSWORD.pattern()));
  }

  @Test
  public void testNumbersPattern() {
    IntStream.range(32, 47)
        .mapToObj(index -> String.valueOf((char) index))
        .forEach(string -> assertFalse(string.matches(NUMBERS.pattern())));

    IntStream.range(48, 57)
        .mapToObj(index -> String.valueOf((char) index))
        .forEach(string -> assertTrue(string.matches(NUMBERS.pattern())));

    IntStream.range(58, 255)
        .mapToObj(index -> String.valueOf((char) index))
        .forEach(string -> assertFalse(string.matches(NUMBERS.pattern())));
  }

  @Test
  public void testEmailPattern() {
    assertTrue("some._email@gmail.com".matches(EMAIL.pattern()));
    assertFalse("some._email@gmail".matches(EMAIL.pattern()));
    assertFalse("some._email.gmail.com".matches(EMAIL.pattern()));
    assertFalse("some._@email@gmail.com".matches(EMAIL.pattern()));
  }

  @Test
  public void testWildcardPattern() {
    assertTrue("v".matches(WILDCARD.pattern()));
    assertTrue("$%&@$".matches(WILDCARD.pattern()));
    assertTrue(" ".matches(WILDCARD.pattern()));
    assertTrue("text".matches(WILDCARD.pattern()));
    assertTrue("some text".matches(WILDCARD.pattern()));
    assertFalse("".matches(WILDCARD.pattern()));
  }

  @Test
  public void testUrlPattern() {
    assertTrue("example.com".matches(URL.pattern()));
    assertTrue("example.ua".matches(URL.pattern()));
    assertTrue("example.ru".matches(URL.pattern()));
    assertTrue("example.i".matches(URL.pattern()));
    assertTrue("some_example.com".matches(URL.pattern()));
    assertTrue("some-example.com".matches(URL.pattern()));
    assertFalse("example".matches(URL.pattern()));
    assertFalse("example.".matches(URL.pattern()));
  }
}