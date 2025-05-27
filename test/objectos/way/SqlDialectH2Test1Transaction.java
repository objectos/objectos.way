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

import org.h2.jdbcx.JdbcConnectionPool;
import org.testng.annotations.Test;

public class SqlDialectH2Test1Transaction {

  private final Sql.Database db = createDb();

  @Test(enabled = false)
  public void param01() {
    db.beginTransaction(Sql.READ_COMMITED);
  }

  private Sql.Database createDb() {
    final Sql.Database db;
    db = Sql.Database.create(config -> {
      config.clock(Y.clockIncMinutes(2025, 3, 10));

      final String url;
      url = "jdbc:h2:mem:sql_dialect_h2_test_1_transaction";

      final JdbcConnectionPool ds;
      ds = JdbcConnectionPool.create(url, "sa", "");

      config.dataSource(ds);

      config.noteSink(Y.noteSink());
    });

    db.migrate(migrations -> {
      migrations.add("Create test schema", """
      create table TYPES_STRING (
        ID int not null,

        T_CHAR_5 char(5) not null,
        T_VARCHAR_5 varchar(5) not null,

        primary key (ID)
      );

      create table TYPES_INTEGER (
        ID int not null,

        T_TINYINT tinyint not null,
        T_SMALLINT smallint not null,
        T_INT int not null,
        T_BIGINT bigint not null,

        primary key (ID)
      );
      """);
    });

    return db;
  }

}
