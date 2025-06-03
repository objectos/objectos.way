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
import objectos.way.Sql.BatchUpdate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SqlTransactionTestGenerated extends SqlTransactionTestSupport {

  private Sql.GeneratedKeys.OfInt generatedKeys;

  @BeforeMethod
  public void resetGeneratedKeys() {
    generatedKeys = Sql.createGeneratedKeysOfInt();
  }

  @Test // currently unsupported
  @Override
  public void addBatch01() {
    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) values (123)");

          trx.with(generatedKeys);

          trx.addBatch();
        },

        """
        The 'addBatch' operation cannot be executed on a SQL statement:

        1) Returning generated keys; and
        2) With no parameter values set.
        """
    );
  }

  @Test
  @Override
  public void addIf01() {
    invalidOperation("addIf", trx -> trx.paramIf("abc", true));
  }

  @Test
  @Override
  public void addNullable01() {
    assertEquals(
        batchPrepared(
            List.of(
                Map.of("1", 123)
            ),

            batches(
                batch(1)
            ),

            trx -> {
              trx.sql("insert into FOO (A, B) values (?, ?)");

              trx.with(generatedKeys);

              trx.param(null, Types.DATE);

              trx.param("bar");

              trx.addBatch();

              return trx.batchUpdate();
            }
        ),

        batch(1)
    );

    assertEquals(generatedKeys.size(), 1);
    assertEquals(generatedKeys.getAsInt(0), 123);

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into FOO (A, B) values (?, ?), 1)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setNull(1, 91)
        setObject(2, bar)
        addBatch()
        executeBatch()
        getGeneratedKeys()
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

  @Test(description = """
  batchUpdate:
  - happy path
  - prepared
  - 1 batch
  """)
  @Override
  public void batchUpdate01() {
    assertEquals(
        batchPrepared(
            List.of(
                Map.of("1", 123)
            ),

            batches(
                batch(1)
            ),

            trx -> {
              trx.sql("insert into FOO (A, B) values (?, ?)");

              trx.with(generatedKeys);

              trx.param(123);

              trx.param("bar");

              trx.addBatch();

              return trx.batchUpdate();
            }
        ),

        batch(1)
    );

    assertEquals(generatedKeys.size(), 1);
    assertEquals(generatedKeys.getAsInt(0), 123);

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into FOO (A, B) values (?, ?), 1)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 123)
        setObject(2, bar)
        addBatch()
        executeBatch()
        getGeneratedKeys()
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

  @Test(description = """
  batchUpdate:
  - happy path
  - prepared
  - 2 batches
  """)
  public void batchUpdate02() {
    assertEquals(
        batchPrepared(
            List.of(
                Map.of("1", 123),
                Map.of("1", 124)
            ),

            batches(
                batch(1, 1)
            ),

            trx -> {
              trx.sql("insert into BAR (X) values (?)");

              trx.with(generatedKeys);

              trx.param(1);

              trx.addBatch();

              trx.param(2);

              trx.addBatch();

              return trx.batchUpdate();
            }
        ),

        batch(1, 1)
    );

    assertEquals(generatedKeys.size(), 2);
    assertEquals(generatedKeys.getAsInt(0), 123);
    assertEquals(generatedKeys.getAsInt(1), 124);

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X) values (?), 1)
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
        getGeneratedKeys()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        next()
        getInt(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test(description = """
  batchUpdate:
  - unhappy path
  - prepared
  - 0 batches
  """)
  public void batchUpdate03() {
    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) values (?)");

          trx.with(generatedKeys);

          trx.param(1);

          trx.batchUpdate();
        },

        """
        The 'batchUpdate' operation cannot be executed on a SQL statement with no batches defined.
        """
    );
  }

  @Test(description = """
  batchUpdate:
  - unhappy path
  - statement
  - 0 batches
  """)
  public void batchUpdate04() {
    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) values (123)");

          trx.with(generatedKeys);

          trx.batchUpdate();
        },

        """
        The 'batchUpdate' operation cannot be executed on a SQL statement with no batches defined.
        """
    );
  }

  @Test
  @Override
  public void batchUpdateWithResult01() {
    assertEquals(
        batchPrepared(
            List.of(
                Map.of("1", 123)
            ),

            batches(
                batch(1)
            ),

            trx -> {
              trx.sql("insert into FOO (A, B) values (?, ?)");

              trx.with(generatedKeys);

              trx.param(123);

              trx.param("bar");

              trx.addBatch();

              final BatchUpdate result;
              result = trx.batchUpdateWithResult();

              assertEquals(result, new SqlBatchUpdateSuccess(batch(1)));

              return batch(1);
            }
        ),

        batch(1)
    );

    assertEquals(generatedKeys.size(), 1);
    assertEquals(generatedKeys.getAsInt(0), 123);

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into FOO (A, B) values (?, ?), 1)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 123)
        setObject(2, bar)
        addBatch()
        executeBatch()
        getGeneratedKeys()
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
  public void close01() {
    batchPrepared(
        List.of(),

        batches(),

        trx -> {
          trx.sql("insert into BAR (X) values (?)");

          trx.with(generatedKeys);

          trx.param(1);

          trx.addBatch();

          return batch();
        }
    );

    assertEquals(generatedKeys.size(), 0);

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X) values (?), 1)
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

    assertEmpty(resultSet);

    assertEmpty(statement);
  }

  @Test
  public void close02() {
    batchPrepared(
        List.of(),

        batches(),

        trx -> {
          trx.sql("insert into BAR (X) values (?)");

          trx.with(generatedKeys);

          trx.param(1);

          return batch();
        }
    );

    assertEquals(generatedKeys.size(), 0);

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X) values (?), 1)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 1)
        close()
        """
    );

    assertEmpty(resultSet);

    assertEmpty(statement);
  }

  @Test
  public void close03() {
    batchPrepared(
        List.of(),

        batches(),

        trx -> {
          trx.sql("insert into BAR (X) values (?)");

          trx.with(generatedKeys);

          return batch();
        }
    );

    assertEquals(generatedKeys.size(), 0);

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
    assertEquals(
        updatePrepared(
            List.of(
                Map.of("1", 23)
            ),

            updates(1),

            trx -> {
              trx.sql("insert into FOO (A, B) values (?, ?)");

              trx.with(generatedKeys);

              trx.param(123);

              trx.param("bar");

              return trx.update();
            }
        ),

        1
    );

    assertEquals(generatedKeys.size(), 1);
    assertEquals(generatedKeys.getAsInt(0), 23);

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into FOO (A, B) values (?, ?), 1)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 123)
        setObject(2, bar)
        executeUpdate()
        getGeneratedKeys()
        close()
        """
    );

    assertEmpty(statement);

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        next()
        close()
        """
    );
  }

  @Test
  public void update02() {
    assertEquals(
        updateStatement(
            List.of(
                Map.of("1", 23)
            ),

            updates(1),

            trx -> {
              trx.sql("insert into FOO (A, B) values (123, 'bar')");

              trx.with(generatedKeys);

              return trx.update();
            }
        ),

        1
    );

    assertEquals(generatedKeys.size(), 1);
    assertEquals(generatedKeys.getAsInt(0), 23);

    assertEquals(
        connection.toString(),

        """
        createStatement()
        close()
        """
    );

    assertEmpty(preparedStatement);

    assertEquals(
        statement.toString(),

        """
        executeUpdate(insert into FOO (A, B) values (123, 'bar'), 1)
        getGeneratedKeys()
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
  }

  @Test
  public void update03() {
    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) select A from FOO where X = ?");

          trx.with(generatedKeys);

          trx.param(123);

          trx.addBatch();

          trx.update();
        },

        """
        The 'update' operation cannot be executed on a SQL batch statement returning generated keys.
        """
    );
  }

  private void invalidOperation(String name, Consumer<Sql.Transaction> operation) {
    String expectedMessage = """
    The '%s' operation cannot be executed on a SQL statement returning generated keys.
    """.formatted(name);

    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) values (123)");

          trx.with(generatedKeys);

          operation.accept(trx);
        },

        expectedMessage
    );

    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) select A from FOO where X = ?");

          trx.with(generatedKeys);

          trx.param(123);

          operation.accept(trx);
        },

        expectedMessage
    );

    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) select A from FOO where X = ?");

          trx.with(generatedKeys);

          trx.param(123);

          trx.addBatch();

          operation.accept(trx);
        },

        expectedMessage
    );
  }

}