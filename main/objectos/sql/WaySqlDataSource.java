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
import java.sql.SQLException;
import javax.sql.DataSource;

final class WaySqlDataSource implements SqlDataSource {

  private final DataSource dataSource;

  private final Dialect dialect;

  public WaySqlDataSource(DataSource dataSource, Dialect dialect) {
    this.dataSource = dataSource;

    this.dialect = dialect;
  }

  @Override
  public final SqlTransaction beginTransaction(IsolationLevel level) throws UncheckedSqlException {
    int transactionIsolation;
    transactionIsolation = level.jdbcValue;

    Connection connection;

    try {
      connection = dataSource.getConnection();
    } catch (SQLException e) {
      throw new UncheckedSqlException(e);
    }

    try {
      connection.setAutoCommit(false);

      connection.setTransactionIsolation(transactionIsolation);

      return new WaySqlTransaction(dialect, connection);
    } catch (SQLException e) {
      try {
        connection.close();
      } catch (SQLException suppressed) {
        e.addSuppressed(suppressed);
      }

      throw new UncheckedSqlException(e);
    }
  }

}