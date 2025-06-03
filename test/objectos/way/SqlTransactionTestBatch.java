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

import java.sql.BatchUpdateException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import objectos.way.Sql.BatchUpdate;
import org.testng.annotations.Test;

public class SqlTransactionTestBatch extends SqlTransactionTestSupport {

  @Test(enabled = false)
  @Override
  public void addBatch01() {
    // tested on batchUpdateXY
  }

  @Test
  @Override
  public void addIf01() {
    invalidOperation("addIf", trx -> trx.addIf("ABC", true));
  }

  @Test
  @Override
  public void addNullable01() {
    assertEquals(
        batchPrepared(
            List.of(),

            batches(
                batch(1)
            ),

            trx -> {
              trx.sql("insert into BAR (X) values (?)");

              trx.add(1);

              trx.addBatch();

              trx.add(null, Types.DATE);

              trx.addBatch();

              return trx.batchUpdate();
            }
        ),

        batch(1)
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X) values (?), 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 1)
        addBatch()
        setNull(1, 91)
        addBatch()
        executeBatch()
        close()
        """
    );

    assertEmpty(statement);

    assertEmpty(resultSet);
  }

  @Test
  public void addNullable02() {
    assertEquals(
        batchPrepared(
            List.of(),

            batches(
                batch(1)
            ),

            trx -> {
              trx.sql("insert into BAR (X) values (?)");

              trx.add(1);

              trx.addBatch();

              trx.add(2, Types.INTEGER);

              trx.addBatch();

              return trx.batchUpdate();
            }
        ),

        batch(1)
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X) values (?), 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 1)
        addBatch()
        setObject(1, 2)
        addBatch()
        executeBatch()
        close()
        """
    );

    assertEmpty(statement);

    assertEmpty(resultSet);
  }

  @Test
  @Override
  public void batchUpdate01() {
    assertEquals(
        batchPrepared(
            List.of(),

            batches(
                batch(1)
            ),

            trx -> {
              trx.sql("insert into BAR (X) values (?)");

              trx.add(1);

              trx.addBatch();

              return trx.batchUpdate();
            }
        ),

        batch(1)
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X) values (?), 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 1)
        addBatch()
        executeBatch()
        close()
        """
    );

    assertEmpty(statement);

    assertEmpty(resultSet);
  }

  @Test
  public void batchUpdate02() {
    assertEquals(
        batchPrepared(
            List.of(),

            batches(
                batch(1, 1)
            ),

            trx -> {
              trx.sql("insert into BAR (X) values (?)");

              trx.add(1);

              trx.addBatch();

              trx.add(2);

              trx.addBatch();

              return trx.batchUpdate();
            }
        ),

        batch(1, 1)
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X) values (?), 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 1)
        addBatch()
        setObject(1, 2)
        addBatch()
        executeBatch()
        close()
        """
    );

    assertEmpty(statement);

    assertEmpty(resultSet);
  }

  @Test
  @Override
  public void batchUpdateWithResult01() {
    assertEquals(
        batchPrepared(
            List.of(),

            batches(
                batch(1)
            ),

            trx -> {
              trx.sql("insert into BAR (X) values (?)");

              trx.add(1);

              trx.addBatch();

              final BatchUpdate result;
              result = trx.batchUpdateWithResult();

              assertEquals(result, new SqlBatchUpdateSuccess(batch(1)));

              return batch(1);
            }
        ),

        batch(1)
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X) values (?), 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 1)
        addBatch()
        executeBatch()
        close()
        """
    );

    assertEmpty(statement);

    assertEmpty(resultSet);
  }

  @Test
  public void batchUpdateWithResult02() {
    final BatchUpdateException batchUpdateError;
    batchUpdateError = new BatchUpdateException();

    assertEquals(
        batchPrepared(
            List.of(),

            batches(
                batch(1)
            ),

            trx -> {
              preparedStatement.batchUpdateError(batchUpdateError);

              trx.sql("insert into BAR (X) values (?)");

              trx.add(1);

              trx.addBatch();

              final BatchUpdate result;
              result = trx.batchUpdateWithResult();

              assertEquals(result, new SqlBatchUpdateFailed(batchUpdateError, List.of()));

              return batch(0);
            }
        ),

        batch(0)
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X) values (?), 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 1)
        addBatch()
        executeBatch()
        close()
        """
    );

    assertEmpty(statement);

    assertEmpty(resultSet);
  }

  @Test
  public void batchUpdateWithResult03() {
    final BatchUpdateException batchUpdateError;
    batchUpdateError = new BatchUpdateException();

    connection = new TestingConnection();

    preparedStatement = new TestingPreparedStatement();

    TestingPreparedStatement select;
    select = new TestingPreparedStatement();

    resultSet = new TestingResultSet(List.of(
        Map.of("1", "Hello", "2", "World!")
    ));

    select.queries(resultSet);

    connection.preparedStatements(preparedStatement, select);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      preparedStatement.batchUpdateError(batchUpdateError);

      trx.sql("insert into BAR (X) values (?)");

      trx.add(1);

      trx.addBatch();

      final BatchUpdate result;
      result = trx.batchUpdateWithResult();

      assertEquals(result, new SqlBatchUpdateFailed(batchUpdateError, List.of()));

      trx.sql("select A, B from X where NAME = ?");

      trx.add("ABC");

      final List<String2> rows;
      rows = trx.query(String2::new);

      assertEquals(rows, List.of(new String2("Hello", "World!")));
    } finally {
      trx.close();
    }

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X) values (?), 2)
        prepareStatement(select A, B from X where NAME = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 1)
        addBatch()
        executeBatch()
        close()
        """
    );

    assertEmpty(statement);

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
  }

  @Test
  @Override
  public void close01() {
    batchPrepared(
        List.of(),

        batches(),

        trx -> {
          trx.sql("insert into BAR (X) values (?)");

          trx.add(1);

          trx.addBatch();

          return batch();
        }
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X) values (?), 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 1)
        addBatch()
        close()
        """
    );

    assertEmpty(statement);

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

  @Test
  @Override
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
    The '%s' operation cannot be executed on a SQL batch statement.
    """.formatted(name);

    invalidOperation(
        trx -> {
          trx.sql("insert into FOO (X) values (?)");

          trx.add(123);

          trx.addBatch();

          operation.accept(trx);
        },

        expectedMessage
    );
  }

}