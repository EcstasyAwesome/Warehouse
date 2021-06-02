package com.github.ecstasyawesome.warehouse.model;

public interface Recoverable<T> {

  void recover(T instance);
}
