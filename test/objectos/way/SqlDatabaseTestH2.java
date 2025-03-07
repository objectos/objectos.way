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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.h2.jdbcx.JdbcConnectionPool;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SqlDatabaseTestH2 {

  private Sql.Database db;

  @BeforeClass
  public void beforeClass() {
    db = Sql.Database.create(config -> {
      final JdbcConnectionPool ds;
      ds = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "");

      TestingShutdownHook.register(ds::dispose);

      config.dataSource(ds);

      config.noteSink(TestingNoteSink.INSTANCE);
    });
  }

  @Test
  public void migrate01() {
    assertEquals(
        showTables(),

        ""
    );

    db.migrate(migrator -> {

      migrator.add("First Version", """
      create schema TEST;

      set schema TEST;

      create table T1 (
        ID int not null,

        primary key (ID)
      );
      """);

    });

    assertEquals(
        showTables(),

        """
        PUBLIC.SCHEMA_HISTORY
        TEST.T1
        """
    );
  }

  @Test(dependsOnMethods = "migrate01")
  public void migrate02() {

  }

  private String showTables() {
    record Schema(String name) implements Comparable<Schema> {
      static Sql.Mapper<Schema> MAPPER = Sql.createRecordMapper(Schema.class);

      @Override
      public int compareTo(Schema o) {
        return name.compareTo(o.name);
      }
    }

    record Table(String name, String schema) {
      static Sql.Mapper<Table> MAPPER = Sql.createRecordMapper(Table.class);

      final String print() {
        return schema + "." + name;
      }
    }

    Sql.Transaction trx;
    trx = db.beginTransaction(Sql.READ_COMMITED);

    try {
      trx.sql("show schemas");

      List<Schema> _schemas;
      _schemas = trx.query(Schema.MAPPER);

      List<Schema> schemas;
      schemas = new ArrayList<>(_schemas);

      schemas.sort(Comparator.naturalOrder());

      List<Table> all;
      all = Util.createList();

      for (Schema schema : schemas) {
        if ("INFORMATION_SCHEMA".equals(schema.name)) {
          continue;
        }

        trx.sql("show tables from " + schema.name);

        List<Table> tables;
        tables = trx.query(Table.MAPPER);

        all.addAll(tables);
      }

      if (all.isEmpty()) {
        return "";
      }

      String n;
      n = System.lineSeparator();

      String result;
      result = all.stream().map(Table::print).collect(Collectors.joining(n, "", n));

      trx.commit();

      return result;
    } finally {
      trx.close();
    }
  }

}
