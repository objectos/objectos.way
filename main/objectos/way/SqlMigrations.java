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
import java.util.List;
import java.util.Objects;
import objectos.way.Sql.MetaTable;

final class SqlMigrations implements Sql.Migrations, AutoCloseable {

  private final Clock clock;

  @SuppressWarnings("unused")
  private final Note.Sink noteSink;

  private final SqlDialect dialect;

  private final SqlTransaction trx;

  private Initialized initialized;

  private int rank = 1;

  SqlMigrations(Clock clock, Note.Sink noteSink, SqlDialect dialect, Connection connection) {
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
    initialized = switch (dialect) {
      case H2 -> initializeH2();

      case MYSQL -> initializeMySQL();

      case TESTING -> throw new UnsupportedOperationException();
    };
  }

  private Initialized initializeH2() {
    trx.sql("set schema PUBLIC");

    trx.update();

    final Sql.Meta meta;
    meta = trx.meta();

    final List<MetaTable> tables;
    tables = meta.queryTables(filter -> {
      filter.schemaName("PUBLIC");

      filter.tableName("SCHEMA_HISTORY");
    });

    if (tables.isEmpty()) {

      trx.sql(Sql.SCRIPT, """
      create table if not exists SCHEMA_HISTORY (
        INSTALLED_RANK int not null,
        DESCRIPTION varchar(200) not null,
        INSTALLED_BY varchar(100) not null,
        INSTALLED_ON timestamp default current_timestamp,
        EXECUTION_TIME int not null,
        SUCCESS boolean not null,

        constraint SCHEMA_HISTORY_PK primary key (INSTALLED_RANK)
      );

      create index SCHEMA_HISTORY_S_IDX on SCHEMA_HISTORY (SUCCESS);
      """);

      trx.batchUpdate();

      schemaHistory(0, "SCHEMA_HISTORY table created", true);

    }

    trx.sql("""
    select
      not exists(select 1 from SCHEMA_HISTORY where SUCCESS = false),
      max(INSTALLED_RANK),
      user()
    from
      SCHEMA_HISTORY
    """);

    final SqlMigrations.Initialized result;
    result = trx.querySingle(SqlMigrations.Initialized::new);

    trx.commit();

    return result;
  }

  private Initialized initializeMySQL() {
    final Sql.Meta meta;
    meta = trx.meta();

    final List<MetaTable> tables;
    tables = meta.queryTables(filter -> {
      filter.tableName("SCHEMA_HISTORY");
    });

    if (tables.isEmpty()) {

      trx.sql(Sql.SCRIPT, """
      create table if not exists SCHEMA_HISTORY (
        INSTALLED_RANK int not null,
        DESCRIPTION varchar(200) not null,
        INSTALLED_BY varchar(100) not null,
        INSTALLED_ON timestamp default current_timestamp,
        EXECUTION_TIME int not null,
        SUCCESS boolean not null,

        constraint SCHEMA_HISTORY_PK primary key (INSTALLED_RANK)
      );

      create index SCHEMA_HISTORY_S_IDX on SCHEMA_HISTORY (SUCCESS);
      """);

      trx.batchUpdate();

      schemaHistory(0, "SCHEMA_HISTORY table created", true);

    }

    trx.sql("""
    select
      not exists(select 1 from SCHEMA_HISTORY where SUCCESS = false),
      max(INSTALLED_RANK),
      user()
    from
      SCHEMA_HISTORY
    """);

    final SqlMigrations.Initialized result;
    result = trx.querySingle(SqlMigrations.Initialized::new);

    trx.commit();

    return result;
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
        schemaHistory(currentRank, name, true);

        trx.commit();
      }

      case SqlBatchUpdateFailed error -> {
        trx.rollback();

        schemaHistory(currentRank, name, false);

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

  final SqlTransaction trx() {
    return trx;
  }

  private void schemaHistory(int rank, String name, boolean success) {
    final String sql;
    sql = switch (dialect) {
      case H2 -> """
      insert into PUBLIC.SCHEMA_HISTORY
      values (?, ?, user(), ?, 0, ?)
      """;

      case MYSQL -> """
      insert into SCHEMA_HISTORY
      values (?, ?, user(), ?, 0, ?)
      """;

      case TESTING -> throw new UnsupportedOperationException();
    };

    trx.sql(sql);

    trx.add(rank);

    trx.add(name);

    trx.add(now());

    trx.add(success);

    trx.update();
  }

}