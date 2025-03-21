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
import java.sql.SQLException;
import java.time.Clock;
import java.util.function.Consumer;
import javax.sql.DataSource;
import objectos.way.Sql.DatabaseException;
import objectos.way.Sql.Migrator;

final class SqlDatabase implements Sql.Database {

  private final Clock clock;

  private final DataSource dataSource;

  private final SqlDialect dialect;

  @SuppressWarnings("unused")
  private final Note.Sink noteSink;

  SqlDatabase(Clock clock, Note.Sink noteSink, DataSource dataSource, SqlDialect dialect) {
    this.clock = clock;

    this.noteSink = noteSink;

    this.dataSource = dataSource;

    this.dialect = dialect;
  }

  @Override
  public final Sql.Transaction beginTransaction(Sql.Transaction.Isolation level) throws Sql.DatabaseException {
    Sql.TransactionIsolation impl;
    impl = (Sql.TransactionIsolation) level;

    int transactionIsolation;
    transactionIsolation = impl.jdbcValue;

    Connection connection;

    try {
      connection = dataSource.getConnection();
    } catch (SQLException e) {
      throw new Sql.DatabaseException(e);
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

      throw new Sql.DatabaseException(e);
    }
  }

  @Override
  public final void migrate(Consumer<Migrator> config) throws DatabaseException {
    try (
        Connection connection = dataSource.getConnection();
        SqlMigrator migrator = new SqlMigrator(clock, noteSink, dialect, connection)
    ) {

      migrator.initialize();

      config.accept(migrator);

    } catch (SQLException e) {
      throw new Sql.DatabaseException(e);
    }
  }

}