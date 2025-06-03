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

import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Consumer;
import org.testng.annotations.Test;

public class SqlTransactionTestCount extends SqlTransactionTestSupport {

  @Test
  @Override
  public void addBatch01() {
    invalidOperation("addBatch", Sql.Transaction::addBatch);
  }

  @Test
  @Override
  public void addIf01() {
    invalidOperation("addIf", trx -> trx.paramIf("ABC", true));
  }

  @Test
  @Override
  public void addNullable01() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql(Sql.Kind.COUNT, """
              select A, B
              from FOO
              where C = ?
              """);

              trx.param(null, Types.DATE);

              return trx.queryOptionalInt();
            }
        ),

        OptionalInt.empty()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select count(*) from ( select A, B from FOO where C = ? ) x, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setNull(1, 91)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  @Override
  public void batchUpdate01() {
    invalidOperation("batchUpdate", Sql.Transaction::batchUpdate);
  }

  @Test
  @Override
  public void batchUpdateWithResult01() {
    invalidOperation("batchUpdate", Sql.Transaction::batchUpdateWithResult);
  }

  @Test
  @Override
  public void close01() {
    preparedStatement(
        List.of(),

        trx -> {
          trx.sql(Sql.Kind.COUNT, """
          select A, B
          from FOO
          where C = ?
          """);

          trx.param(123);

          return 0;
        }
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select count(*) from ( select A, B from FOO where C = ? ) x, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 123)
        close()
        """
    );

    assertEmpty(resultSet);

    assertEmpty(statement);
  }

  @Test
  public void close02() {
    preparedStatement(
        List.of(),

        trx -> {
          trx.sql(Sql.Kind.COUNT, """
          select A, B
          from FOO
          where C = ?
          """);

          return 0;
        }
    );

    assertEquals(
        connection.toString(),

        """
        close()
        """
    );

    assertEmpty(preparedStatement);

    assertEmpty(resultSet);

    assertEmpty(statement);
  }

  @Test
  @Override
  public void query01() {
    invalidOperation("query", trx -> trx.query(Foo::new));
  }

  @Test
  @Override
  public void queryOptional01() {
    invalidOperation("queryOptional", trx -> trx.queryOptional(Foo::new));
  }

  @Test
  @Override
  public void queryOptionalInt01() {
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

              return trx.queryOptionalInt();
            }
        ),

        OptionalInt.of(23)
    );

    assertEquals(
        connection.toString(),

        """
        createStatement()
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

  @Test
  public void queryOptionalInt02() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql(Sql.Kind.COUNT, """
              select A, B
              from FOO
              where C = ?
              """);

              trx.param(123);

              return trx.queryOptionalInt();
            }
        ),

        OptionalInt.empty()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select count(*) from ( select A, B from FOO where C = ? ) x, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 123)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  @Override
  public void queryOptionalLong01() {
    assertEquals(
        statement(
            List.of(
                Map.of("1", 23L)
            ),

            trx -> {
              trx.sql(Sql.Kind.COUNT, """
              select A, B
              from FOO
              """);

              return trx.queryOptionalLong();
            }
        ),

        OptionalLong.of(23L)
    );

    assertEquals(
        connection.toString(),

        """
        createStatement()
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
        getLong(1)
        next()
        close()
        """
    );

    assertEmpty(preparedStatement);
  }

  @Test
  public void queryOptionalLong02() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql(Sql.Kind.COUNT, """
              select A, B
              from FOO
              where C = ?
              """);

              trx.param(123);

              return trx.queryOptionalLong();
            }
        ),

        OptionalLong.empty()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select count(*) from ( select A, B from FOO where C = ? ) x, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 123)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  @Override
  public void querySingle01() {
    invalidOperation("querySingle", trx -> trx.querySingle(Foo::new));
  }

  @Test(description = """
  querySingleInt
  - happy path
  - statement
  """)
  @Override
  public void querySingleInt01() {
    assertEquals(
        (int) statement(
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
        (int) preparedStatement(
            List.of(
                Map.of("1", 567)
            ),

            trx -> {
              trx.sql(Sql.Kind.COUNT, """
              select A, B
              from FOO
              where C = ?
              """);

              trx.param(123);

              return trx.querySingleInt();
            }
        ),

        567
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select count(*) from ( select A, B from FOO where C = ? ) x, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 123)
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
  public void querySingleLong01() {
    assertEquals(
        (long) statement(
            List.of(
                Map.of("1", 23L)
            ),

            trx -> {
              trx.sql(Sql.Kind.COUNT, """
              select A, B
              from FOO
              """);

              return trx.querySingleLong();
            }
        ),

        23L
    );

    assertEquals(
        connection.toString(),

        """
        createStatement()
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
        getLong(1)
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
  public void querySingleLong02() {
    assertEquals(
        (long) preparedStatement(
            List.of(
                Map.of("1", 567L)
            ),

            trx -> {
              trx.sql(Sql.Kind.COUNT, """
              select A, B
              from FOO
              where C = ?
              """);

              trx.param(123);

              return trx.querySingleLong();
            }
        ),

        567L
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select count(*) from ( select A, B from FOO where C = ? ) x, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 123)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getLong(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  @Override
  public void update01() {
    invalidOperation("update", Sql.Transaction::update);
  }

  private void invalidOperation(String name, Consumer<Sql.Transaction> operation) {
    String expectedMessage = """
    The '%s' operation cannot be executed on a SQL count statement.
    """.formatted(name);

    invalidOperation(
        trx -> {
          trx.sql(Sql.COUNT, "select A, B, C from FOO");

          operation.accept(trx);
        },

        expectedMessage
    );

    invalidOperation(
        trx -> {
          trx.sql(Sql.COUNT, "select A, B, C from FOO where X = ?");

          trx.param("ABC");

          operation.accept(trx);
        },

        expectedMessage
    );
  }

}