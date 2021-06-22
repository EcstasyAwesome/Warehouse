package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCategory;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProduct;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createReturnInvoiceItem;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class ReturnInvoiceItemTest {

  @Test
  public void testCopyConstructor() {
    var category = createCategory("category");
    var product = createProduct("product", category);

    var item1 = createReturnInvoiceItem(product);
    var item2 = new ReturnInvoiceItem(item1);

    assertNotSame(item1.getProduct(), item2.getProduct());

    assertNotSame(item1, item2);
    assertEquals(item1, item2);
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(ReturnInvoiceItem.class)
        .withRedefinedSuperclass()
        .usingGetClass()
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var category = createCategory("category");
    var product = createProduct("product", category);

    var item = ReturnInvoiceItem.Builder.create()
        .setPrice(13.6)
        .setProduct(product)
        .setAmount(10)
        .build();
    assertEquals(-1, item.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var category = createCategory("category");
    var product = createProduct("product", category);

    var item = ReturnInvoiceItem.Builder.create();
    assertThrows(NullPointerException.class, item::build);

    item.setPrice(10.5)
        .setProduct(product)
        .setAmount(10)
        .build();
    assertDoesNotThrow(item::build);

    item.setProduct(null);
    assertThrows(NullPointerException.class, item::build);
    item.setProduct(product);

    item.setAmount(-1);
    assertThrows(NullPointerException.class, item::build);
    item.setAmount(0);
    assertThrows(NullPointerException.class, item::build);
    item.setAmount(1);

    item.setPrice(-1);
    assertThrows(NullPointerException.class, item::build);
    item.setPrice(0);
    assertThrows(NullPointerException.class, item::build);
  }
}