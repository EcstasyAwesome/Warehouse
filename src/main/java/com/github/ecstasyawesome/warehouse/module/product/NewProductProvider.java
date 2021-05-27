package com.github.ecstasyawesome.warehouse.module.product;

import com.github.ecstasyawesome.warehouse.controller.product.NewProduct;
import com.github.ecstasyawesome.warehouse.core.FeedbackModule;
import com.github.ecstasyawesome.warehouse.core.FeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.Product;
import java.net.URL;

public class NewProductProvider extends FeedbackModuleProvider<NewProduct, Product> {

  private static final NewProductProvider INSTANCE = new NewProductProvider();
  private final URL fxml = getClass().getResource("/model/product/NewProduct.fxml");

  private NewProductProvider() {
  }

  public static NewProductProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FeedbackModule<NewProduct, Product> create() {
    return new FeedbackModule<>(fxml) {
    };
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "New product"; // TODO i18n
  }

}
