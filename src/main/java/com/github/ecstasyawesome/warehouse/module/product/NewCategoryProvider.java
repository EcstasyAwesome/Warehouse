package com.github.ecstasyawesome.warehouse.module.product;

import com.github.ecstasyawesome.warehouse.controller.product.NewCategory;
import com.github.ecstasyawesome.warehouse.core.FeedbackModule;
import com.github.ecstasyawesome.warehouse.core.FeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import java.net.URL;

public class NewCategoryProvider extends FeedbackModuleProvider<NewCategory, Category> {

  private static final NewCategoryProvider INSTANCE = new NewCategoryProvider();
  private final URL fxml = getClass().getResource("/model/product/NewCategory.fxml");

  private NewCategoryProvider() {
  }

  public static NewCategoryProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FeedbackModule<NewCategory, Category> create() {
    return new FeedbackModule<>(fxml) {
    };
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
