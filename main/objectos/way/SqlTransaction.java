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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import objectos.lang.object.Check;

final class SqlTransaction implements Sql.Transaction {

  private final SqlDialect dialect;

  private final Connection connection;

  SqlTransaction(SqlDialect dialect, Connection connection) {
    this.dialect = dialect;

    this.connection = connection;
  }

  @Override
  public final void commit() throws Sql.UncheckedSqlException {
    try {
      connection.commit();
    } catch (SQLException e) {
      throw new Sql.UncheckedSqlException(e);
    }
  }

  @Override
  public final void rollback() throws Sql.UncheckedSqlException {
    try {
      connection.rollback();
    } catch (SQLException e) {
      throw new Sql.UncheckedSqlException(e);
    }
  }

  @Override
  public final void close() throws Sql.UncheckedSqlException {
    try {
      connection.close();
    } catch (SQLException e) {
      throw new Sql.UncheckedSqlException(e);
    }
  }

  @Override
  public final int[] batchUpdate(String sql, Object[]... batches) throws Sql.UncheckedSqlException {
    Check.notNull(sql, "sql == null");
    Check.notNull(batches, "batches == null");

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {

      for (Object[] batch : batches) {
        for (int index = 0; index < batch.length;) {
          Object value;
          value = batch[index++];

          Sql.set(stmt, index, value);
        }

        stmt.addBatch();
      }

      return stmt.executeBatch();

    } catch (SQLException e) {
      throw new Sql.UncheckedSqlException(e);
    }
  }

  @Override
  public final int count(String sql, Object... args) throws Sql.UncheckedSqlException {
    Check.notNull(sql, "sql == null");
    Check.notNull(args, "args == null");

    SqlTemplate template;
    template = SqlTemplate.parse(sql, args);

    return template.count(dialect, connection);
  }

  @Override
  public final void queryPage(String sql, Sql.ResultSetHandler handler, Sql.Page page, Object... args) throws Sql.UncheckedSqlException {
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