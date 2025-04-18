/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.way;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

final class TestingDatabaseMetaData implements DatabaseMetaData {

  public static final TestingDatabaseMetaData H2 = new TestingDatabaseMetaData(
      "H2", 2, 2, "2.2.224 (2023-09-17)"
  );

  public static final TestingDatabaseMetaData MYSQL_5_7 = new TestingDatabaseMetaData(
      "MySQL", 5, 7, "5.7.44-log"
  );

  public static final TestingDatabaseMetaData TESTING = new TestingDatabaseMetaData(
      "ObjectosWay", 0, 0, "0"
  );

  private final String databaseProductName;

  private final int databaseMajorVersion;

  private final int databaseMinorVersion;

  private final String databaseProductVersion;

  public TestingDatabaseMetaData(String databaseProductName, int databaseMajorVersion, int databaseMinorVersion, String databaseProductVersion) {
    this.databaseProductName = databaseProductName;
    this.databaseMajorVersion = databaseMajorVersion;
    this.databaseMinorVersion = databaseMinorVersion;
    this.databaseProductVersion = databaseProductVersion;
  }

  public final SqlDialect toSqlDialect() {
    try {
      return SqlDialect.of(this);
    } catch (SQLException e) {
      throw new Sql.DatabaseException(e);
    }
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean allProceduresAreCallable() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean allTablesAreSelectable() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getURL() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getUserName() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isReadOnly() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean nullsAreSortedHigh() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean nullsAreSortedLow() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean nullsAreSortedAtStart() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean nullsAreSortedAtEnd() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getDatabaseProductName() throws SQLException {
    return databaseProductName;
  }

  @Override
  public String getDatabaseProductVersion() throws SQLException {
    return databaseProductVersion;
  }

  @Override
  public String getDriverName() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getDriverVersion() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getDriverMajorVersion() { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getDriverMinorVersion() { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean usesLocalFiles() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean usesLocalFilePerTable() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsMixedCaseIdentifiers() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean storesUpperCaseIdentifiers() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean storesLowerCaseIdentifiers() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean storesMixedCaseIdentifiers() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean storesUpperCaseQuotedIdentifiers() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean storesLowerCaseQuotedIdentifiers() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean storesMixedCaseQuotedIdentifiers() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getIdentifierQuoteString() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getSQLKeywords() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getNumericFunctions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getStringFunctions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getSystemFunctions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getTimeDateFunctions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getSearchStringEscape() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getExtraNameCharacters() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsAlterTableWithAddColumn() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsAlterTableWithDropColumn() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsColumnAliasing() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean nullPlusNonNullIsNull() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsConvert() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsConvert(int fromType, int toType) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsTableCorrelationNames() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsDifferentTableCorrelationNames() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsExpressionsInOrderBy() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsOrderByUnrelated() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsGroupBy() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsGroupByUnrelated() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsGroupByBeyondSelect() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsLikeEscapeClause() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsMultipleResultSets() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsMultipleTransactions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsNonNullableColumns() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsMinimumSQLGrammar() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsCoreSQLGrammar() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsExtendedSQLGrammar() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsANSI92EntryLevelSQL() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsANSI92IntermediateSQL() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsANSI92FullSQL() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsIntegrityEnhancementFacility() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsOuterJoins() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsFullOuterJoins() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsLimitedOuterJoins() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getSchemaTerm() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getProcedureTerm() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getCatalogTerm() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isCatalogAtStart() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getCatalogSeparator() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsSchemasInDataManipulation() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsSchemasInProcedureCalls() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsSchemasInTableDefinitions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsSchemasInIndexDefinitions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsCatalogsInDataManipulation() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsCatalogsInProcedureCalls() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsCatalogsInTableDefinitions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsCatalogsInIndexDefinitions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsPositionedDelete() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsPositionedUpdate() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsSelectForUpdate() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsStoredProcedures() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsSubqueriesInComparisons() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsSubqueriesInExists() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsSubqueriesInIns() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsSubqueriesInQuantifieds() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsCorrelatedSubqueries() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsUnion() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsUnionAll() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsOpenCursorsAcrossCommit() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsOpenCursorsAcrossRollback() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsOpenStatementsAcrossCommit() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsOpenStatementsAcrossRollback() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxBinaryLiteralLength() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxCharLiteralLength() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxColumnNameLength() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxColumnsInGroupBy() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxColumnsInIndex() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxColumnsInOrderBy() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxColumnsInSelect() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxColumnsInTable() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxConnections() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxCursorNameLength() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxIndexLength() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxSchemaNameLength() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxProcedureNameLength() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxCatalogNameLength() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxRowSize() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean doesMaxRowSizeIncludeBlobs() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxStatementLength() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxStatements() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxTableNameLength() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxTablesInSelect() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxUserNameLength() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getDefaultTransactionIsolation() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsTransactions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsTransactionIsolationLevel(int level) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsDataManipulationTransactionsOnly() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean dataDefinitionCausesTransactionCommit() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean dataDefinitionIgnoredInTransactions() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getSchemas() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getCatalogs() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getTableTypes() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public ResultSet getTypeInfo() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsResultSetType(int type) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean ownUpdatesAreVisible(int type) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean ownDeletesAreVisible(int type) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean ownInsertsAreVisible(int type) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean othersUpdatesAreVisible(int type) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean othersDeletesAreVisible(int type) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean othersInsertsAreVisible(int type) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean updatesAreDetected(int type) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean deletesAreDetected(int type) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean insertsAreDetected(int type) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsBatchUpdates() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public Connection getConnection() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsSavepoints() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsNamedParameters() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsMultipleOpenResults() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsGetGeneratedKeys() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public boolean supportsResultSetHoldability(int holdability) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getResultSetHoldability() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getDatabaseMajorVersion() throws SQLException {
    return databaseMajorVersion;
  }

  @Override
  public int getDatabaseMinorVersion() throws SQLException {
    return databaseMinorVersion;
  }

  @Override
  public int getJDBCMajorVersion() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getJDBCMinorVersion() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getSQLStateType() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean locatorsUpdateCopy() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsStatementPooling() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public RowIdLifetime getRowIdLifetime() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean autoCommitFailureClosesAllResultSets() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getClientInfoProperties() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public boolean generatedKeyAlwaysReturned() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

}