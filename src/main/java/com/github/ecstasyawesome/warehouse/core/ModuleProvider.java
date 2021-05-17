package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.model.Access;

public abstract class ModuleProvider<T extends Controller> {

  public abstract Module<T> create();

  public abstract Access getAccess();

  public abstract String getTitle();
}
