package com.github.ecstasyawesome.warehouse.module.product;

import com.github.ecstasyawesome.warehouse.controller.product.EditCategory;
import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackModule;
import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.Category;
import java.net.URL;

public class EditCategoryProvider extends ConfiguredFeedbackModuleProvider<EditCategory, Category> {

  private static final EditCategoryProvider INSTANCE = new EditCategoryProvider();
  public final URL fxml = getClass().getResource("/model/product/EditCategory.fxml");

  private EditCategoryProvider() {
  }

  public static EditCategoryProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public ConfiguredFeedbackModule<EditCategory, Category> create() {
    return new ConfiguredFeedbackModule<>(fxml) {
    };
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
