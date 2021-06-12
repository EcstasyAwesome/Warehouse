package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.core.controller.Cacheable;

public abstract class Cache {

  public abstract <T> void check(Cacheable<T> instance);

  public abstract void erase();
}
