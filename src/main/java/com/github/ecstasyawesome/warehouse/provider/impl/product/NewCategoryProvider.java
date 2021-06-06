package com.github.ecstasyawesome.warehouse.provider.impl.product;

import com.github.ecstasyawesome.warehouse.controller.impl.product.NewCategoryController;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.provider.AbstractFeedbackModuleProvider;
import java.net.URL;

public class NewCategoryProvider extends
    AbstractFeedbackModuleProvider<NewCategoryController, Category> {

  private static final NewCategoryProvider INSTANCE = new NewCategoryProvider();
  private final URL fxml = getClass().getResource("/model/product/NewCategory.fxml");

  private NewCategoryProvider() {
  }

  public static NewCategoryProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<NewCategoryController> create() {
    return new FxmlModule<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "New category"; // TODO i18n
  }
}
