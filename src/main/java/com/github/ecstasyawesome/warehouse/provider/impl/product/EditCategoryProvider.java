package com.github.ecstasyawesome.warehouse.provider.impl.product;

import com.github.ecstasyawesome.warehouse.controller.impl.product.EditCategoryController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractConfiguredModuleProvider;
import java.net.URL;

public class EditCategoryProvider extends
    AbstractConfiguredModuleProvider<EditCategoryController, Category> {

  private static final EditCategoryProvider INSTANCE = new EditCategoryProvider();
  private final URL fxml = getClass().getResource("/model/product/EditCategory.fxml");

  private EditCategoryProvider() {
  }

  public static EditCategoryProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<EditCategoryController> create() {
    return new FxmlModule<>(fxml);
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
