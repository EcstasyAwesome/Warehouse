package com.github.ecstasyawesome.warehouse.module.user;

import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkAccess;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkFxmlBundle;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkTitle;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.Access;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class AuthorizationModuleTest {

  private final AuthorizationModule module = AuthorizationModule.getInstance();

  @Start
  public void start(Stage stage) {
    WindowManager.getInstance();
  }

  @Test
  public void testCreating() {
    Platform.runLater(() -> checkFxmlBundle(module.create()));
  }

  @Test
  public void testAccess() {
    checkAccess(Access.GUEST, module.getAccess());
  }

  @Test
  public void testTitle() {
    checkTitle(module.getTitle());
  }
}