package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCategory;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCompany;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createDecommissionedCommodity;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProduct;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProductStorage;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createUser;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class DecommissionedCommodityTest {

  @Test
  public void testCopyConstructor() {
    var company = createCompany("company", "1234567898745632");
    var storage = createProductStorage("storage", company);
    var category = createCategory("category");
    var product = createProduct("product", category);
    var user = createUser("login");
    var commodity1 = createDecommissionedCommodity(product, storage, user);
    var commodity2 = new DecommissionedCommodity(commodity1);

    assertNotSame(commodity1.getProductStorage(), commodity2.getProductStorage());
    assertNotSame(commodity1.getProduct(), commodity2.getProduct());
    assertNotSame(commodity1.getUpdateTime(), commodity2.getUpdateTime());
    assertNotSame(commodity1.getDecommissioningTime(), commodity2.getDecommissioningTime());
    assertNotSame(commodity1.getUser(), commodity2.getUser());

    assertNotSame(commodity1, commodity2);
    assertEquals(commodity1, commodity2);
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(DecommissionedCommodity.class)
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
    var user = createUser("login");
    var commodity = DecommissionedCommodity.Builder.create()
        .setAmount(10)
        .setProduct(product)
        .setProductStorage(storage)
        .setPurchasePrice(69)
        .setRetailPrice(75)
        .setUpdateTime(LocalDateTime.now())
        .setDecommissioningTime(LocalDateTime.now())
        .setReason("Reason")
        .setUser(user)
        .build();

    assertEquals(-1, commodity.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var company = createCompany("company", "1234567898745636");
    var storage = createProductStorage("storage", company);
    var category = createCategory("category");
    var product = createProduct("product", category);
    var user = createUser("login");
    var commodity = DecommissionedCommodity.Builder.create();
    assertThrows(NullPointerException.class, commodity::build);

    commodity.setAmount(10)
        .setProduct(product)
        .setProductStorage(storage)
        .setPurchasePrice(69)
        .setRetailPrice(75)
        .setUpdateTime(LocalDateTime.now())
        .setDecommissioningTime(LocalDateTime.now())
        .setReason("Reason")
        .setUser(user)
        .build();
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
    commodity.setPurchasePrice(1);

    commodity.setDecommissioningTime(null);
    assertThrows(NullPointerException.class, commodity::build);
    commodity.setDecommissioningTime(LocalDateTime.now());

    commodity.setReason(null);
    assertThrows(NullPointerException.class, commodity::build);
    commodity.setReason("r");

    commodity.setUser(null);
    assertThrows(NullPointerException.class, commodity::build);
  }
}