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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.testng.annotations.Test;

public class SqlTransactionTestPaginated extends SqlTransactionTestSupport {

  private record String2(String a, String b) {
    String2(ResultSet rs, int idx) throws SQLException {
      this(rs.getString(idx++), rs.getString(idx++));
    }
  }

  private final Sql.Page page1 = Sql.Page.of(1, 15);
  private final Sql.Page page2 = Sql.Page.of(2, 15);

  @Override
  public void addIf01() {
    invalidOperation("addIf", trx -> trx.addIf("FOO", true));
  }

  @Override
  public void batchUpdate01() {
    invalidOperation("batchUpdate", Sql.Transaction::batchUpdate);
  }

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
        setAutoCommit(true)
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
  public void queryFirst01() {
    invalidOperation("queryFirst", trx -> trx.queryFirst(Foo::new));
  }

  @Test
  public void queryOptionalInt01() {
    invalidOperation("queryOptionalInt", Sql.Transaction::queryOptionalInt);
  }

  @Test
  public void queryOptionalLong01() {
    invalidOperation("queryOptionalLong", Sql.Transaction::queryOptionalLong);
  }

  @Test
  public void querySingle01() {
    invalidOperation("querySingle", trx -> trx.querySingle(Foo::new));
  }

  @Test
  @Override
  public void querySingleInt01() {
    invalidOperation("querySingleInt", Sql.Transaction::querySingleInt);
  }

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