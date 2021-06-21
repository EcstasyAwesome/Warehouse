package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCompany;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ecstasyawesome.warehouse.model.PersonType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class CompanyTest {

  @Test
  public void testCopyConstructor() {
    var company1 = createCompany("name", "123456789");
    var company2 = new Company(company1);

    assertNotSame(company1.getAddress(), company2.getAddress());
    assertNotSame(company1.getBusinessContact(), company2.getBusinessContact());

    assertNotSame(company1, company2);
    assertEquals(company1, company2);
  }

  @Test
  public void testRecover() {
    var company1 = createCompany("name", "123456789");
    var company2 = Company.Builder.create()
        .setId(7)
        .setName("n")
        .setIdentifierCode("987654321")
        .setPersonType(PersonType.LEGAL_ENTITY)
        .setAddress(getAddress())
        .setBusinessContact(getBusinessContact())
        .build();

    assertNotEquals(company1, company2);
    company2.recover(company1);
    assertEquals(company1, company2);

    assertNotSame(company1.getBusinessContact(), company2.getBusinessContact());
    assertNotSame(company1.getAddress(), company2.getAddress());
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(Company.class)
        .withRedefinedSuperclass()
        .usingGetClass()
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var company = Company.Builder.create()
        .setName("n")
        .setIdentifierCode("987654321")
        .setPersonType(PersonType.LEGAL_ENTITY)
        .setAddress(getAddress())
        .setBusinessContact(getBusinessContact())
        .build();
    assertEquals(-1, company.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var company = Company.Builder.create();
    assertThrows(NullPointerException.class, company::build);
    company.setId(7)
        .setName("n")
        .setIdentifierCode("987654321")
        .setPersonType(PersonType.LEGAL_ENTITY)
        .setAddress(getAddress())
        .setBusinessContact(getBusinessContact())
        .build();
    assertDoesNotThrow(company::build);

    company.setName(null);
    assertThrows(NullPointerException.class, company::build);
    company.setName("n");

    company.setIdentifierCode(null);
    assertThrows(NullPointerException.class, company::build);
    company.setIdentifierCode("7879846");

    company.setPersonType(null);
    assertThrows(NullPointerException.class, company::build);
    company.setPersonType(PersonType.LEGAL_ENTITY);

    company.setAddress(null);
    assertThrows(NullPointerException.class, company::build);
    company.setAddress(getAddress());

    company.setBusinessContact(null);
    assertThrows(NullPointerException.class, company::build);
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