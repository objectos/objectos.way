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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

final class TestingStatement extends AbstractTestable implements Statement {

  private Iterator<int[]> batches = Collections.emptyIterator();

  public final void batches(int[]... values) {
    batches = Stream.of(values).iterator();
  }

  @Override
  public ResultSet executeQuery(String sql) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int executeUpdate(String sql) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public void close() throws SQLException {
    logMethod("close");
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
  public void addBatch(String sql) throws SQLException {
    logMethod("addBatch", sql);
  }

  @Override
  public void clearBatch() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int[] executeBatch() throws SQLException {
    logMethod("executeBatch");

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
  public ResultSet getGeneratedKeys() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

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
  public boolean isClosed() throws SQLException { throw new UnsupportedOperationException("Implement me"); }

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

}