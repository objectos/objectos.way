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

import static org.testng.Assert.assertEquals;

import java.sql.BatchUpdateException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import objectos.way.Sql.MetaTable;
import org.h2.jdbcx.JdbcDataSource;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SqlDialectH2Test0Migrations {

  private final Consumer<Sql.Migrator> v001 = v("First Version", """
  create schema TEST;

  set schema TEST;

  create table T1 (ID int not null, primary key (ID));
  """);

  private final Consumer<Sql.Migrator> v002 = v("Second Version", """
  set schema TEST;

  create table T2 (ID int not null, primary key (ID));
  """);

  @Test(description = "Single run")
  public void migrate01() {
    final Sql.Database db;
    db = createdb();

    assertEquals(report(db), """
    # History

    N/A

    # Tables

    N/A
    """);

    db.migrate(v001);

    assertEquals(report(db), """
    # History

    000 | SCHEMA_HISTORY table created   | SA    | 2025-03-10 10:00:00 | true
    001 | First Version                  | SA    | 2025-03-10 10:01:00 | true

    # Tables

    PUBLIC.SCHEMA_HISTORY
    TEST.T1
    """);
  }

  @Test(description = "Two runs: same migration")
  public void migrate02() {
    final Sql.Database db;
    db = createdb();

    assertEquals(report(db), """
    # History

    N/A

    # Tables

    N/A
    """);

    db.migrate(migrator -> {
      migrator.add("First Version", """
      create schema TEST;

      set schema TEST;

      create table T1 (ID int not null, primary key (ID));
      """);
    });

    assertEquals(report(db), """
    # History

    000 | SCHEMA_HISTORY table created   | SA    | 2025-03-10 10:00:00 | true
    001 | First Version                  | SA    | 2025-03-10 10:01:00 | true

    # Tables

    PUBLIC.SCHEMA_HISTORY
    TEST.T1
    """);

    db.migrate(v001);

    assertEquals(report(db), """
    # History

    000 | SCHEMA_HISTORY table created   | SA    | 2025-03-10 10:00:00 | true
    001 | First Version                  | SA    | 2025-03-10 10:01:00 | true

    # Tables

    PUBLIC.SCHEMA_HISTORY
    TEST.T1
    """);
  }

  @Test(description = "Two runs: additional migration")
  public void migrate03() {
    final Sql.Database db;
    db = createdb();

    assertEquals(report(db), """
    # History

    N/A

    # Tables

    N/A
    """);

    db.migrate(v001);

    assertEquals(report(db), """
    # History

    000 | SCHEMA_HISTORY table created   | SA    | 2025-03-10 10:00:00 | true
    001 | First Version                  | SA    | 2025-03-10 10:01:00 | true

    # Tables

    PUBLIC.SCHEMA_HISTORY
    TEST.T1
    """);

    db.migrate(v001.andThen(v002));

    assertEquals(report(db), """
    # History

    000 | SCHEMA_HISTORY table created   | SA    | 2025-03-10 10:00:00 | true
    001 | First Version                  | SA    | 2025-03-10 10:01:00 | true
    002 | Second Version                 | SA    | 2025-03-10 10:02:00 | true

    # Tables

    PUBLIC.SCHEMA_HISTORY
    TEST.T1
    TEST.T2
    """);
  }

  @Test(description = "Single run: invalid migration")
  public void migrate04() {
    final Sql.Database db;
    db = createdb();

    assertEquals(report(db), """
    # History

    N/A

    # Tables

    N/A
    """);

    try {
      db.migrate(m -> m.add("First Version", """
      create schema TEST;

      some invalid SQL;
      """));

      Assert.fail("It should have thrown");
    } catch (Sql.MigrationFailedException expected) {
      final Throwable cause;
      cause = expected.getCause();

      assertEquals(cause instanceof BatchUpdateException, true);
    }

    assertEquals(report(db), """
    # History

    000 | SCHEMA_HISTORY table created   | SA    | 2025-03-10 10:00:00 | true
    001 | First Version                  | SA    | 2025-03-10 10:01:00 | false

    # Tables

    PUBLIC.SCHEMA_HISTORY
    """);
  }

  private Consumer<Sql.Migrator> v(String name, String script) {
    return migrator -> migrator.add(name, script);
  }

  private Sql.Database createdb() {
    final Throwable t;
    t = new Throwable();

    final StackTraceElement[] stackTrace;
    stackTrace = t.getStackTrace();

    final StackTraceElement caller;
    caller = stackTrace[1];

    return Sql.Database.create(config -> {
      config.clock(Y.clockIncMinutes(2025, 3, 10));

      final String dbName;
      dbName = caller.getMethodName();

      final String url;
      url = "jdbc:h2:mem:" + dbName + ";DB_CLOSE_DELAY=-1";

      final JdbcDataSource ds;
      ds = new JdbcDataSource();

      ds.setUrl(url);

      ds.setUser("sa");

      ds.setPassword("");

      config.dataSource(ds);

      config.noteSink(Y.noteSink());
    });
  }

  private String report(Sql.Database db) {
    String result;
    result = null;

    Sql.Transaction trx;
    trx = db.beginTransaction(Sql.READ_COMMITED);

    try {
      result = report(trx);

      trx.commit();
    } finally {
      trx.close();
    }

    return result;
  }

  private String report(Sql.Transaction trx) {
    Testable.Formatter t;
    t = Testable.Formatter.create();

    t.heading1("History");

    record History(int rank, String description, String installedBy, LocalDateTime installedOn, boolean success)
        implements Testable {

      static Sql.Mapper<History> MAPPER = Sql.createRecordMapper(History.class);

      @Override
      public void formatTestable(Testable.Formatter t) {
        t.row(
            rank, 3,
            description, 30,
            installedBy, 5,
            installedOn,
            success
        );
      }
    }

    final Sql.Meta meta;
    meta = trx.meta();

    final List<MetaTable> beforeSchemaHistory;
    beforeSchemaHistory = meta.queryTables(filter -> {
      filter.schemaName("PUBLIC");

      filter.tableName("SCHEMA_HISTORY");
    });

    if (!beforeSchemaHistory.isEmpty()) {

      trx.sql("""
      select
        INSTALLED_RANK,
        DESCRIPTION,
        INSTALLED_BY,
        INSTALLED_ON,
        SUCCESS
      from
        PUBLIC.SCHEMA_HISTORY
      """);

      List<History> historyRows;
      historyRows = trx.query(History.MAPPER);

      for (History row : historyRows) {
        row.formatTestable(t);
      }

    } else {

      t.fieldValue("N/A");

    }

    t.heading1("Tables");

    final List<MetaTable> tables;
    tables = meta.queryTables();

    boolean empty = true;

    for (MetaTable table : tables) {

      final String schema;
      schema = table.schema();

      if ("INFORMATION_SCHEMA".equals(schema)) {
        continue;
      }

      empty = false;

      t.fieldValue(schema + "." + table.name());

    }

    if (empty) {
      t.fieldValue("N/A");
    }

    return t.toString();
  }

}
