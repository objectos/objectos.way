/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.sql;

import static org.testng.Assert.assertEquals;

import java.sql.DatabaseMetaData;
import org.testng.annotations.Test;

public class MysqlDialectTest {

  @Test
  public void of() {
    DatabaseMetaData data;
    data = TestingDatabaseMetaData.MYSQL_5_7;

    Dialect dialect;
    dialect = Dialect.of(data);

    assertEquals(dialect.getClass(), Dialect.class);
  }
  
  @Test
  public void count() {
    Dialect dialect;
    dialect = TestingDialect.MYSQL_5_7;

    StringBuilder sqlBuilder;
    sqlBuilder = new StringBuilder("select * from FOO");

    dialect.count(sqlBuilder);

    assertEquals(sqlBuilder.toString(), """
    select count(*) from (
    select * from FOO
    ) x
    """);
  }
  
  @Test
  public void paginate01() {
    Dialect dialect;
    dialect = TestingDialect.MYSQL_5_7;

    StringBuilder sqlBuilder;
    sqlBuilder = new StringBuilder("select * from FOO");

    dialect.paginate(sqlBuilder, new TestingPage(1, 15));

    assertEquals(sqlBuilder.toString(), """
    select * from FOO
    limit 15
    """);
  }

  @Test
  public void paginate02() {
    Dialect dialect;
    dialect = TestingDialect.MYSQL_5_7;

    StringBuilder sqlBuilder;
    sqlBuilder = new StringBuilder("select * from FOO");

    dialect.paginate(sqlBuilder, new TestingPage(3, 15));

    assertEquals(sqlBuilder.toString(), """
    select * from FOO
    limit 15
    offset 30
    """);
  }

}