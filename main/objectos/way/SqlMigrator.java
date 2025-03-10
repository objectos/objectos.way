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

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

final class SqlMigrator implements Sql.Migrator, AutoCloseable {

  private final Clock clock;

  @SuppressWarnings("unused")
  private final Note.Sink noteSink;

  private final SqlDialect dialect;

  private final SqlTransaction trx;

  private Initialized initialized;

  private int rank = 1;

  SqlMigrator(Clock clock, Note.Sink noteSink, SqlDialect dialect, Connection connection) {
    this.clock = clock;

    this.noteSink = noteSink;

    this.dialect = dialect;

    trx = new SqlTransaction(dialect, connection);
  }

  record Initialized(
      boolean success,
      int maxRank,
      String user
  ) {

    Initialized(ResultSet rs, int idx) throws SQLException {
      this(
          rs.getBoolean(idx++),
          rs.getInt(idx++),
          rs.getString(idx++)
      );
    }

  }

  public final void initialize() {
    initialized = dialect.migratorInitialize(this);
  }

  @Override
  public final void add(String name, String script) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(script, "script == null");

    final int currentRank;
    currentRank = rank++;

    if (currentRank <= initialized.maxRank) {
      return;
    }

    trx.sql(Sql.SCRIPT, script);

    Sql.BatchUpdate result;
    result = trx.batchUpdateWithResult();

    switch (result) {
      case Sql.BatchUpdateSuccess ok -> {
        dialect.migratorHistory(this, currentRank, name, true);

        trx.commit();
      }

      case SqlBatchUpdateFailed error -> {
        trx.rollback();

        dialect.migratorHistory(this, currentRank, name, false);

        trx.commit();

        final BatchUpdateException original;
        original = error.original();

        throw new Sql.MigrationFailedException(original);
      }
    }
  }

  @Override
  public final void close() throws SQLException {}

  final LocalDateTime now() {
    return LocalDateTime.now(clock);
  }

  final SqlTransaction trx() { return trx; }

}