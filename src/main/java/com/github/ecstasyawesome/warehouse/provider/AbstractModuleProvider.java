package com.github.ecstasyawesome.warehouse.provider;

import com.github.ecstasyawesome.warehouse.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;

public abstract class AbstractModuleProvider<T extends AbstractController> {

  public abstract FxmlModule<T> create();

  public abstract Access getAccess();

  public abstract String getTitle();
}
