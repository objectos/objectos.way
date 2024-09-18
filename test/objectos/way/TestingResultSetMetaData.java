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

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class TestingResultSetMetaData extends AbstractTestable implements ResultSetMetaData {

  private int columnCount;

  public final void columnCount(int value) {
    columnCount = value;
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public final int getColumnCount() throws SQLException {
    logMethod("columnCount");

    return columnCount;
  }

  @Override
  public boolean isAutoIncrement(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isCaseSensitive(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isSearchable(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isCurrency(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int isNullable(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isSigned(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getColumnDisplaySize(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getColumnLabel(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getColumnName(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getSchemaName(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getPrecision(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getScale(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getTableName(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getCatalogName(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public int getColumnType(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getColumnTypeName(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isReadOnly(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isWritable(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public boolean isDefinitelyWritable(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String getColumnClassName(int column) throws SQLException { throw new UnsupportedOperationException("Implement me"); }

}