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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.testng.annotations.Test;

public class SqlDialectH2Test {

  @Test
  public void of() throws SQLException {
    DatabaseMetaData data;
    data = TestingDatabaseMetaData.H2;

    SqlDialect dialect;
    dialect = SqlDialect.of(data);

    assertEquals(dialect.getClass(), SqlDialect.class);
  }
  
  @Test
  public void count() {
    SqlDialect dialect;
    dialect = TestingSqlDialect.H2;

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
    SqlDialect dialect;
    dialect = TestingSqlDialect.H2;

    StringBuilder sqlBuilder;
    sqlBuilder = new StringBuilder("select * from FOO");

    dialect.paginate(sqlBuilder, Sql.createPage(1, 15));

    assertEquals(sqlBuilder.toString(), """
    select * from FOO
    fetch first 15 rows only
    """);
  }

  @Test
  public void paginate02() {
    SqlDialect dialect;
    dialect = TestingSqlDialect.H2;

    StringBuilder sqlBuilder;
    sqlBuilder = new StringBuilder("select * from FOO");

    dialect.paginate(sqlBuilder, Sql.createPage(3, 15));

    assertEquals(sqlBuilder.toString(), """
    select * from FOO
    offset 30 rows
    fetch first 15 rows only
    """);
  }

}