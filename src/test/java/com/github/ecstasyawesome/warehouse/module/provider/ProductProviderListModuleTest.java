package com.github.ecstasyawesome.warehouse.module.provider;

import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkAccess;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkFxmlBundle;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkTitle;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.initCurrentModule;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.initSessionUser;

import com.github.ecstasyawesome.warehouse.model.Access;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class ProductProviderListModuleTest {

  private final ProductProviderListModule module = ProductProviderListModule.getInstance();

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