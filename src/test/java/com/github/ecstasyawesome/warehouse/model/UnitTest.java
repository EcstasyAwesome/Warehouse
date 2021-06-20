package com.github.ecstasyawesome.warehouse.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Function;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

public class UnitTest {

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testGetPersonTypes() {
    var actual = Unit.getUnits();
    assertTrue(actual instanceof ObservableList);
    assertArrayEquals(Unit.values(), actual.toArray());
  }

  @Test
  public void testGetConverter() {
    var converter1 = Unit.getConverter();
    var converter2 = Unit.getConverter();
    assertNotNull(converter1);
    assertNotNull(converter2);
    assertSame(converter1, converter2);
  }

  @Test
  public void testConvertDoubleToString() {
    convertDoubleToString(Unit::convert);
  }

  @Test
  public void testConverterConvertDoubleToString() {
    convertDoubleToString(Unit.getConverter()::toString);
  }

  @Test
  public void checkConvertDoubleToStringThrowsNpe() {
    assertThrows(NullPointerException.class, () -> Unit.convert((Double) null));
  }

  @Test
  public void checkConverterConvertDoubleToStringThrowsNpe() {
    assertThrows(NullPointerException.class, () -> Unit.getConverter().toString(null));
  }

  @Test
  public void testConvertStringToDouble() {
    convertStringToDouble(Unit::convert);
  }

  @Test
  public void testConverterConvertStringToDouble() {
    convertStringToDouble(Unit.getConverter()::fromString);
  }

  @Test
  public void checkConvertStringToDoubleThrowsNpe() {
    assertThrows(NullPointerException.class, () -> Unit.convert((String) null));
  }

  @Test
  public void checkConverterConvertStringToDoubleThrowsNpe() {
    assertThrows(NullPointerException.class, () -> Unit.getConverter().fromString(null));
  }

  @Test
  public void checkRoundThrowsNpe() {
    for (var unit : Unit.values()) {
      assertThrows(NullPointerException.class, () -> unit.round(null));
    }
  }

  @Test
  public void testGetPositiveOrZero() {
    for (var unit : Unit.values()) {
      assertEquals(0D, unit.getPositiveOrZero(0D));
      assertEquals(12D, unit.getPositiveOrZero(12D));
      assertEquals(0D, unit.getPositiveOrZero(-0.001));
      assertEquals(0D, unit.getPositiveOrZero(-1D));
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Test
  public void checkGetPositiveOrZeroThrowsNpe() {
    for (var unit : Unit.values()) {
      assertThrows(NullPointerException.class, () -> unit.getPositiveOrZero(null));
    }
  }

  @Test
  public void testRoundPc() {
    var unit = Unit.PC;
    assertEquals(0D, unit.round(0D));
    assertEquals(0D, unit.round(0.99999999));
    assertEquals(12D, unit.round(12.256));
  }

  @Test
  public void testRoundKilo() {
    var unit = Unit.KILO;
    assertEquals(0D, unit.round(0D));
    assertEquals(0.789, unit.round(0.78999999));
    assertEquals(0.788, unit.round(0.788999999));
    assertEquals(12.256, unit.round(12.256));
    assertEquals(7.899, unit.round(7.8999));
    assertEquals(7.89, unit.round(7.89));
    assertEquals(7.8, unit.round(7.8));
  }

  @Test
  public void testRoundLiter() {
    var unit = Unit.LITER;
    assertEquals(0D, unit.round(0D));
    assertEquals(0.789, unit.round(0.78999999));
    assertEquals(0.788, unit.round(0.788999999));
    assertEquals(12.256, unit.round(12.256));
    assertEquals(7.899, unit.round(7.8999));
    assertEquals(7.89, unit.round(7.89));
    assertEquals(7.8, unit.round(7.8));
  }

  private void convertDoubleToString(Function<Double, String> function) {
    assertEquals("17.234", function.apply(17.234444));
    assertEquals("17.235", function.apply(17.234555));
    assertEquals("0.23", function.apply(0.23));
    assertEquals("0.239", function.apply(0.239));
    assertEquals("0.00", function.apply(0D));
    assertEquals("5.00", function.apply(5D));
    assertEquals("5.10", function.apply(5.1));
    assertEquals("5.90", function.apply(5.9));
  }

  private void convertStringToDouble(Function<String, Double> function) {
    assertEquals(12D, function.apply("12"));
    assertEquals(0D, function.apply("0"));
    assertEquals(7.456, function.apply("7.456"));
    assertEquals(7.456999999, function.apply("7.456999999"));
    assertEquals(0.999999, function.apply("0.999999"));
    assertEquals(0D, function.apply(""));
    assertEquals(0D, function.apply("text"));
  }
}