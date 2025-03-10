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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.BatchUpdateException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

final class TestingPreparedStatement extends AbstractTestable implements PreparedStatement {

  private Iterator<int[]> batches = Collections.emptyIterator();

  private boolean closed;

  private BatchUpdateException batchUpdateError;

  private Iterator<ResultSet> generatedKeys = Collections.emptyIterator();

  private Iterator<ResultSet> queries = Collections.emptyIterator();

  private SQLException updateError;

  private Iterator<Integer> updates = Collections.emptyIterator();

  public final void batches(int[]... values) {
    batches = Stream.of(values).iterator();
  }

  public final void batchUpdateError(BatchUpdateException error) {
    batchUpdateError = error;
  }

  public final void generatedKeys(ResultSet... values) {
    generatedKeys = Stream.of(values).iterator();
  }

  public final void queries(ResultSet... values) {
    queries = Stream.of(values).iterator();
  }

  public final void updateError(SQLException error) {
    updateError = error;
  }

  public final void updates(int... values) {
    updates = Arrays.stream(values).boxed().iterator();
  }

  @Override
  public ResultSet executeQuery(String sql) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int executeUpdate(String sql) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void close() throws SQLException {
    logMethod("close");

    closed = true;
  }

  @Override
  public int getMaxFieldSize() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setMaxFieldSize(int max) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getMaxRows() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setMaxRows(int max) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setEscapeProcessing(boolean enable) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getQueryTimeout() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setQueryTimeout(int seconds) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void cancel() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public SQLWarning getWarnings() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void clearWarnings() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setCursorName(String name) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean execute(String sql) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet getResultSet() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getUpdateCount() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean getMoreResults() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setFetchDirection(int direction) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getFetchDirection() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setFetchSize(int rows) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getFetchSize() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getResultSetConcurrency() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getResultSetType() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void addBatch(String sql) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void clearBatch() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int[] executeBatch() throws SQLException {
    logMethod("executeBatch");

    if (batchUpdateError != null) {
      throw batchUpdateError;
    }

    if (!batches.hasNext()) {
      throw new IllegalStateException("No more batches");
    }

    return batches.next();
  }

  @Override
  public Connection getConnection() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean getMoreResults(int current) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public final ResultSet getGeneratedKeys() throws SQLException {
    logMethod("getGeneratedKeys");

    if (!generatedKeys.hasNext()) {
      throw new IllegalStateException("No more generated keys");
    }

    return generatedKeys.next();
  }

  @Override
  public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int executeUpdate(String sql, int[] columnIndexes) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int executeUpdate(String sql, String[] columnNames) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean execute(String sql, int autoGeneratedKeys) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean execute(String sql, int[] columnIndexes) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean execute(String sql, String[] columnNames) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getResultSetHoldability() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isClosed() throws SQLException { return closed; }

  @Override
  public void setPoolable(boolean poolable) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isPoolable() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void closeOnCompletion() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isCloseOnCompletion() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSet executeQuery() throws SQLException {
    logMethod("executeQuery");

    if (!queries.hasNext()) {
      throw new IllegalStateException("No more queries");
    }

    return queries.next();
  }

  @Override
  public final int executeUpdate() throws SQLException {
    logMethod("executeUpdate");

    if (updateError != null) {
      throw updateError;
    }

    if (!updates.hasNext()) {
      throw new IllegalStateException("No more updates");
    }

    return updates.next();
  }

  @Override
  public final void setNull(int parameterIndex, int sqlType) throws SQLException {
    logMethod("setNull", parameterIndex, sqlType);
  }

  @Override
  public void setBoolean(int parameterIndex, boolean x) throws SQLException {
    logMethod("setBoolean", parameterIndex, x);
  }

  @Override
  public void setByte(int parameterIndex, byte x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setShort(int parameterIndex, short x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public final void setInt(int parameterIndex, int x) throws SQLException {
    logMethod("setInt", parameterIndex, x);
  }

  @Override
  public final void setLong(int parameterIndex, long x) throws SQLException {
    logMethod("setLong", parameterIndex, x);
  }

  @Override
  public void setFloat(int parameterIndex, float x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public final void setDouble(int parameterIndex, double x) throws SQLException {
    logMethod("setDouble", parameterIndex, x);
  }

  @Override
  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public final void setString(int parameterIndex, String x) throws SQLException {
    logMethod("setString", parameterIndex, x);
  }

  @Override
  public void setBytes(int parameterIndex, byte[] x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setDate(int parameterIndex, Date x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setTime(int parameterIndex, Time x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void clearParameters() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public final void setObject(int parameterIndex, Object x) throws SQLException {
    logMethod("setObject", parameterIndex, x);
  }

  @Override
  public boolean execute() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public final void addBatch() throws SQLException {
    logMethod("addBatch");
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setRef(int parameterIndex, Ref x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setBlob(int parameterIndex, Blob x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setClob(int parameterIndex, Clob x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setArray(int parameterIndex, Array x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setURL(int parameterIndex, URL x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public ParameterMetaData getParameterMetaData() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setRowId(int parameterIndex, RowId x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setNString(int parameterIndex, String value) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setNClob(int parameterIndex, NClob value) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setClob(int parameterIndex, Reader reader, long length) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setClob(int parameterIndex, Reader reader) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void setNClob(int parameterIndex, Reader reader) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

}