package com.github.ecstasyawesome.warehouse.module.storage;

import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkAccess;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkFxmlBundle;
import static com.github.ecstasyawesome.warehouse.module.ModuleTester.checkTitle;

import com.github.ecstasyawesome.warehouse.model.Access;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
public class ShowProductStorageModuleTest {

  private final ShowProductStorageModule module = ShowProductStorageModule.getInstance();

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