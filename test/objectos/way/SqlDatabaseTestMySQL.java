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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import javax.sql.DataSource;
import objectos.way.Sql.MetaTable;
import org.mariadb.jdbc.MariaDbPoolDataSource;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SqlDatabaseTestMySQL {

  private final Consumer<Sql.Migrator> v001 = v("First Version", """
  create table T1 (ID int not null, primary key (ID));
  """);

  @SuppressWarnings("unused")
  private final Consumer<Sql.Migrator> v002 = v("Second Version", """
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

    000 | SCHEMA_HISTORY table created   | MIGRATE01@localhost  | 2025-03-10 10:00:00 | true
    001 | First Version                  | MIGRATE01@localhost  | 2025-03-10 10:01:00 | true

    # Tables

    MIGRATE01.SCHEMA_HISTORY
    MIGRATE01.T1
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

    db.migrate(v001);

    assertEquals(report(db), """
    # History

    000 | SCHEMA_HISTORY table created   | MIGRATE02@localhost  | 2025-03-10 10:00:00 | true
    001 | First Version                  | MIGRATE02@localhost  | 2025-03-10 10:01:00 | true

    # Tables

    MIGRATE02.SCHEMA_HISTORY
    MIGRATE02.T1
    """);

    db.migrate(v001);

    assertEquals(report(db), """
    # History

    000 | SCHEMA_HISTORY table created   | MIGRATE02@localhost  | 2025-03-10 10:00:00 | true
    001 | First Version                  | MIGRATE02@localhost  | 2025-03-10 10:01:00 | true

    # Tables

    MIGRATE02.SCHEMA_HISTORY
    MIGRATE02.T1
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

    000 | SCHEMA_HISTORY table created   | MIGRATE03@localhost  | 2025-03-10 10:00:00 | true
    001 | First Version                  | MIGRATE03@localhost  | 2025-03-10 10:01:00 | true

    # Tables

    MIGRATE03.SCHEMA_HISTORY
    MIGRATE03.T1
    """);

    db.migrate(v001.andThen(v002));

    assertEquals(report(db), """
    # History

    000 | SCHEMA_HISTORY table created   | MIGRATE03@localhost  | 2025-03-10 10:00:00 | true
    001 | First Version                  | MIGRATE03@localhost  | 2025-03-10 10:01:00 | true
    002 | Second Version                 | MIGRATE03@localhost  | 2025-03-10 10:02:00 | true

    # Tables

    MIGRATE03.SCHEMA_HISTORY
    MIGRATE03.T1
    MIGRATE03.T2
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

    000 | SCHEMA_HISTORY table created   | MIGRATE04@localhost  | 2025-03-10 10:00:00 | true
    001 | First Version                  | MIGRATE04@localhost  | 2025-03-10 10:01:00 | false

    # Tables

    MIGRATE04.SCHEMA_HISTORY
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

    final String methodName;
    methodName = caller.getMethodName();

    final String dbName;
    dbName = methodName.toUpperCase();

    try (
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:17003/", "root", "");
        Statement stmt = conn.createStatement();
    ) {
      stmt.execute("drop database if exists " + dbName);
      stmt.execute("drop user if exists " + dbName);

      stmt.execute("create database " + dbName);
      stmt.execute("create user " + dbName);

      stmt.execute("grant all on " + dbName + ".* to '" + dbName + "'@'localhost'");
    } catch (SQLException e) {
      throw new AssertionError("Failed to create DataSource", e);
    }

    final String url;
    url = "jdbc:mariadb://localhost:17003/" + dbName + "?user=" + dbName;

    final DataSource ds;

    try {
      ds = new MariaDbPoolDataSource(url);
    } catch (SQLException e) {
      throw new AssertionError("Failed to create DataSource", e);
    }

    return Sql.Database.create(config -> {
      final Clock clock;
      clock = new IncrementingClock(2025, 3, 10);

      config.clock(clock);

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
            installedBy, 20,
            installedOn,
            success
        );
      }
    }

    final Sql.Meta meta;
    meta = trx.meta();

    final List<MetaTable> beforeSchemaHistory;
    beforeSchemaHistory = meta.queryTables(filter -> {
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
        SCHEMA_HISTORY
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

      final String cat;
      cat = table.catalog();

      if ("information_schema".equals(cat)) {
        continue;
      }

      empty = false;

      t.fieldValue(cat + "." + table.name());

    }

    if (empty) {
      t.fieldValue("N/A");
    }

    return t.toString();
  }

}
