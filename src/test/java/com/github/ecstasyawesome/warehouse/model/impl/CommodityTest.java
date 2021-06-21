package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCategory;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCommodity;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCompany;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProduct;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProductStorage;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class CommodityTest {

  @Test
  public void testCopyConstructor() {
    var company = createCompany("company", "1234567898745632");
    var storage = createProductStorage("storage", company);
    var category = createCategory("category");
    var product = createProduct("product", category);
    var commodity1 = createCommodity(product, storage);
    var commodity2 = new Commodity(commodity1);

    assertNotSame(commodity1.getProductStorage(), commodity2.getProductStorage());
    assertNotSame(commodity1.getProduct(), commodity2.getProduct());
    assertNotSame(commodity1.getUpdateTime(), commodity2.getUpdateTime());

    assertNotSame(commodity1, commodity2);
    assertEquals(commodity1, commodity2);
  }

  @Test
  public void testRecover() {
    var company1 = createCompany("company1", "1234567898745637");
    var storage1 = createProductStorage("storage1", company1);
    var category1 = createCategory("category1");
    var product1 = createProduct("product1", category1);
    var commodity1 = createCommodity(product1, storage1);

    var company2 = createCompany("company2", "1234567898745636");
    var storage2 = createProductStorage("storage2", company2);
    var category2 = createCategory("category2");
    var product2 = createProduct("product2", category2);
    var commodity2 = Commodity.Builder.create()
        .setId(2)
        .setUpdateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
        .setAmount(19)
        .setPurchasePrice(12)
        .setRetailPrice(15)
        .setProductStorage(storage2)
        .setProduct(product2)
        .build();

    assertNotEquals(commodity1, commodity2);
    commodity2.recover(commodity1);
    assertEquals(commodity1, commodity2);

    assertNotSame(commodity1.getProductStorage(), commodity2.getProductStorage());
    assertNotSame(commodity1.getProduct(), commodity2.getProduct());
    assertSame(commodity1.getUpdateTime(), commodity2.getUpdateTime());
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(Commodity.class)
        .withRedefinedSuperclass()
        .usingGetClass()
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var company = createCompany("company", "1234567898745636");
    var storage = createProductStorage("storage", company);
    var category = createCategory("category");
    var product = createProduct("product", category);
    var commodity = Commodity.Builder.create()
        .setUpdateTime(LocalDateTime.now())
        .setAmount(19)
        .setPurchasePrice(12)
        .setRetailPrice(15)
        .setProductStorage(storage)
        .setProduct(product)
        .build();

    assertEquals(-1, commodity.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var company = createCompany("company", "1234567898745636");
    var storage = createProductStorage("storage", company);
    var category = createCategory("category");
    var product = createProduct("product", category);
    var commodity = Commodity.Builder.create();
    assertThrows(NullPointerException.class, commodity::build);

    commodity.setUpdateTime(LocalDateTime.now())
        .setAmount(19)
        .setPurchasePrice(12)
        .setRetailPrice(15)
        .setProductStorage(storage)
        .setProduct(product);
    assertDoesNotThrow(commodity::build);

    commodity.setProduct(null);
    assertThrows(NullPointerException.class, commodity::build);
    commodity.setProduct(product);

    commodity.setProductStorage(null);
    assertThrows(NullPointerException.class, commodity::build);
    commodity.setProductStorage(storage);

    commodity.setUpdateTime(null);
    assertThrows(NullPointerException.class, commodity::build);
    commodity.setUpdateTime(LocalDateTime.now());

    commodity.setAmount(0);
    assertThrows(NullPointerException.class, commodity::build);
    commodity.setAmount(-1);
    assertThrows(NullPointerException.class, commodity::build);
    commodity.setAmount(1);

    commodity.setRetailPrice(0);
    assertThrows(NullPointerException.class, commodity::build);
    commodity.setRetailPrice(-1);
    assertThrows(NullPointerException.class, commodity::build);
    commodity.setRetailPrice(1);

    commodity.setPurchasePrice(0);
    assertThrows(NullPointerException.class, commodity::build);
    commodity.setPurchasePrice(-1);
    assertThrows(NullPointerException.class, commodity::build);
  }
}