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
import java.util.function.Consumer;
import org.testng.annotations.Test;

public class SqlTransactionTestScript extends SqlTransactionTestSupport {

  @Test
  @Override
  public void addIf01() {
    invalidOperation("addIf", trx -> trx.addIf("abc", true));
  }

  @Test
  @Override
  public void addNullable01() {
    invalidOperation("addNullable", trx -> trx.add("abc", Types.VARCHAR));
  }

  @Test(description = """
  batchUpdate
  - happy path
  - one statement
  """)
  @Override
  public void batchUpdate01() {
    assertEquals(
        batchStatement(
            List.of(),

            batches(
                batch(1)
            ),

            trx -> {
              trx.sql(Sql.Kind.SCRIPT, """
              insert into FOO (A, B) values (1, 5)
              """);

              return trx.batchUpdate();
            }
        ),

        batch(1)
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
        addBatch(insert into FOO (A, B) values (1, 5))
        executeBatch()
        close()
        """
    );

    assertEmpty(preparedStatement);

    assertEmpty(resultSet);
  }

  @Test(description = """
  batchUpdate
  - happy path
  - two statements
  """)
  public void batchUpdate02() {
    assertEquals(
        batchStatement(
            List.of(),

            batches(
                batch(1, 1)
            ),

            trx -> {
              trx.sql(Sql.Kind.SCRIPT, """
              insert into FOO (A, B) values (1, 5)

              insert into BAR (X, Y) values ('A', 'B')
              """);

              return trx.batchUpdate();
            }
        ),

        batch(1, 1)
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
        addBatch(insert into FOO (A, B) values (1, 5))
        addBatch(insert into BAR (X, Y) values ('A', 'B'))
        executeBatch()
        close()
        """
    );

    assertEmpty(preparedStatement);

    assertEmpty(resultSet);
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
    invalidOperation("queryOptionalInt", Sql.Transaction::queryOptionalInt);
  }

  @Test
  @Override
  public void queryOptionalLong01() {
    invalidOperation("queryOptionalLong", Sql.Transaction::queryOptionalLong);
  }

  @Test
  @Override
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
  public final void update01() {
    invalidOperation("update", Sql.Transaction::update);
  }

  private void invalidOperation(String name, Consumer<Sql.Transaction> operation) {
    String expectedMessage = """
    The '%s' operation cannot be executed on a SQL script.
    """.formatted(name);

    invalidOperation(
        trx -> {
          trx.sql(Sql.Kind.SCRIPT, """
          insert into FOO (A, B) values (1, 5)

          insert into BAR (X, Y) values ('A', 'B')
          """);

          operation.accept(trx);
        },

        expectedMessage
    );
  }

}