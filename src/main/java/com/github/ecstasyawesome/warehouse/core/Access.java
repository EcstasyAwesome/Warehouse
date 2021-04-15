package com.github.ecstasyawesome.warehouse.core;

public enum Access {

  ADMIN(0), USER(1), GUEST(2);

  public final int level;

  Access(int level) {
    this.level = level;
  }
}
