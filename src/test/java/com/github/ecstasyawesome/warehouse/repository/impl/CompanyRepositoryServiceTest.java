package com.github.ecstasyawesome.warehouse.repository.impl;

import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createCompany;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockStatic;

import com.github.ecstasyawesome.warehouse.model.PersonType;
import com.github.ecstasyawesome.warehouse.repository.CompanyRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import com.github.ecstasyawesome.warehouse.util.TestDatabase;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class CompanyRepositoryServiceTest {

  private final CompanyRepository companyRepository = CompanyRepositoryService.getInstance();
  private MockedStatic<DatabaseManager> mockedDatabase;


  @BeforeEach
  public void setUp() throws SQLException {
    clearDatabase();
    mockedDatabase = mockStatic(DatabaseManager.class);
    mockedDatabase.when(DatabaseManager::getConnection)
        .then(answer -> TestDatabase.getConnection());
  }

  @SuppressWarnings("SqlWithoutWhere")
  private void clearDatabase() throws SQLException {
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
  }

  @AfterEach
  public void tearDown() {
    mockedDatabase.close();
  }

  @Test
  public void testGetAll() {
    var company1 = createCompany("Name1", "777777777777777");
    companyRepository.create(company1);
    var company2 = createCompany("Name2", "888888888888888");
    companyRepository.create(company2);
    var company3 = createCompany("Name3", "999999999999999");
    companyRepository.create(company3);
    var expected = List.of(company1, company2, company3);
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
    var company = createCompany("Name1", "777777777777777");
    companyRepository.create(company);
    assertEquals(1, company.getId());
    assertEquals(1, company.getAddress().getId());
    assertEquals(1, company.getBusinessContact().getId());
    assertEquals(company, companyRepository.read(company.getId()));
  }

  @Test
  public void testReadAbsentEntry() {
    assertThrows(NullPointerException.class, () -> companyRepository.read(1));
  }

  @Test
  public void testCreateDuplicate() {
    var company = createCompany("Name1", "8888888888888888");
    var companyDuplicateName = createCompany("Name1", "481651946198");
    var companyDuplicateIdentifiedCode = createCompany("Name2", "8888888888888888");
    companyRepository.create(company);
    assertEquals(1, company.getId());
    assertEquals(company, companyRepository.read(company.getId()));
    assertThrows(NullPointerException.class, () -> companyRepository.create(companyDuplicateName));
    assertThrows(NullPointerException.class,
        () -> companyRepository.create(companyDuplicateIdentifiedCode));
  }

  @Test
  public void testUpdate() {
    var company = createCompany("Name1", "777777777777777");
    companyRepository.create(company);
    company.setName("New name");
    company.setPersonType(PersonType.LEGAL_ENTITY);
    company.setIdentifierCode("8798352198494");
    companyRepository.update(company);
    assertEquals(company, companyRepository.read(company.getId()));
  }

  @Test
  public void testUpdateDuplicate() {
    var company1 = createCompany("Name1", "8888888888888888");
    companyRepository.create(company1);
    var company2 = createCompany("Name2", "7777777777777777");
    companyRepository.create(company2);
    company2.setName(company1.getName());
    assertThrows(NullPointerException.class, () -> companyRepository.update(company2));
    company2.setName("Name2");
    company2.setIdentifierCode(company1.getIdentifierCode());
    assertThrows(NullPointerException.class, () -> companyRepository.update(company2));
  }

  @Test
  public void testDelete() {
    var company = createCompany("Name1", "777777777777777");
    companyRepository.create(company);
    assertEquals(1, company.getId());
    companyRepository.delete(company);
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      assertTrue(statement.execute("SELECT EXISTS(SELECT 1 FROM COMPANIES_ADDRESSES)"));
      assertTrue(statement.execute("SELECT EXISTS(SELECT 1 FROM COMPANIES_CONTACTS)"));
      try (var resultSet = statement.getResultSet()) {
        while (resultSet.next()) {
          assertFalse(resultSet.getBoolean(1));
        }
      }
    } catch (SQLException exception) {
      fail(exception);
    }
    assertThrows(NullPointerException.class, () -> companyRepository.read(company.getId()));
  }

  @Test
  public void testDeleteAbsentEntry() {
    var company = createCompany("Name1", "777777777777777");
    company.setId(7);
    assertThrows(NullPointerException.class, () -> companyRepository.delete(company));
  }
}