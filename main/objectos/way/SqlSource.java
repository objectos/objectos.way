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
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;

final class SqlSource implements Sql.Source {

  static class Builder {

    final DataSource dataSource;

    NoteSink noteSink = NoOpNoteSink.of();

    public Builder(DataSource dataSource) {
      this.dataSource = dataSource;
    }

    public final SqlSource build() throws SQLException {
      try (Connection connection = dataSource.getConnection()) {
        DatabaseMetaData data;
        data = connection.getMetaData();

        noteSink.send(Sql.METADATA, data, data.getDatabaseProductName(), data.getDatabaseProductVersion());

        SqlDialect dialect;
        dialect = SqlDialect.of(data);

        return new SqlSource(dataSource, dialect, noteSink);
      }
    }

  }

  private final DataSource dataSource;

  private final SqlDialect dialect;

  @SuppressWarnings("unused")
  private final NoteSink noteSink;

  private SqlSource(DataSource dataSource, SqlDialect dialect, NoteSink noteSink) {
    this.dataSource = dataSource;
    this.dialect = dialect;
    this.noteSink = noteSink;
  }

  @Override
  public final Sql.Transaction beginTransaction(Sql.Transaction.IsolationLevel level) throws Sql.UncheckedSqlException {
    int transactionIsolation;
    transactionIsolation = level.jdbcValue;

    Connection connection;

    try {
      connection = dataSource.getConnection();
    } catch (SQLException e) {
      throw new Sql.UncheckedSqlException(e);
    }

    try {
      connection.setTransactionIsolation(transactionIsolation);

      connection.setAutoCommit(false);

      return new SqlTransaction(dialect, connection);
    } catch (SQLException e) {
      try {
        connection.close();
      } catch (SQLException suppressed) {
        e.addSuppressed(suppressed);
      }

      throw new Sql.UncheckedSqlException(e);
    }
  }

}