package com.github.ecstasyawesome.warehouse.core.module;

import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.model.Access;

public abstract class AbstractModule<T extends AbstractController> {

  public abstract FxmlBundle<T> create();

  public abstract Access getAccess();

  public abstract String getTitle();
}
