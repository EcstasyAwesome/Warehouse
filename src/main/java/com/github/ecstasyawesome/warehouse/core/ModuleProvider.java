package com.github.ecstasyawesome.warehouse.core;

public abstract class ModuleProvider<T extends Controller> {

  public abstract Module<T> create();

  public abstract Access getAccess();

  public abstract String getTitle();
}
