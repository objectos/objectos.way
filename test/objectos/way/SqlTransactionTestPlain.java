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
import static org.testng.Assert.assertTrue;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SqlTransactionTestPlain extends SqlTransactionTestSupport {

  @Test
  @Override
  public void addBatch01() {
    invalidOperation(
        trx -> {
          trx.sql("insert into FOO (X) values (123)");

          trx.addBatch();
        },

        """
        The 'addBatch' operation cannot be executed on a plain SQL statement with no parameters values set.
        """
    );
  }

  @Test
  @Override
  public void addIf01() {
    String expectedMessage = """
    The 'addIf' operation cannot be executed on a plain SQL statement.
    """;

    invalidOperation(
        trx -> {
          trx.sql("select T from FOO where X = ?");

          trx.addIf("BAR", true);
        },

        expectedMessage
    );

    invalidOperation(
        trx -> {
          trx.sql("select T from FOO where X = ?");

          trx.add(123);

          trx.addIf("BAR", true);
        },

        expectedMessage
    );
  }

  @Test
  @Override
  public final void addNullable01() {
    assertEquals(
        updatePrepared(
            List.of(),

            updates(1),

            trx -> {
              trx.sql("insert into BAR (X, Y) values (?, ?)");

              trx.add(1);

              trx.add(null, Types.DATE);

              return trx.update();
            }
        ),

        1
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X, Y) values (?, ?), 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setInt(1, 1)
        setNull(2, 91)
        executeUpdate()
        close()
        """
    );

    assertEmpty(statement);

    assertEmpty(resultSet);
  }

  @Test
  @Override
  public void batchUpdate01() {
    String expectedMessage = """
    The 'batchUpdate' operation cannot be executed on a plain SQL statement with no batches defined.
    """;

    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) values (?)");

          trx.batchUpdate();
        },

        expectedMessage
    );

    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) values (?)");

          trx.add(123);

          trx.batchUpdate();
        },

        expectedMessage
    );
  }

  @Test
  @Override
  public final void batchUpdateWithResult01() {
    String expectedMessage = """
    The 'batchUpdate' operation cannot be executed on a plain SQL statement with no batches defined.
    """;

    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) values (?)");

          trx.batchUpdateWithResult();
        },

        expectedMessage
    );

    invalidOperation(
        trx -> {
          trx.sql("insert into BAR (X) values (?)");

          trx.add(123);

          trx.batchUpdateWithResult();
        },

        expectedMessage
    );
  }

  @Test
  @Override
  public void close01() {
    preparedStatement(
        List.of(),

        trx -> {
          trx.sql("select * from FOO where X = ?");

          return 0;
        }
    );

    assertEquals(
        connection.toString(),

        """
        setAutoCommit(true)
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
        List.of(),

        trx -> {
          trx.sql("select * from FOO where X = ?");

          trx.add(123);

          return 0;
        }
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select * from FOO where X = ?, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setInt(1, 123)
        close()
        """
    );

    assertEmpty(resultSet);

    assertEmpty(statement);
  }

  @Override
  @Test(description = "trx.sql(sql).args(args).query(Record::new)")
  public void query01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("1", 567, "2", "BAR", "3", LocalDate.of(2024, 12, 1))
    );

    stmt.queries(query);

    conn.preparedStatements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("select A, B, C from FOO where X = ?");

      trx.add(123);

      List<Foo> result;
      result = trx.query(Foo::new);

      assertEquals(result.size(), 1);
      assertEquals(result.get(0), new Foo(567, "BAR", LocalDate.of(2024, 12, 1)));
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ?, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        stmt.toString(),

        """
        setInt(1, 123)
        executeQuery()
        close()
        """
    );

    assertEquals(
        query.toString(),

        """
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );
  }

  @Test(description = "trx.sql(sql).query(Record::new)")
  public void query02() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingStatement stmt;
    stmt = new TestingStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("1", 567, "2", "BAR", "3", LocalDate.of(2024, 12, 1))
    );

    stmt.queries(query);

    conn.statements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("select A, B, C from FOO");

      List<Foo> result;
      result = trx.query(Foo::new);

      assertEquals(result.size(), 1);
      assertEquals(result.get(0), new Foo(567, "BAR", LocalDate.of(2024, 12, 1)));
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
        executeQuery(select A, B, C from FOO)
        close()
        """
    );

    assertEquals(
        query.toString(),

        """
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );
  }

  @Test(description = "query after a count with same arguments")
  public void query03() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt0;
    stmt0 = new TestingPreparedStatement();

    TestingResultSet query0;
    query0 = new TestingResultSet(
        Map.of("1", 2)
    );

    stmt0.queries(query0);

    TestingPreparedStatement stmt1;
    stmt1 = new TestingPreparedStatement();

    TestingResultSet query1;
    query1 = new TestingResultSet(
        Map.of("1", 1, "2", "FOO", "3", LocalDate.of(2020, 12, 1)),
        Map.of("1", 2, "2", "BAR", "3", LocalDate.of(2024, 12, 1))
    );

    stmt1.queries(query1);

    conn.preparedStatements(stmt0, stmt1);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql(Sql.Kind.COUNT, "select A from FOO where X = ? and Y = ?");

      trx.add(456);

      trx.add("XPTO");

      int count;
      count = trx.querySingleInt();

      assertEquals(count, 2);

      trx.sql("select A from FOO where X = ? and Y = ?");

      trx.add(456);

      trx.add("XPTO");

      List<Foo> rows;
      rows = trx.query(Foo::new);

      assertEquals(rows.size(), 2);
      assertEquals(rows.get(0), new Foo(1, "FOO", LocalDate.of(2020, 12, 1)));
      assertEquals(rows.get(1), new Foo(2, "BAR", LocalDate.of(2024, 12, 1)));
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select count(*) from ( select A from FOO where X = ? and Y = ? ) x, 2)
        prepareStatement(select A from FOO where X = ? and Y = ?, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        stmt0.toString(),

        """
        setInt(1, 456)
        setString(2, XPTO)
        executeQuery()
        close()
        """
    );
    assertEquals(
        stmt1.toString(),

        """
        setInt(1, 456)
        setString(2, XPTO)
        executeQuery()
        close()
        """
    );

    assertEquals(
        query0.toString(),

        """
        next()
        getInt(1)
        next()
        close()
        """
    );

    assertEquals(
        query1.toString(),

        """
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );
  }

  @Test(description = "subsequent query must not use the arguments of previous query")
  public void query04() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt0;
    stmt0 = new TestingPreparedStatement();

    TestingResultSet query0;
    query0 = new TestingResultSet(
        Map.of("1", 1, "2", "FOO", "3", LocalDate.of(2020, 12, 1))
    );

    stmt0.queries(query0);

    TestingPreparedStatement stmt1;
    stmt1 = new TestingPreparedStatement();

    TestingResultSet query1;
    query1 = new TestingResultSet(
        Map.of("1", 2, "2", "BAR", "3", LocalDate.of(2024, 12, 1))
    );

    stmt1.queries(query1);

    conn.preparedStatements(stmt0, stmt1);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("select A from FOO where X = ?");

      trx.add(456);

      List<Foo> rows;
      rows = trx.query(Foo::new);

      assertEquals(rows.size(), 1);
      assertEquals(rows.get(0), new Foo(1, "FOO", LocalDate.of(2020, 12, 1)));

      trx.sql("select B from FOO where Y = ?");

      trx.add("XPTO");

      rows = trx.query(Foo::new);

      assertEquals(rows.get(0), new Foo(2, "BAR", LocalDate.of(2024, 12, 1)));
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        prepareStatement(select B from FOO where Y = ?, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        stmt0.toString(),

        """
        setInt(1, 456)
        executeQuery()
        close()
        """
    );
    assertEquals(
        stmt1.toString(),

        """
        setString(1, XPTO)
        executeQuery()
        close()
        """
    );

    assertEquals(
        query0.toString(),

        """
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );
    assertEquals(
        query1.toString(),

        """
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );
  }

  @Test
  @Override
  public void queryOptional01() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", 1, "2", "FOO", "3", LocalDate.of(2020, 12, 1))
            ),

            trx -> {
              trx.sql("select * from FOO where X = ?");

              trx.add(123);

              return trx.queryOptional(Foo::new);
            }
        ),

        Optional.of(
            new Foo(1, "FOO", LocalDate.of(2020, 12, 1))
        )
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select * from FOO where X = ?, 2)
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
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void queryOptional02() {
    try {
      preparedStatement(
          List.of(
              Map.of("1", 1, "2", "FOO", "3", LocalDate.of(2020, 12, 1)),
              Map.of("1", 2, "2", "BAR", "3", LocalDate.of(2024, 12, 1))
          ),

          trx -> {
            trx.sql("select * from FOO where X = ?");

            trx.add(123);

            return trx.queryOptional(Foo::new);
          }
      );

      Assert.fail("It should have thrown Sql.TooManyRowsException");
    } catch (Sql.TooManyRowsException expected) {

    }

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select * from FOO where X = ?, 2)
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
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void queryOptional03() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql("select * from FOO where X = ?");

              trx.add(123);

              return trx.queryOptional(Foo::new);
            }
        ),

        Optional.empty()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select * from FOO where X = ?, 2)
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
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void queryOptional04() {
    assertEquals(
        statement(
            List.of(
                Map.of("1", 1, "2", "FOO", "3", LocalDate.of(2020, 12, 1))
            ),

            trx -> {
              trx.sql("select * from FOO");

              return trx.queryOptional(Foo::new);
            }
        ),

        Optional.of(
            new Foo(1, "FOO", LocalDate.of(2020, 12, 1))
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
        executeQuery(select * from FOO)
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );

    assertEmpty(preparedStatement);
  }

  @Test
  public void queryOptional05() {
    try {
      statement(
          List.of(
              Map.of("1", 1, "2", "FOO", "3", LocalDate.of(2020, 12, 1)),
              Map.of("1", 2, "2", "BAR", "3", LocalDate.of(2024, 12, 1))
          ),

          trx -> {
            trx.sql("select * from FOO");

            return trx.queryOptional(Foo::new);
          }
      );

      Assert.fail("It should have thrown Sql.TooManyRowsException");
    } catch (Sql.TooManyRowsException expected) {

    }

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
        executeQuery(select * from FOO)
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );

    assertEmpty(preparedStatement);
  }

  @Test
  public void queryOptional06() {
    assertEquals(
        statement(
            List.of(),

            trx -> {
              trx.sql("select * from FOO");

              return trx.queryOptional(Foo::new);
            }
        ),

        Optional.empty()
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
        executeQuery(select * from FOO)
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

    assertEmpty(preparedStatement);
  }

  @Test
  @Override
  public void queryOptionalInt01() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", 23)
            ),

            trx -> {
              trx.sql("select max(SEQ) from FOO where X = ?");

              trx.add(123);

              return trx.queryOptionalInt();
            }
        ),

        OptionalInt.of(23)
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select max(SEQ) from FOO where X = ?, 2)
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
  public void queryOptionalInt02() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql("select max(SEQ) from FOO where X = ?");

              trx.add(123);

              return trx.queryOptionalInt();
            }
        ),

        OptionalInt.empty()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select max(SEQ) from FOO where X = ?, 2)
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
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void queryOptionalInt03() {
    assertEquals(
        statement(
            List.of(
                Map.of("1", 23)
            ),

            trx -> {
              trx.sql("select max(SEQ) from FOO");

              return trx.queryOptionalInt();
            }
        ),

        OptionalInt.of(23)
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
        executeQuery(select max(SEQ) from FOO)
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
  public void queryOptionalInt04() {
    assertEquals(
        statement(
            List.of(),

            trx -> {
              trx.sql("select max(SEQ) from FOO");

              return trx.queryOptionalInt();
            }
        ),

        OptionalInt.empty()
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
        executeQuery(select max(SEQ) from FOO)
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

    assertEmpty(preparedStatement);
  }

  @Test
  @Override
  public void queryOptionalLong01() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", 23L)
            ),

            trx -> {
              trx.sql("select max(SEQ) from FOO where X = ?");

              trx.add(123);

              return trx.queryOptionalLong();
            }
        ),

        OptionalLong.of(23L)
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select max(SEQ) from FOO where X = ?, 2)
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
        getLong(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  @Override
  public void querySingle01() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", 567, "2", "BAR", "3", LocalDate.of(2024, 12, 1))
            ),

            trx -> {
              trx.sql("select A, B, C from FOO where X = ?");

              trx.add(123);

              return trx.querySingle(Foo::new);
            }
        ),

        new Foo(567, "BAR", LocalDate.of(2024, 12, 1))
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ?, 2)
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
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void querySingle02() {
    assertEquals(
        statement(
            List.of(
                Map.of("1", 567, "2", "BAR", "3", LocalDate.of(2024, 12, 1))
            ),

            trx -> {
              trx.sql("select A, B, C from FOO");

              return trx.querySingle(Foo::new);
            }
        ),

        new Foo(567, "BAR", LocalDate.of(2024, 12, 1))
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
        executeQuery(select A, B, C from FOO)
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );

    assertEmpty(preparedStatement);
  }

  @Test
  @Override
  public void querySingleInt01() {
    assertEquals(
        (int) preparedStatement(
            List.of(
                Map.of("1", 567)
            ),

            trx -> {
              trx.sql("select ID from FOO where X = ?");

              trx.add(123);

              return trx.querySingleInt();
            }
        ),

        567
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select ID from FOO where X = ?, 2)
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
  public void querySingleLong01() {
    assertEquals(
        (long) preparedStatement(
            List.of(
                Map.of("1", 567L)
            ),

            trx -> {
              trx.sql("select ID from FOO where X = ?");

              trx.add(123);

              return trx.querySingleLong();
            }
        ),

        567L
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select ID from FOO where X = ?, 2)
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
    assertEquals(
        updateStatement(
            List.of(),

            updates(1),

            trx -> {
              trx.sql("create table TEMP (ID)");

              return trx.update();
            }
        ),

        1
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
        executeUpdate(create table TEMP (ID))
        close()
        """
    );

    assertEmpty(preparedStatement);

    assertEmpty(resultSet);
  }

  @Test
  public void update02() {
    assertEquals(
        updatePrepared(
            List.of(),

            updates(1),

            trx -> {
              trx.sql("insert into BAR (X, Y) values (?, ?)");

              trx.add(1);

              return trx.update();
            }
        ),

        1
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X, Y) values (?, ?), 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setInt(1, 1)
        executeUpdate()
        close()
        """
    );

    assertEmpty(statement);

    assertEmpty(resultSet);
  }

  @Test
  public void updateWithResult01() {
    assertEquals(
        updatePrepared(
            List.of(),

            updates(1),

            trx -> {
              trx.sql("insert into BAR (X, Y) values (?, ?)");

              trx.add(1);

              assertEquals(trx.updateWithResult(), new SqlUpdateSuccess(1));

              return 0;
            }
        ),

        0
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X, Y) values (?, ?), 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setInt(1, 1)
        executeUpdate()
        close()
        """
    );

    assertEmpty(statement);

    assertEmpty(resultSet);
  }

  @Test
  public void updateWithResult02() {
    final SQLIntegrityConstraintViolationException error;
    error = new SQLIntegrityConstraintViolationException();

    final Sql.Update update;
    update = updatePrepared(
        error,

        trx -> {
          trx.sql("insert into BAR (X, Y) values (?, ?)");

          trx.add(123);

          trx.add("foo");

          return trx.updateWithResult();
        }
    );

    assertTrue(update instanceof Sql.UpdateFailed);

    assertEquals(
        connection.toString(),

        """
        prepareStatement(insert into BAR (X, Y) values (?, ?), 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setInt(1, 123)
        setString(2, foo)
        executeUpdate()
        close()
        """
    );

    assertEmpty(statement);

    assertEmpty(resultSet);
  }

}