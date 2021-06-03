package com.github.ecstasyawesome.warehouse.provider;

import com.github.ecstasyawesome.warehouse.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.module.AbstractModule;

public abstract class AbstractModuleProvider<T extends AbstractController> implements
    ModuleFactory<AbstractModule<T>> {

  public abstract Access getAccess();

  public abstract String getTitle();
}