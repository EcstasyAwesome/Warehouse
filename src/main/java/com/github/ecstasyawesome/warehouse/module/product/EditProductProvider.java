package com.github.ecstasyawesome.warehouse.module.product;

import com.github.ecstasyawesome.warehouse.controller.product.EditProduct;
import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackModule;
import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.Product;
import java.net.URL;

public class EditProductProvider extends ConfiguredFeedbackModuleProvider<EditProduct, Product> {

  public static final EditProductProvider INSTANCE = new EditProductProvider();
  public final URL fxml = getClass().getResource("/model/product/EditProduct.fxml");

  private EditProductProvider() {
  }

  @Override
  public ConfiguredFeedbackModule<EditProduct, Product> create() {
    return new ConfiguredFeedbackModule<>(fxml) {
    };
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Edit product"; // TODO i18n
  }
}
