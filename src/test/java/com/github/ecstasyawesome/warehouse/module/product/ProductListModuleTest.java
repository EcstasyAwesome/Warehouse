package com.github.ecstasyawesome.warehouse.module.product;

import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkAccess;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkFxmlBundle;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkTitle;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.initCurrentModule;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.initSessionUser;
import static org.junit.jupiter.api.Assertions.*;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.Access;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class ProductListModuleTest {
  private final ProductListModule module = ProductListModule.getInstance();

  @Start
  public void start(Stage stage) {
    initCurrentModule(module);
  }

  @Test
  public void testCreating() {
    initSessionUser();
    checkFxmlBundle(module.create());
  }

  @Test
  public void testAccess() {
    checkAccess(Access.USER, module.getAccess());
  }

  @Test
  public void testTitle() {
    checkTitle(module.getTitle());
  }
}