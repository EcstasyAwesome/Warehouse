package com.github.ecstasyawesome.warehouse.core;

public interface ModuleProvider<T extends Controller> {

  Module<T> create();

  Access getAccess();

  String getTitle();
}
