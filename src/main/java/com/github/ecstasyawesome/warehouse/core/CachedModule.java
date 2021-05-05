package com.github.ecstasyawesome.warehouse.core;

import java.net.URL;

public abstract class CachedModule<T extends CachedController> extends Module<T> {

  public CachedModule(URL url) {
    super(url);
  }
}