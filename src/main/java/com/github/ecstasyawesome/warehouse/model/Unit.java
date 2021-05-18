package com.github.ecstasyawesome.warehouse.model;

import java.text.DecimalFormat;
import java.text.ParseException;

public enum Unit {

  PC, KILO, LITER;

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(".00#");

  public static String convert(Double value) {
    return DECIMAL_FORMAT.format(value.doubleValue());
  }

  public static Double convert(String value) {
    try {
      return DECIMAL_FORMAT.parse(value).doubleValue();
    } catch (ParseException e) {
      return 0D;
    }
  }
}
