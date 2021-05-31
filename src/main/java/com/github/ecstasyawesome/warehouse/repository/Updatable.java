package com.github.ecstasyawesome.warehouse.repository;

public interface Updatable<T> {

  void update(T instance);
}
