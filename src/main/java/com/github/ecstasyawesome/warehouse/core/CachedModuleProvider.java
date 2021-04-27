package com.github.ecstasyawesome.warehouse.core;

public abstract class CachedModuleProvider<T extends CachedController> extends ModuleProvider<T> {

  @Override
  public abstract CachedModule<T> create();
}
