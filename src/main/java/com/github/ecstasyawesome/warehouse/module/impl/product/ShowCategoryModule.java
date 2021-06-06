package com.github.ecstasyawesome.warehouse.module.impl.product;

import com.github.ecstasyawesome.warehouse.controller.impl.product.ShowCategoryController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.module.AbstractConfiguredModule;
import java.net.URL;

public class ShowCategoryModule extends
    AbstractConfiguredModule<ShowCategoryController, Category> {

  private static final ShowCategoryModule INSTANCE = new ShowCategoryModule();
  private final URL fxml = getClass().getResource("/model/product/ShowCategory.fxml");

  private ShowCategoryModule() {
  }

  public static ShowCategoryModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<ShowCategoryController> create() {
    return new FxmlBundle<>(fxml);
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
