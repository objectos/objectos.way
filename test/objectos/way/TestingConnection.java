/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

final class TestingConnection extends AbstractTestable implements Connection {

  private Iterator<PreparedStatement> preparedStatements = Collections.emptyIterator();

  private SQLException rollbackException;

  private Iterator<Statement> statements = Collections.emptyIterator();

  public final void preparedStatements(PreparedStatement... stmts) {
    preparedStatements = Arrays.asList(stmts).iterator();
  }

  public final void rollbackException(SQLException error) {
    rollbackException = error;
  }

  public final void statements(Statement... stmts) {
    statements = Arrays.asList(stmts).iterator();
  }

  //

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public Statement createStatement() throws SQLException {
    logMethod("createStatement");

    if (!statements.hasNext()) {
      throw new IllegalStateException("No more statements");
    }

    return statements.next();
  }

  @Override
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    logMethod("prepareStatement", sql);

    if (!preparedStatements.hasNext()) {
      throw new IllegalStateException("No more statements");
    }

    return preparedStatements.next();
  }

  @Override
  public CallableStatement prepareCall(String sql) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String nativeSQL(String sql) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean getAutoCommit() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void commit() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void rollback() throws SQLException {
    logMethod("rollback");

    if (rollbackException != null) {
      throw rollbackException;
    }
  }

  @Override
  public void close() throws SQLException {
    logMethod("close");
  }

  @Override
  public boolean isClosed() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public DatabaseMetaData getMetaData() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setReadOnly(boolean readOnly) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isReadOnly() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setCatalog(String catalog) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getCatalog() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setTransactionIsolation(int level) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getTransactionIsolation() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public SQLWarning getWarnings() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void clearWarnings() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public Map<String, Class<?>> getTypeMap() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setTypeMap(Map<String, Class<?>> map) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setHoldability(int holdability) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getHoldability() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public Savepoint setSavepoint() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public Savepoint setSavepoint(String name) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void rollback(Savepoint savepoint) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public Clob createClob() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public Blob createBlob() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public NClob createNClob() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public SQLXML createSQLXML() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isValid(int timeout) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setClientInfo(String name, String value) throws SQLClientInfoException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setClientInfo(Properties properties) throws SQLClientInfoException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getClientInfo(String name) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public Properties getClientInfo() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public Array createArrayOf(String typeName, Object[] elements) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public Struct createStruct(String typeName, Object[] attributes) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setSchema(String schema) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getSchema() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void abort(Executor executor) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getNetworkTimeout() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

}
