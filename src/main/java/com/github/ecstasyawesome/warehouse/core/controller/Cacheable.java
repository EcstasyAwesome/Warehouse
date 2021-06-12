package com.github.ecstasyawesome.warehouse.core.controller;

public interface Cacheable<T> {

  boolean isReady();

  T backup();

  void recover(T instance);
}
