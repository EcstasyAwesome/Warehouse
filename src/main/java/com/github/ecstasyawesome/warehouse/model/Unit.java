package com.github.ecstasyawesome.warehouse.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

public enum Unit {

  PC(0), KILO(3), LITER(3);

  private static final StringConverter<Double> CONVERTER = new CustomDoubleStringConverter();
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00#");

  private final int roundingScale;

  Unit(int roundingScale) {
    this.roundingScale = roundingScale;
  }

  public static ObservableList<Unit> getUnits() {
    return FXCollections.observableArrayList(Unit.values());
  }

  public static StringConverter<Double> getConverter() {
    return CONVERTER;
  }

  public static String convert(final Double value) {
    Objects.requireNonNull(value);
    return DECIMAL_FORMAT.format(value.doubleValue());
  }

  public static Double convert(final String value) {
    Objects.requireNonNull(value);
    try {
      return DECIMAL_FORMAT.parse(value).doubleValue();
    } catch (ParseException e) {
      return 0D;
    }
  }

  public Double round(final Double value) {
    Objects.requireNonNull(value);
    return new BigDecimal(value.toString())
        .setScale(roundingScale, RoundingMode.FLOOR)
        .doubleValue();
  }

  public Double getPositiveOrZero(final Double value) {
    Objects.requireNonNull(value);
    return value < 0D ? 0D : value;
  }

  public static class CustomDoubleStringConverter extends StringConverter<Double> {

    private CustomDoubleStringConverter() {
    }

    @Override
    public String toString(Double value) {
      return convert(value);
    }

    @Override
    public Double fromString(String text) {
      return convert(text);
    }
  }
}
