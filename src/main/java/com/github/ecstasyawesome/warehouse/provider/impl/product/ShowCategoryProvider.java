package com.github.ecstasyawesome.warehouse.provider.impl.product;

import com.github.ecstasyawesome.warehouse.controller.impl.product.ShowCategoryController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractConfiguredModuleProvider;
import java.net.URL;

public class ShowCategoryProvider extends
    AbstractConfiguredModuleProvider<ShowCategoryController, Category> {

  private static final ShowCategoryProvider INSTANCE = new ShowCategoryProvider();
  private final URL fxml = getClass().getResource("/model/product/ShowCategory.fxml");

  private ShowCategoryProvider() {
  }

  public static ShowCategoryProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<ShowCategoryController> create() {
    return new FxmlModule<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Show category"; // TODO i18n
  }
}
