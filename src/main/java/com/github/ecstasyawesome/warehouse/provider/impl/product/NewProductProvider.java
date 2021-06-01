package com.github.ecstasyawesome.warehouse.provider.impl.product;

import com.github.ecstasyawesome.warehouse.controller.impl.product.NewProductController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import com.github.ecstasyawesome.warehouse.module.AbstractFeedbackModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractFeedbackModuleProvider;
import java.net.URL;

public class NewProductProvider extends
    AbstractFeedbackModuleProvider<NewProductController, Product> {

  private static final NewProductProvider INSTANCE = new NewProductProvider();
  private final URL fxml = getClass().getResource("/model/product/NewProduct.fxml");

  private NewProductProvider() {
  }

  public static NewProductProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public AbstractFeedbackModule<NewProductController, Product> create() {
    return new AbstractFeedbackModule<>(fxml) {
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
