package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredController;
import java.net.URL;

public abstract class AbstractConfiguredModule<T extends AbstractConfiguredController<E>, E> extends
    AbstractModule<T> {

  public AbstractConfiguredModule(final URL url) {
    super(url);
  }
}
