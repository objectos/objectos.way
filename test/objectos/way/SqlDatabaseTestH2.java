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

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import objectos.way.Sql.MetaTable;
import org.h2.jdbcx.JdbcConnectionPool;
import org.testng.annotations.Test;

public class SqlDatabaseTestH2 {

  private final IncrementingClock clock = new IncrementingClock(2025, 3, 10);

  @Test
  public void migrate01() {
    test(
        migrator -> migrator.add("First Version", """
        create schema TEST;

        set schema TEST;

        create table T1 (
          ID int not null,

          primary key (ID)
        );
        """),

        """
        # History

        N/A

        # Tables

        """,

        """
        # History

        000 | SCHEMA_HISTORY table created   | SA    | 2025-03-10 10:00:00 | true
        001 | First Version                  | SA    | 2025-03-10 10:01:00 | true

        # Tables

        PUBLIC.SCHEMA_HISTORY
        TEST.T1
        """
    );
  }

  @Test(enabled = false, dependsOnMethods = "migrate01")
  public void migrate02() {

  }

  private void test(Consumer<Sql.Migrator> migration, String before, String after) {
    final JdbcConnectionPool ds;
    ds = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "");

    final Sql.Database db;
    db = Sql.Database.create(config -> {
      config.clock(clock);

      config.dataSource(ds);

      config.noteSink(TestingNoteSink.INSTANCE);
    });

    assertEquals(report(db), before);

    db.migrate(migration);

    assertEquals(report(db), after);
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

      t.row("N/A", 3);

    }

    t.heading1("Tables");

    final List<MetaTable> tables;
    tables = meta.queryTables();

    for (MetaTable table : tables) {

      final String schema;
      schema = table.schema();

      if ("INFORMATION_SCHEMA".equals(schema)) {
        continue;
      }

      t.fieldValue(schema + "." + table.name());

    }

    return t.toString();
  }

}
