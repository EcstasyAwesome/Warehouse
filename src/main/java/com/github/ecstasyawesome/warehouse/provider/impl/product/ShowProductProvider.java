package com.github.ecstasyawesome.warehouse.provider.impl.product;

import com.github.ecstasyawesome.warehouse.controller.impl.product.ShowProductController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import com.github.ecstasyawesome.warehouse.module.AbstractModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractConfiguredModuleProvider;
import java.net.URL;

public class ShowProductProvider extends
    AbstractConfiguredModuleProvider<ShowProductController, Product> {

  private static final ShowProductProvider INSTANCE = new ShowProductProvider();
  private final URL fxml = getClass().getResource("/model/product/ShowProduct.fxml");

  private ShowProductProvider() {
  }

  public static ShowProductProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public AbstractModule<ShowProductController> create() {
    return new AbstractModule<>(fxml) {
    };
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Show product"; // TODO i18n
  }
}
