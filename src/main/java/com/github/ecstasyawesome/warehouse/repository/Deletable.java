package com.github.ecstasyawesome.warehouse.repository;

public interface Deletable<T> {

  void delete(T instance);
}
