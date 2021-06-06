package com.github.ecstasyawesome.warehouse.module.product;

import com.github.ecstasyawesome.warehouse.controller.product.NewCategoryController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractFeedbackModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import java.net.URL;

public class NewCategoryModule extends
    AbstractFeedbackModule<NewCategoryController, Category> {

  private static final NewCategoryModule INSTANCE = new NewCategoryModule();
  private final URL fxml = getClass().getResource("/model/product/NewCategory.fxml");

  private NewCategoryModule() {
  }

  public static NewCategoryModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<NewCategoryController> create() {
    return new FxmlBundle<>(fxml);
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
