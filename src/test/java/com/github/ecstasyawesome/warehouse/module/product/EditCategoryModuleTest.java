package com.github.ecstasyawesome.warehouse.module.product;

import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkAccess;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkFxmlBundle;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkTitle;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.Access;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class EditCategoryModuleTest {

  private final EditCategoryModule module = EditCategoryModule.getInstance();

  @Start
  public void start(Stage stage) {
    WindowManager.getInstance();
  }

  @Test
  public void testCreating() {
    checkFxmlBundle(module.create());
  }

  @Test
  public void testAccess() {
    checkAccess(Access.ADMIN, module.getAccess());
  }

  @Test
  public void testTitle() {
    checkTitle(module.getTitle());
  }
}