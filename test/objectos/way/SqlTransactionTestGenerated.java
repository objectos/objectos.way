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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SqlTransactionTestGenerated extends SqlTransactionTestSupport {

  private Sql.GeneratedKeys.OfInt generatedKeys;

  @BeforeMethod
  public void resetGeneratedKeys() {
    generatedKeys = Sql.createGeneratedKeysOfInt();
  }

  @Test
  @Override
  public void addIf01() {
    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) values (?)");

          trx.with(generatedKeys);

          trx.addIf("abc", true);
        },

        """
        The 'addIf' operation cannot be executed on a SQL statement returning generated keys.
        """
    );
  }

  @Test
  public void addIf02() {
    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) values (?)");

          trx.with(generatedKeys);

          trx.add("FOO");

          trx.addIf("abc", true);
        },

        """
        The 'addIf' operation cannot be executed on a SQL statement returning generated keys.
        """
    );
  }

  @Test
  public void addIf03() {
    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) values (?)");

          trx.with(generatedKeys);

          trx.add("FOO");

          trx.addBatch();

          trx.addIf("abc", true);
        },

        """
        The 'addIf' operation cannot be executed on a SQL statement returning generated keys.
        """
    );
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

              trx.add(123);

              trx.add("bar");

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
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setInt(1, 123)
        setString(2, bar)
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

              trx.add(1);

              trx.addBatch();

              trx.add(2);

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
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setInt(1, 1)
        addBatch()
        setInt(1, 2)
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

          trx.add(1);

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
  public void querySingleInt01() {
    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) values (123)");

          trx.with(generatedKeys);

          trx.querySingleInt();
        },

        """
        The 'querySingleInt' operation cannot be executed on a SQL statement returning generated keys.
        """
    );
  }

  @Test
  public void querySingleInt02() {
    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) select A from FOO where X = ?");

          trx.with(generatedKeys);

          trx.add(123);

          trx.querySingleInt();
        },

        """
        The 'querySingleInt' operation cannot be executed on a SQL statement returning generated keys.
        """
    );
  }

  @Test
  public void querySingleInt03() {
    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) select A from FOO where X = ?");

          trx.with(generatedKeys);

          trx.add(123);

          trx.addBatch();

          trx.querySingleInt();
        },

        """
        The 'querySingleInt' operation cannot be executed on a SQL statement returning generated keys.
        """
    );
  }

  @Test(description = """
  update
  - happy path
  - prepared
  """)
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

              trx.add(123);

              trx.add("bar");

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
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setInt(1, 123)
        setString(2, bar)
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

  @Test(description = """
  update + generated keys
  - happy path
  - statement
  """)
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
        setAutoCommit(true)
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

}