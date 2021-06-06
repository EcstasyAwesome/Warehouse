package com.github.ecstasyawesome.warehouse.core.controller;

public interface Configurable<T> {

  void apply(T instance);
}
