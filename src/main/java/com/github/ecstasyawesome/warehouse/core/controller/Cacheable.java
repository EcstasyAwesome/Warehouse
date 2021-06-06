package com.github.ecstasyawesome.warehouse.core.controller;

public interface Cacheable {

  boolean isReady();

  void backup();

  void recover();
}
