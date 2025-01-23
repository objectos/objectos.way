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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class SqlTransactionTestUpdate extends SqlTransactionTestSupport {

  @Test(description = """
  batchUpdate:
  - happy path
  - prepared
  - no generated keys
  - 1 batch
  """)
  public void batchUpdate01() {
    // TODO
  }

  @Test(description = """
  batchUpdate:
  - happy path
  - prepared
  - no generated keys
  - 2 batches
  """)
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
        close()
        """
    );

    assertEmpty(statement);

    assertEmpty(resultSet);
  }

  @Test(description = """
  batchUpdate:
  - unhappy path
  - prepared
  - no generated keys
  - 0 batches
  """)
  public void batchUpdate03() {
    // TODO
  }

  @Test(description = "trx.sql().format()")
  public void format01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingStatement stmt;
    stmt = new TestingStatement();

    stmt.updates(1);

    conn.statements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("insert into BAR (X, Y) values (%1$d, '%2$s')");

      trx.format(123, LocalDate.of(2024, 9, 26));

      int result;
      result = trx.update();

      assertEquals(result, 1);
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        createStatement()
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        stmt.toString(),

        """
        executeUpdate(insert into BAR (X, Y) values (123, '2024-09-26'))
        close()
        """
    );
  }

  @Test
  public void sqlScript01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingStatement stmt;
    stmt = new TestingStatement();

    stmt.batches(new int[] {1});

    conn.statements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql(Sql.Kind.SCRIPT, """
      insert into FOO (A, B) values (1, 5)
      """);

      int[] result;
      result = trx.batchUpdate();

      assertEquals(result, new int[] {1});
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        createStatement()
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        stmt.toString(),

        """
        addBatch(insert into FOO (A, B) values (1, 5))
        executeBatch()
        close()
        """
    );
  }

  @Test
  public void sqlScript02() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingStatement stmt;
    stmt = new TestingStatement();

    stmt.batches(new int[] {1});

    conn.statements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql(Sql.Kind.SCRIPT, """
      insert into FOO (A, B) values (1, 5)

      insert into BAR (X, Y) values ('A', 'B')
      """);

      int[] result;
      result = trx.batchUpdate();

      assertEquals(result, new int[] {1});
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        createStatement()
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        stmt.toString(),

        """
        addBatch(insert into FOO (A, B) values (1, 5))
        addBatch(insert into BAR (X, Y) values ('A', 'B'))
        executeBatch()
        close()
        """
    );
  }

  @Test(description = "trx.sql(sql).update()")
  public void update01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingStatement stmt;
    stmt = new TestingStatement();

    stmt.updates(1);

    conn.statements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("create table TEMP (ID)");

      int result;
      result = trx.update();

      assertEquals(result, 1);
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        createStatement()
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        stmt.toString(),

        """
        executeUpdate(create table TEMP (ID))
        close()
        """
    );
  }

  @Test(description = "trx.add(null, Types.DATE)")
  public void update02() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    stmt.updates(1);

    conn.preparedStatements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("insert into BAR (X, Y) values (?, ?)");

      trx.add(1);

      trx.add(null, Types.DATE);

      int result;
      result = trx.update();

      assertEquals(result, 1);
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(insert into BAR (X, Y) values (?, ?), 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        stmt.toString(),

        """
        setInt(1, 1)
        setNull(2, 91)
        executeUpdate()
        close()
        """
    );
  }

  @Test(description = """
  update + generated keys
  - happy path
  - statement
  """)
  public void updateGeneratedKeys01() {
    final Sql.GeneratedKeys.OfInt generatedKeys;
    generatedKeys = Sql.createGeneratedKeysOfInt();

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

  @Test(description = """
  update + generated keys
  - happy path
  - prepared
  """)
  public void updateGeneratedKeys02() {
    final Sql.GeneratedKeys.OfInt generatedKeys;
    generatedKeys = Sql.createGeneratedKeysOfInt();

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

}