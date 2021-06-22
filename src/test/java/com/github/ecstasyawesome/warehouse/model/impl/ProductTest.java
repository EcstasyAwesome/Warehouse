package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCategory;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProduct;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ecstasyawesome.warehouse.model.Unit;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class ProductTest {

  @Test
  public void testCopyConstructor() {
    var category = createCategory("category");
    var product1 = createProduct("product", category);
    var product2 = new Product(product1);

    assertNotSame(product1.getCategory(), product2.getCategory());

    assertNotSame(product1, product2);
    assertEquals(product1, product2);
  }

  @Test
  public void testRecover() {
    var category1 = createCategory("category1");
    var product1 = createProduct("product", category1);

    var category2 = createCategory("category2");
    var product2 = Product.Builder.create()
        .setId(7)
        .setName("n")
        .setCategory(category2)
        .setUnit(Unit.PC)
        .build();

    assertNotEquals(product1, product2);
    product2.recover(product1);
    assertEquals(product1, product2);

    assertNotSame(product1.getCategory(), product2.getCategory());
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(Product.class)
        .withRedefinedSuperclass()
        .usingGetClass()
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var category = createCategory("category");
    var product = Product.Builder.create()
        .setName("n")
        .setCategory(category)
        .setUnit(Unit.PC)
        .build();
    assertEquals(-1, product.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var category = createCategory("category");
    var product = Product.Builder.create();
    assertThrows(NullPointerException.class, product::build);
    product.setName("n")
        .setCategory(category)
        .setUnit(Unit.PC)
        .build();
    assertDoesNotThrow(product::build);

    product.setName(null);
    assertThrows(NullPointerException.class, product::build);
    product.setName("n");

    product.setCategory(null);
    assertThrows(NullPointerException.class, product::build);
    product.setCategory(category);

    product.setUnit(null);
    assertThrows(NullPointerException.class, product::build);
  }
}