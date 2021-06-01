package com.github.ecstasyawesome.warehouse.controller;

public interface Cacheable {

  boolean isReady();

  void backup();

  void recover();
}
