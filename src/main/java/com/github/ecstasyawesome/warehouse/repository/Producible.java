package com.github.ecstasyawesome.warehouse.repository;

public interface Producible<T> {

  void create(T instance);
}
