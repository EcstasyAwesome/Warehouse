package com.github.ecstasyawesome.warehouse.module.product;

import com.github.ecstasyawesome.warehouse.controller.product.EditCategoryController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.core.module.AbstractConfiguredModule;
import java.net.URL;

public class EditCategoryModule extends
    AbstractConfiguredModule<EditCategoryController, Category> {

  private static final EditCategoryModule INSTANCE = new EditCategoryModule();
  private final URL fxml = getClass().getResource("/model/product/EditCategory.fxml");

  private EditCategoryModule() {
  }

  public static EditCategoryModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<EditCategoryController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.ADMIN;
  }

  @Override
  public String getTitle() {
    return "Edit category"; // TODO i18n
  }
}
