package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.core.controller.Cacheable;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ControllerCache extends Cache {

  private final Logger logger = LogManager.getLogger(ControllerCache.class);
  private final Map<Class<? extends Cacheable>, Cacheable> cachedControllers = new HashMap<>();

  @Override
  public <T> void check(Cacheable<T> instance) {
    var clazz = instance.getClass();
    if (cachedControllers.containsKey(clazz)) {
      var cacheable = cachedControllers.get(clazz);
      if (cacheable.isReady()) {
        var backup = (T) cacheable.backup();
        instance.recover(backup);
        logger.debug("Recovered from cache a controller instance '{}'", clazz.getSimpleName());
      }
    }
    cachedControllers.put(clazz, instance);
    logger.debug("Stored a new controller instance '{}'", clazz.getSimpleName());
  }

  @Override
  public void erase() {
    cachedControllers.clear();
  }
}
