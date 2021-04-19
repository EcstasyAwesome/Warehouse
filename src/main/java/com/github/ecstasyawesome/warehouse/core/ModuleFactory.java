package com.github.ecstasyawesome.warehouse.core;

public interface ModuleFactory<T extends Controller> {

  ModuleWrapper<T> create();
}
