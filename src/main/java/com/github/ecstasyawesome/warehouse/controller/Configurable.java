package com.github.ecstasyawesome.warehouse.controller;

public interface Configurable<T> {

  void apply(T instance);
}
