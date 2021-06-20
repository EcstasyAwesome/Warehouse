package com.github.ecstasyawesome.warehouse.module;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.core.module.AbstractModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.util.SessionManager;

public final class ModuleTester {

  public static <T extends AbstractController> void checkFxmlBundle(FxmlBundle<T> fxmlBundle) {
    assertNotNull(fxmlBundle);
    assertNotNull(fxmlBundle.getScene());
    assertNotNull(fxmlBundle.getController());
    assertNotNull(fxmlBundle.getParent());
  }

  public static void checkTitle(String title) {
    assertNotNull(title);
    assertFalse(title.isBlank());
  }

  public static void checkAccess(Access expected, Access actual) {
    assertNotNull(expected);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  public static void initSessionUser() {
    SessionManager.store("currentUser", createUser("login"));
  }

  public static void initCurrentModule(AbstractModule<? extends AbstractController> module) {
    var windowManager = WindowManager.getInstance();
    try {
      var field = windowManager.getClass().getDeclaredField("currentModule");
      field.setAccessible(true);
      field.set(windowManager, module);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalAccessError();
    }
  }
}
