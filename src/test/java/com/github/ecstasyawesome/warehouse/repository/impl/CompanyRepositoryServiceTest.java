package com.github.ecstasyawesome.warehouse.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockStatic;

import com.github.ecstasyawesome.warehouse.model.PersonType;
import com.github.ecstasyawesome.warehouse.model.impl.Address;
import com.github.ecstasyawesome.warehouse.model.impl.BusinessContact;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.repository.CompanyRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import com.github.ecstasyawesome.warehouse.util.TestDatabase;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class CompanyRepositoryServiceTest {

  private static final Random RANDOM = new Random();
  private final CompanyRepository companyRepository = CompanyRepositoryService.getInstance();
  private MockedStatic<DatabaseManager> mockedDatabase;

  @SuppressWarnings("SqlWithoutWhere")
  @BeforeEach
  public void setUp() throws SQLException {
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      statement.addBatch("DELETE FROM COMPANIES");
      statement.addBatch("DELETE FROM COMPANIES_ADDRESSES");
      statement.addBatch("DELETE FROM COMPANIES_CONTACTS");
      statement.addBatch("ALTER TABLE COMPANIES ALTER COLUMN COMPANY_ID RESTART WITH 1");
      statement.addBatch("ALTER TABLE COMPANIES_ADDRESSES ALTER COLUMN ADDRESS_ID RESTART WITH 1");
      statement.addBatch("ALTER TABLE COMPANIES_CONTACTS ALTER COLUMN CONTACT_ID RESTART WITH 1");
      statement.executeBatch();
    }
    mockedDatabase = mockStatic(DatabaseManager.class);
    mockedDatabase.when(DatabaseManager::getConnection)
        .then(answer -> TestDatabase.getConnection());
  }

  @AfterEach
  public void tearDown() {
    mockedDatabase.close();
  }

  @Test
  public void testGetAll() {
    var one = getTestEntry("Name1", "777777777777777");
    companyRepository.create(one);
    var two = getTestEntry("Name2", "888888888888888");
    companyRepository.create(two);
    var three = getTestEntry("Name3", "999999999999999");
    companyRepository.create(three);
    var expected = List.of(one, two, three);
    var actual = companyRepository.getAll();
    assertEquals(expected.size(), actual.size());
    IntStream.range(0, expected.size())
        .forEach(index -> assertEquals(expected.get(index), actual.get(index)));
  }

  @Test
  public void testGetAllEmptyList() {
    var actual = companyRepository.getAll();
    assertEquals(0, actual.size());
  }

  @Test
  public void testReadAndCreate() {
    var company = getTestEntry();
    companyRepository.create(company);
    assertEquals(1, company.getId());
    assertEquals(company, companyRepository.read(1));
  }

  @Test
  public void testReadAbsentEntry() {
    assertThrows(NullPointerException.class, () -> companyRepository.read(1));
  }

  @Test
  public void testCreateDuplicate() {
    var company = getTestEntry("Name1", "8888888888888888");
    var companyDuplicateName = getTestEntry("Name1", "481651946198");
    var companyDuplicateIdentifiedCode = getTestEntry("Name2", "8888888888888888");
    companyRepository.create(company);
    assertEquals(1, company.getId());
    assertEquals(company, companyRepository.read(1));
    assertThrows(NullPointerException.class, () -> companyRepository.create(companyDuplicateName));
    assertThrows(NullPointerException.class,
        () -> companyRepository.create(companyDuplicateIdentifiedCode));
  }

  @Test
  public void testUpdate() {
    var company = getTestEntry();
    companyRepository.create(company);
    company.setName("New name");
    company.setPersonType(PersonType.LEGAL_ENTITY);
    company.setIdentifierCode("8798352198494");
    companyRepository.update(company);
    assertEquals(company, companyRepository.read(company.getId()));
  }

  @Test
  public void testUpdateDuplicate() {
    var one = getTestEntry("Name1", "8888888888888888");
    companyRepository.create(one);
    var two = getTestEntry("Name2", "7777777777777777");
    companyRepository.create(two);
    two.setName(one.getName());
    assertThrows(NullPointerException.class, () -> companyRepository.update(two));
    two.setName("Name2");
    two.setIdentifierCode(one.getIdentifierCode());
    assertThrows(NullPointerException.class, () -> companyRepository.update(two));
  }

  @Test
  public void testDelete() {
    var company = getTestEntry();
    companyRepository.create(company);
    assertEquals(1, company.getId());
    companyRepository.delete(company);
    assertThrows(NullPointerException.class, () -> companyRepository.read(company.getId()));

    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      assertTrue(statement.execute("SELECT EXISTS(SELECT 1 FROM COMPANIES_CONTACTS)"));
      assertTrue(statement.execute("SELECT EXISTS(SELECT 1 FROM COMPANIES_ADDRESSES)"));
      try (var resultSet = statement.getResultSet()) {
        while (resultSet.next()) {
          assertFalse(resultSet.getBoolean(1));
        }
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void testDeleteAbsentEntry() {
    var company = getTestEntry();
    company.setId(7);
    assertThrows(NullPointerException.class, () -> companyRepository.delete(company));
  }

  private Company getTestEntry() {
    return getTestEntry("Name", String.valueOf(RANDOM.nextInt(Integer.MAX_VALUE)));
  }

  private Company getTestEntry(String name, String identifierCode) {
    var contact = BusinessContact.Builder.create()
        .setPhone("0955558877")
        .setExtraPhone("0958886633")
        .setEmail("example@mail.com")
        .setSite("example.com")
        .build();
    var address = Address.Builder.create()
        .setRegion("Region")
        .setTown("Town")
        .setStreet("Street")
        .setNumber("10/7")
        .build();
    return Company.Builder.create()
        .setName(name)
        .setIdentifierCode(identifierCode)
        .setPersonType(PersonType.INDIVIDUAL)
        .setBusinessContact(contact)
        .setAddress(address)
        .build();
  }
}