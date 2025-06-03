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
import java.util.function.Consumer;
import org.testng.annotations.Test;

public class SqlTransactionTestPaginated extends SqlTransactionTestSupport {

  private final Sql.Page page1 = Sql.Page.of(1, 15);
  private final Sql.Page page2 = Sql.Page.of(2, 15);

  @Test
  @Override
  public void addBatch01() {
    invalidOperation("addBatch", trx -> trx.addBatch());
  }

  @Test
  @Override
  public void addIf01() {
    invalidOperation("addIf", trx -> trx.addIf("FOO", true));
  }

  @Test
  @Override
  public void addNullable01() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", "Hello", "2", "World!")
            ),

            trx -> {
              trx.sql("""
              select A, B
              from FOO
              where C = ?
              """);

              trx.with(page1);

              trx.add(null, Types.DATE);

              return trx.query(String2::new);
            }
        ),

        List.of(
            new String2("Hello", "World!")
        )
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B from FOO where C = ? limit 15, 2)
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
        getString(1)
        getString(2)
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
  public final void batchUpdateWithResult01() {
    invalidOperation("batchUpdate", Sql.Transaction::batchUpdateWithResult);
  }

  @Test
  @Override
  public void close01() {
    preparedStatement(
        List.of(),

        trx -> {
          trx.sql("""
          select A, B
          from FOO
          where C = ?
          """);

          trx.with(page1);

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
  public void close02() {
    preparedStatement(
        List.of(
            Map.of("1", "Hello", "2", "World!")
        ),

        trx -> {
          trx.sql("""
          select A, B
          from FOO
          where C = ?
          """);

          trx.with(page1);

          trx.add(123);

          return 0;
        }
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B from FOO where C = ? limit 15, 2)
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

  @Override
  @Test(description = """
  query:
  - prepared
  - first page
  """)
  public void query01() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", "Hello", "2", "World!")
            ),

            trx -> {
              trx.sql("""
              select A, B
              from FOO
              where C = ?
              """);

              trx.with(page1);

              trx.add(123);

              return trx.query(String2::new);
            }
        ),

        List.of(
            new String2("Hello", "World!")
        )
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B from FOO where C = ? limit 15, 2)
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
        getString(1)
        getString(2)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test(description = """
  query:
  - prepared
  - second page
  """)
  public void query02() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", "Hello", "2", "World!")
            ),

            trx -> {
              trx.sql("""
              select A, B
              from FOO
              where C = ?
              """);

              trx.with(page2);

              trx.add(123);

              return trx.query(String2::new);
            }
        ),

        List.of(
            new String2("Hello", "World!")
        )
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B from FOO where C = ? limit 15 offset 15, 2)
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
        getString(1)
        getString(2)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test(description = """
  query:
  - regular statement
  - first page
  """)
  public void query03() {
    assertEquals(
        statement(
            List.of(
                Map.of("1", "Hello", "2", "World!")
            ),

            trx -> {
              trx.sql("""
              select A, B
              from FOO
              """);

              trx.with(page1);

              return trx.query(String2::new);
            }
        ),

        List.of(
            new String2("Hello", "World!")
        )
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
        executeQuery(select A, B from FOO limit 15)
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getString(1)
        getString(2)
        next()
        close()
        """
    );

    assertEmpty(preparedStatement);
  }

  @Test
  @Override
  public void queryOptional01() {
    invalidOperation("queryOptional", trx -> trx.queryOptional(Foo::new));
  }

  @Override
  @Test
  public void queryOptionalInt01() {
    invalidOperation("queryOptionalInt", Sql.Transaction::queryOptionalInt);
  }

  @Override
  @Test
  public void queryOptionalLong01() {
    invalidOperation("queryOptionalLong", Sql.Transaction::queryOptionalLong);
  }

  @Override
  @Test
  public void querySingle01() {
    invalidOperation("querySingle", trx -> trx.querySingle(Foo::new));
  }

  @Test
  @Override
  public void querySingleInt01() {
    invalidOperation("querySingleInt", Sql.Transaction::querySingleInt);
  }

  @Override
  @Test
  public void querySingleLong01() {
    invalidOperation("querySingleLong", Sql.Transaction::querySingleLong);
  }

  @Test
  @Override
  public void update01() {
    invalidOperation("update", Sql.Transaction::update);
  }

  private void invalidOperation(String name, Consumer<Sql.Transaction> operation) {
    String expectedMessage = """
    The '%s' operation cannot be executed on a paginated SQL statement.
    """.formatted(name);

    invalidOperation(
        trx -> {
          trx.sql("select A, B, C from FOO");

          trx.with(page1);

          operation.accept(trx);
        },

        expectedMessage
    );

    invalidOperation(
        trx -> {
          trx.sql("select A, B, C from FOO where X = ?");

          trx.with(page1);

          trx.add("ABC");

          operation.accept(trx);
        },

        expectedMessage
    );
  }

}