package com.github.ecstasyawesome.warehouse.core;

public interface Cacheable {

  boolean isReady();

  void backup();

  void recover();
}
