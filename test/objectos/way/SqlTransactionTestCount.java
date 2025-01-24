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
import java.util.Map;
import org.testng.annotations.Test;

public class SqlTransactionTestCount extends SqlTransactionTestSupport {

  @Test
  @Override
  public void addIf01() {
    invalidOperation(
        trx -> {
          trx.sql(Sql.Kind.COUNT, """
          select A, B from FOO
          where X = ?
          """);

          trx.addIf("ABC", true);
        },

        """
        The 'addIf' operation cannot be executed on a SQL count statement.
        """
    );
  }

  @Test
  public void addIf02() {
    invalidOperation(
        trx -> {
          trx.sql(Sql.Kind.COUNT, """
          select A, B from FOO
          where X = ? and Y = ?
          """);

          trx.add(123);

          trx.addIf("ABC", true);
        },

        """
        The 'addIf' operation cannot be executed on a SQL count statement.
        """
    );
  }

  @Test
  @Override
  public void batchUpdate01() {
    invalidOperation(
        trx -> {
          trx.sql(Sql.Kind.COUNT, """
          select A, B from FOO
          """);

          trx.batchUpdate();
        },

        """
        The 'batchUpdate' operation cannot be executed on a SQL count statement.
        """
    );
  }

  @Test
  public void batchUpdate02() {
    invalidOperation(
        trx -> {
          trx.sql(Sql.Kind.COUNT, """
          select A, B from FOO where X = ?
          """);

          trx.add(123);

          trx.batchUpdate();
        },

        """
        The 'batchUpdate' operation cannot be executed on a SQL count statement.
        """
    );
  }

  @Test(description = """
  querySingleInt
  - happy path
  - statement
  """)
  @Override
  public void querySingleInt01() {
    assertEquals(
        statement(
            List.of(
                Map.of("1", 23)
            ),

            trx -> {
              trx.sql(Sql.Kind.COUNT, """
              select A, B
              from FOO
              """);

              return trx.querySingleInt();
            }
        ),

        23
    );

    assertEquals(
        connection.toString(),

        """
        createStatement()
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        statement.toString(),

        """
        executeQuery(select count(*) from ( select A, B from FOO ) x)
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        next()
        close()
        """
    );

    assertEmpty(preparedStatement);
  }

  @Test(description = """
  querySingleInt
  - happy path
  - prepared
  """)
  public void querySingleInt02() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", 567)
            ),

            trx -> {
              trx.sql(Sql.Kind.COUNT, """
              select A, B
              from FOO
              where C = ?
              """);

              trx.add(123);

              return trx.querySingleInt();
            }
        ),

        567
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select count(*) from ( select A, B from FOO where C = ? ) x, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setInt(1, 123)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  @Override
  public void update01() {
    invalidOperation(
        trx -> {
          trx.sql(Sql.Kind.COUNT, """
          select A, B from FOO
          """);

          trx.update();
        },

        """
        The 'update' operation cannot be executed on a SQL count statement.
        """
    );
  }

  @Test
  public void update02() {
    invalidOperation(
        trx -> {
          trx.sql(Sql.Kind.COUNT, """
          select A, B from FOO where X = ?
          """);

          trx.add(123);

          trx.update();
        },

        """
        The 'update' operation cannot be executed on a SQL count statement.
        """
    );
  }

}