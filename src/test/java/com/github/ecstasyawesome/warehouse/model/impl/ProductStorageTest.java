package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCompany;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProductStorage;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class ProductStorageTest {

  @Test
  public void testCopyConstructor() {
    var company = createCompany("company", "1234567898745632");
    var storage1 = createProductStorage("storage", company);
    var storage2 = new ProductStorage(storage1);

    assertNotSame(storage1.getAddress(), storage2.getAddress());
    assertNotSame(storage1.getBusinessContact(), storage2.getBusinessContact());
    assertNotSame(storage1.getCompany(), storage2.getCompany());

    assertNotSame(storage1, storage2);
    assertEquals(storage1, storage2);
  }

  @Test
  public void testRecover() {
    var company1 = createCompany("company", "1234567898745632");
    var storage1 = createProductStorage("storage", company1);
    var company2 = createCompany("company", "1234567898745632");
    var storage2 = ProductStorage.Builder.create()
        .setId(7)
        .setName("n")
        .setAddress(getAddress())
        .setBusinessContact(getBusinessContact())
        .setCompany(company2)
        .build();

    assertNotEquals(storage1, storage2);
    storage2.recover(storage1);
    assertEquals(storage1, storage2);

    assertNotSame(storage1.getBusinessContact(), storage2.getBusinessContact());
    assertNotSame(storage1.getAddress(), storage2.getAddress());
    assertNotSame(storage1.getCompany(), storage2.getCompany());
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(ProductStorage.class)
        .withRedefinedSuperclass()
        .usingGetClass()
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var company = createCompany("company", "1234567898745632");
    var storage = ProductStorage.Builder.create()
        .setName("n")
        .setAddress(getAddress())
        .setBusinessContact(getBusinessContact())
        .setCompany(company)
        .build();
    assertEquals(-1, storage.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var company = createCompany("company", "1234567898745632");
    var storage = ProductStorage.Builder.create();
    assertThrows(NullPointerException.class, storage::build);
    storage.setId(7)
        .setName("n")
        .setAddress(getAddress())
        .setBusinessContact(getBusinessContact())
        .setCompany(company)
        .build();
    assertDoesNotThrow(storage::build);

    storage.setName(null);
    assertThrows(NullPointerException.class, storage::build);
    storage.setName("n");

    storage.setAddress(null);
    assertThrows(NullPointerException.class, storage::build);
    storage.setAddress(getAddress());

    storage.setBusinessContact(null);
    assertThrows(NullPointerException.class, storage::build);
    storage.setBusinessContact(getBusinessContact());

    storage.setCompany(null);
    assertThrows(NullPointerException.class, storage::build);
  }

  private Address getAddress() {
    return Address.Builder.create()
        .setRegion("r")
        .setTown("t")
        .setStreet("s")
        .setNumber("n")
        .build();
  }

  private BusinessContact getBusinessContact() {
    return BusinessContact.Builder.create()
        .setPhone("p")
        .setExtraPhone("e")
        .setEmail("e")
        .setSite("s")
        .build();
  }
}