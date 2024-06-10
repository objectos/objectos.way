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
package objectos.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import objectos.lang.object.Check;

final class WaySqlTransaction implements SqlTransaction {

  private final Dialect dialect;

  private final Connection connection;

  WaySqlTransaction(Dialect dialect, Connection connection) {
    this.dialect = dialect;

    this.connection = connection;
  }

  @Override
  public final void commit() throws UncheckedSqlException {
    try {
      connection.commit();
    } catch (SQLException e) {
      throw new UncheckedSqlException(e);
    }
  }

  @Override
  public final void rollback() throws UncheckedSqlException {
    try {
      connection.rollback();
    } catch (SQLException e) {
      throw new UncheckedSqlException(e);
    }
  }

  @Override
  public final void close() throws UncheckedSqlException {
    try {
      connection.close();
    } catch (SQLException e) {
      throw new UncheckedSqlException(e);
    }
  }

  @Override
  public final int[] batchUpdate(String sql, Object[]... batches) throws UncheckedSqlException {
    Check.notNull(sql, "sql == null");
    Check.notNull(batches, "batches == null");

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {

      for (Object[] batch : batches) {
        for (int index = 0; index < batch.length;) {
          Object value;
          value = batch[index++];

          WaySql.set(stmt, index, value);
        }
        
        stmt.addBatch();
      }

      return stmt.executeBatch();
      
    } catch (SQLException e) {
      throw new UncheckedSqlException(e);
    }
  }

  @Override
  public final int count(String sql, Object... args) throws UncheckedSqlException {
    Check.notNull(sql, "sql == null");
    Check.notNull(args, "args == null");

    SqlTemplate template;
    template = SqlTemplate.parse(sql, args);

    return template.count(dialect, connection);
  }

  @Override
  public final void queryPage(String sql, ResultSetHandler handler, Page page, Object... args) throws UncheckedSqlException {
    Check.notNull(sql, "sql == null");
    Check.notNull(handler, "handler == null");
    Check.notNull(page, "page == null");
    Check.notNull(args, "args == null");

    SqlTemplate template;
    template = SqlTemplate.parse(sql, args);

    template.paginate(dialect, page);

    template.query(connection, handler);
  }

}