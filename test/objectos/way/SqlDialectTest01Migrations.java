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
import java.util.function.Function;
import objectos.way.Sql.MetaTable;
import objectos.way.Sql.Migrations;
import objectos.way.Sql.Meta.QueryTables;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SqlDialectTest01Migrations extends SqlDialectTest00Support {

  @SuppressWarnings("exports")
  @Test(description = "Single run", dataProvider = "dbDialectProvider")
  public void migrate01(Sql.Database db, SqlDialect dialect) {
    assertEquals(
        report(db, dialect),

        """
        # History

        N/A

        # Tables

        N/A
        """
    );

    db.migrate(
        v("First Version", switch (dialect) {
          case H2 -> """
          create schema TEST;

          set schema TEST;

          create table T1 (ID int not null, primary key (ID));
          """;

          case MYSQL -> "create table T1 (ID int not null, primary key (ID));";

          case TESTING -> throw new UnsupportedOperationException();
        })
    );

    assertEquals(
        report(db, dialect),

        switch (dialect) {
          case H2 -> """
          # History

          000 | SCHEMA_HISTORY table created   | SA                   | 2025-03-10 10:00:00 | true
          001 | First Version                  | SA                   | 2025-03-10 10:01:00 | true

          # Tables

          PUBLIC.SCHEMA_HISTORY
          TEST.T1
          """;

          case MYSQL -> """
          # History

          000 | SCHEMA_HISTORY table created   | MIGRATE_01@localhost | 2025-03-10 10:00:00 | true
          001 | First Version                  | MIGRATE_01@localhost | 2025-03-10 10:01:00 | true

          # Tables

          MIGRATE_01.SCHEMA_HISTORY
          MIGRATE_01.T1
          """;

          case TESTING -> throw new UnsupportedOperationException();
        }
    );
  }

  @SuppressWarnings("exports")
  @Test(description = "Two runs: same migration", dataProvider = "dbDialectProvider")
  public void migrate02(Sql.Database db, SqlDialect dialect) {
    final Consumer<Migrations> v001;
    v001 = v("First Version", switch (dialect) {
      case H2 -> """
      create schema TEST;

      set schema TEST;

      create table T1 (ID int not null, primary key (ID));
      """;

      case MYSQL -> "create table T1 (ID int not null, primary key (ID));";

      case TESTING -> throw new UnsupportedOperationException();
    });

    final String v001Expected;
    v001Expected = switch (dialect) {
      case H2 -> """
      # History

      000 | SCHEMA_HISTORY table created   | SA                   | 2025-03-10 10:00:00 | true
      001 | First Version                  | SA                   | 2025-03-10 10:01:00 | true

      # Tables

      PUBLIC.SCHEMA_HISTORY
      TEST.T1
      """;

      case MYSQL -> """
      # History

      000 | SCHEMA_HISTORY table created   | MIGRATE_02@localhost | 2025-03-10 10:00:00 | true
      001 | First Version                  | MIGRATE_02@localhost | 2025-03-10 10:01:00 | true

      # Tables

      MIGRATE_02.SCHEMA_HISTORY
      MIGRATE_02.T1
      """;

      case TESTING -> throw new UnsupportedOperationException();
    };

    assertEquals(
        report(db, dialect),

        """
        # History

        N/A

        # Tables

        N/A
        """
    );

    db.migrate(v001);

    assertEquals(
        report(db, dialect),

        v001Expected
    );

    db.migrate(v001);

    assertEquals(
        report(db, dialect),

        v001Expected
    );
  }

  @SuppressWarnings("exports")
  @Test(description = "Two runs: additional migration", dataProvider = "dbDialectProvider")
  public void migrate03(Sql.Database db, SqlDialect dialect) {
    final Consumer<Migrations> v001;
    v001 = v("First Version", switch (dialect) {
      case H2 -> """
      create schema TEST;

      set schema TEST;

      create table T1 (ID int not null, primary key (ID));
      """;

      case MYSQL -> "create table T1 (ID int not null, primary key (ID));";

      case TESTING -> throw new UnsupportedOperationException();
    });

    final String v001Expected;
    v001Expected = switch (dialect) {
      case H2 -> """
      # History

      000 | SCHEMA_HISTORY table created   | SA                   | 2025-03-10 10:00:00 | true
      001 | First Version                  | SA                   | 2025-03-10 10:01:00 | true

      # Tables

      PUBLIC.SCHEMA_HISTORY
      TEST.T1
      """;

      case MYSQL -> """
      # History

      000 | SCHEMA_HISTORY table created   | MIGRATE_03@localhost | 2025-03-10 10:00:00 | true
      001 | First Version                  | MIGRATE_03@localhost | 2025-03-10 10:01:00 | true

      # Tables

      MIGRATE_03.SCHEMA_HISTORY
      MIGRATE_03.T1
      """;

      case TESTING -> throw new UnsupportedOperationException();
    };

    final Consumer<Migrations> v002;
    v002 = v("Second Version", switch (dialect) {
      case H2 -> """
      set schema TEST;

      create table T2 (ID int not null, primary key (ID));
      """;

      case MYSQL -> "create table T2 (ID int not null, primary key (ID));";

      case TESTING -> throw new UnsupportedOperationException();
    });

    final String v002Expected;
    v002Expected = switch (dialect) {
      case H2 -> """
      # History

      000 | SCHEMA_HISTORY table created   | SA                   | 2025-03-10 10:00:00 | true
      001 | First Version                  | SA                   | 2025-03-10 10:01:00 | true
      002 | Second Version                 | SA                   | 2025-03-10 10:02:00 | true

      # Tables

      PUBLIC.SCHEMA_HISTORY
      TEST.T1
      TEST.T2
      """;

      case MYSQL -> """
      # History

      000 | SCHEMA_HISTORY table created   | MIGRATE_03@localhost | 2025-03-10 10:00:00 | true
      001 | First Version                  | MIGRATE_03@localhost | 2025-03-10 10:01:00 | true
      002 | Second Version                 | MIGRATE_03@localhost | 2025-03-10 10:02:00 | true

      # Tables

      MIGRATE_03.SCHEMA_HISTORY
      MIGRATE_03.T1
      MIGRATE_03.T2
      """;

      case TESTING -> throw new UnsupportedOperationException();
    };

    assertEquals(
        report(db, dialect),

        """
        # History

        N/A

        # Tables

        N/A
        """
    );

    db.migrate(v001);

    assertEquals(
        report(db, dialect),

        v001Expected
    );

    db.migrate(v001.andThen(v002));

    assertEquals(
        report(db, dialect),

        v002Expected
    );
  }

  @SuppressWarnings("exports")
  @Test(description = "Single run: invalid migration", dataProvider = "dbDialectProvider")
  public void migrate04(Sql.Database db, SqlDialect dialect) {
    final Consumer<Migrations> v001;
    v001 = v("First Version", "some invalid SQL;");

    final String v001Expected;
    v001Expected = switch (dialect) {
      case H2 -> """
      # History

      000 | SCHEMA_HISTORY table created   | SA                   | 2025-03-10 10:00:00 | true
      001 | First Version                  | SA                   | 2025-03-10 10:01:00 | false

      # Tables

      PUBLIC.SCHEMA_HISTORY
      """;

      case MYSQL -> """
      # History

      000 | SCHEMA_HISTORY table created   | MIGRATE_04@localhost | 2025-03-10 10:00:00 | true
      001 | First Version                  | MIGRATE_04@localhost | 2025-03-10 10:01:00 | false

      # Tables

      MIGRATE_04.SCHEMA_HISTORY
      """;

      case TESTING -> throw new UnsupportedOperationException();
    };

    assertEquals(
        report(db, dialect),

        """
        # History

        N/A

        # Tables

        N/A
        """
    );

    try {
      db.migrate(v001);

      Assert.fail("It should have thrown");
    } catch (Sql.MigrationFailedException expected) {
      final Throwable cause;
      cause = expected.getCause();

      assertEquals(cause instanceof BatchUpdateException, true);
    }

    assertEquals(
        report(db, dialect),

        v001Expected
    );
  }

  private Consumer<Sql.Migrations> v(String name, String script) {
    return migrator -> migrator.apply(name, script);
  }

  private record ReportConfig(
      Consumer<QueryTables> queryTables,
      String schemaHistoryTableName,
      Function<MetaTable, String> metaTable
  ) {}

  private static final ReportConfig REPORT_H2 = new ReportConfig(
      filter -> {
        filter.schemaName("PUBLIC");

        filter.tableName("SCHEMA_HISTORY");
      },

      "PUBLIC.SCHEMA_HISTORY",

      table -> {
        final String schema;
        schema = table.schema();

        if ("INFORMATION_SCHEMA".equals(schema)) {
          return null;
        } else {
          return schema + "." + table.name();
        }
      }
  );

  private static final ReportConfig REPORT_MYSQL = new ReportConfig(
      filter -> {
        filter.tableName("SCHEMA_HISTORY");
      },

      "SCHEMA_HISTORY",

      table -> {
        final String cat;
        cat = table.catalog();

        if ("information_schema".equals(cat)) {
          return null;
        } else {
          return cat + "." + table.name();
        }
      }
  );

  private String report(Sql.Database db, SqlDialect dialect) {
    String result;
    result = null;

    final Sql.Transaction trx;
    trx = db.connect();

    final ReportConfig config;
    config = switch (dialect) {
      case H2 -> REPORT_H2;

      case MYSQL -> REPORT_MYSQL;

      case TESTING -> throw new UnsupportedOperationException();
    };

    try {
      result = report(trx, config);

      trx.commit();
    } finally {
      trx.close();
    }

    return result;
  }

  private String report(Sql.Transaction trx, ReportConfig config) {
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
    beforeSchemaHistory = meta.queryTables(config.queryTables);

    if (!beforeSchemaHistory.isEmpty()) {

      trx.sql("""
      select
        INSTALLED_RANK,
        DESCRIPTION,
        INSTALLED_BY,
        INSTALLED_ON,
        SUCCESS
      from
        %s
      """);

      trx.format(config.schemaHistoryTableName);

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

    final Function<MetaTable, String> metaTableFun;
    metaTableFun = config.metaTable;

    boolean empty = true;

    for (MetaTable table : tables) {

      final String value;
      value = metaTableFun.apply(table);

      if (value == null) {
        continue;
      }

      empty = false;

      t.fieldValue(value);

    }

    if (empty) {
      t.fieldValue("N/A");
    }

    return t.toString();
  }

}
