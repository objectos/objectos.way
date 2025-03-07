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

import java.util.List;
import java.util.stream.Collectors;
import org.h2.jdbcx.JdbcConnectionPool;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SqlDatabaseTestH2 {

  private record Table(String name, String schema) {
    static Sql.Mapper<Table> MAPPER = Sql.createRecordMapper(Table.class);

    final String print() {
      return schema + "." + name;
    }
  }

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
    assertEquals(showTables(), "\n");

    db.migrate(migrator -> {

      migrator.add("First", """
      create table FIRST (
        ID int not null generated by default as identity,
        NAME varchar(32) not null,

        primary key (ID),
        unique (NAME)
      );
      """);

    });

    assertEquals(showTables(), """
    PUBLIC.SCHEMA_HISTORY
    """);
  }

  private String showTables() {
    Sql.Transaction trx;
    trx = db.beginTransaction(Sql.READ_COMMITED);

    trx.sql("show tables");

    List<Table> tables;
    tables = trx.query(Table.MAPPER);

    String n = System.lineSeparator();

    return tables.stream().map(Table::print).collect(Collectors.joining(n, "", n));
  }

}
