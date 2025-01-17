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
import static org.testng.Assert.assertSame;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Consumer;
import java.util.function.Function;
import objectos.way.Sql.RollbackWrapperException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SqlTransactionTest {

  private record Foo(Integer a, String b, LocalDate c) {
    Foo(ResultSet rs, int idx) throws SQLException {
      this(rs.getInt(idx++), rs.getString(idx++), rs.getObject(idx++, LocalDate.class));
    }
  }

  private record String2(String a, String b) {
    String2(ResultSet rs, int idx) throws SQLException {
      this(rs.getString(idx++), rs.getString(idx++));
    }
  }

  private TestingConnection connection;

  private TestingPreparedStatement preparedStatement;

  private TestingStatement statement;

  private TestingResultSet resultSet;

  @BeforeMethod
  public void reset() {
    connection = null;

    preparedStatement = null;

    statement = null;

    resultSet = null;
  }

  @Test(description = """
  addIf
  - query
  - value present
  """)
  public void addIf01() {
    addIfTransaction(trx -> {
      trx.sql("""
      select A, B, C from FOO
      --
      where X = ?
      """);

      trx.addIf("SOME", true);
    });

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
        setString(1, SOME)
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
  }

  @Test(description = """
  addIf
  - query
  - value absent
  """)
  public void addIf02() {
    addIfTransaction(trx -> {
      trx.sql("""
      select A, B, C from FOO
      --
      where X = ?
      """);

      trx.addIf("SOME", false);
    });

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
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
  }

  @Test(description = """
  addIf
  - query
  - prelude with placeholders
  - value absent
  """)
  public void addIf03() {
    addIfTransaction(trx -> {
      trx.sql("""
      select A, B, C from FOO
      where X = ?
      --
      and Y = ?
      """);

      trx.add("XPTO");

      trx.addIf("SOME", false);
    });

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
        setString(1, XPTO)
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
  }

  @Test(description = """
  addIf
  - query
  - fragment 1 absent
  - fragment 2 present
  """)
  public void addIf04() {
    addIfTransaction(trx -> {
      trx.sql("""
      select A, B, C from FOO
      where 1 = 1
      --
      and X = ?
      --
      and Y = ?
      """);

      trx.addIf("1", false);

      trx.addIf("2", true);
    });

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where 1 = 1 and Y = ?, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setString(1, 2)
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
  }

  @Test(description = """
  addIf
  - query
  - fragment 1 present
  - fragment 2 absent
  """)
  public void addIf05() {
    addIfTransaction(trx -> {
      trx.sql("""
      select A, B, C from FOO
      where 1 = 1
      --
      and X = ?
      --
      and Y = ?
      """);

      trx.add("1");

      trx.addIf("2", false);
    });

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where 1 = 1 and X = ?, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setString(1, 1)
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
  }

  @Test(
      description = "addIf\n- reject use in a fragment with more than one placeholder",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Conditional value must not be used in a fragment with more than one placeholder: .*")
  public void addIf06() {
    addIfTransaction(trx -> {
      trx.sql("""
      select A, B, C from FOO
      where 1 = 1
      --
      and X = ? and Y = ?
      """);

      trx.add("1");
      trx.addIf("2", false);
    });
  }

  @Test(description = """
  addIf
  - query
  - fragment 1 present
  - fragment 2 no placeholders
  """)
  public void addIf07() {
    addIfTransaction(trx -> {
      trx.sql("""
      select A, B, C from FOO
      --
      where X = ?
      --
      order by C
      """);

      trx.addIf("2", true);
    });

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ? order by C, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setString(1, 2)
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
  }

  @Test(description = """
  addIf
  - query
  - fragment 1 absent
  - fragment 2 present (with > 1 placeholders)
  """)
  public void addIf08() {
    addIfTransaction(trx -> {
      trx.sql("""
      select A, B, C from FOO
      where 1 = 1
      --
      and X = ?
      --
      and Y = ?
      and Z = ?
      """);

      trx.addIf("X", false);
      trx.add("Y");
      trx.add("Z");
    });

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where 1 = 1 and Y = ? and Z = ?, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setString(1, Y)
        setString(2, Z)
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
  }

  @Test(description = """
  addIf
  - query
  - fail if fragment has less than required args
  """, expectedExceptions = IllegalArgumentException.class)
  public void addIf09() {
    addIfTransaction(trx -> {
      trx.sql("""
      select A, B, C from FOO
      where 1 = 1
      --
      and X = ?
      --
      and Y = ?
      and Z = ?
      """);

      trx.addIf("X", false);
      trx.add("Y");
      // missing Z
    });
  }

  @Test(description = """
  addIf
  - query
  - fail if fragment has less than required args
  """, expectedExceptions = IllegalArgumentException.class)
  public void addIf10() {
    addIfTransaction(trx -> {
      trx.sql("""
      select A, B, C from FOO
      where 1 = 1
      --
      and X = ?
      --
      and Y = ?
      """);

      trx.addIf("X", true);
      // missing Y
    });
  }

  private void addIfTransaction(Consumer<SqlTransaction> config) {
    connection = new TestingConnection();

    preparedStatement = new TestingPreparedStatement();

    resultSet = new TestingResultSet();

    preparedStatement.queries(resultSet);

    connection.preparedStatements(preparedStatement);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      config.accept(trx);

      List<Foo> result;
      result = trx.query(Foo::new);

      assertEquals(result.size(), 0);
    } finally {
      trx.close();
    }
  }

  @Test(description = "trx.sql(sql).add(1).addBatch().add(2).addBatch().batchUpdate()")
  public void batchUpdate01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    stmt.batches(new int[] {1, 1});

    conn.preparedStatements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("insert into BAR (X) values (?)");

      trx.add(1);

      trx.addBatch();

      trx.add(2);

      trx.addBatch();

      int[] result;
      result = trx.batchUpdate();

      assertEquals(result, new int[] {1, 1});
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(insert into BAR (X) values (?))
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        stmt.toString(),

        """
        setInt(1, 1)
        addBatch()
        setInt(1, 2)
        addBatch()
        executeBatch()
        close()
        """
    );
  }

  @Test(description = "count without parameters")
  public void count01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingStatement stmt;
    stmt = new TestingStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("1", 23)
    );

    stmt.queries(query);

    conn.statements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("""
      select A, B
      from FOO
      """);

      int count;
      count = trx.count();

      assertEquals(count, 23);
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
        executeQuery(select count(*) from ( select A, B from FOO ) x)
        close()
        """
    );

    assertEquals(
        query.toString(),

        """
        next()
        getInt(1)
        next()
        close()
        """
    );
  }

  @Test(description = "count with parameters")
  public void count02() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("1", 567)
    );

    stmt.queries(query);

    conn.preparedStatements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("""
      select A, B
      from FOO
      where C = ?
      """);

      trx.add(123);

      int count;
      count = trx.count();

      assertEquals(count, 567);
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select count(*) from ( select A, B from FOO where C = ? ) x, 2)
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
        next()
        close()
        """
    );
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

  @Test(description = """
  paginate:
  - prepared statement
  - first page
  """)
  public void paginate01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("1", "Hello", "2", "World!")
    );

    stmt.queries(query);

    conn.preparedStatements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("""
      select A, B
      from FOO
      where C = ?
      """);

      trx.paginate(Sql.Page.of(1, 15));

      trx.add(123);

      List<String2> result;
      result = trx.query(String2::new);

      assertEquals(result.size(), 1);
      assertEquals(result.get(0), new String2("Hello", "World!"));
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select A, B from FOO where C = ? limit 15, 2)
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
        getString(1)
        getString(2)
        next()
        close()
        """
    );
  }

  @Test(description = """
  paginate:
  - prepared statement
  - second page
  """)
  public void paginate02() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("1", "Hello", "2", "World!")
    );

    stmt.queries(query);

    conn.preparedStatements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("""
      select A, B
      from FOO
      where C = ?
      """);

      trx.paginate(Sql.Page.of(2, 15));

      trx.add(123);

      List<String2> result;
      result = trx.query(String2::new);

      assertEquals(result.size(), 1);
      assertEquals(result.get(0), new String2("Hello", "World!"));
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select A, B from FOO where C = ? limit 15 offset 15, 2)
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
        getString(1)
        getString(2)
        next()
        close()
        """
    );
  }

  @Test(description = """
  paginate:
  - regular statement
  - first page
  """)
  public void paginate03() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingStatement stmt;
    stmt = new TestingStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("1", "Hello", "2", "World!")
    );

    stmt.queries(query);

    conn.statements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("""
      select A, B
      from FOO
      """);

      trx.paginate(Sql.Page.of(1, 15));

      List<String2> result;
      result = trx.query(String2::new);

      assertEquals(result.size(), 1);
      assertEquals(result.get(0), new String2("Hello", "World!"));
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
        executeQuery(select A, B from FOO limit 15)
        close()
        """
    );

    assertEquals(
        query.toString(),

        """
        next()
        getString(1)
        getString(2)
        next()
        close()
        """
    );
  }

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
      trx.sql("select A from FOO where X = ? and Y = ?");

      trx.add(456);

      trx.add("XPTO");

      int count;
      count = trx.count();

      assertEquals(count, 2);

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

  @Test(description = """
  queryFirst:
  - prepared statement
  - result with exactly one
  """)
  public void queryFirst01() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", 1, "2", "FOO", "3", LocalDate.of(2020, 12, 1))
            ),

            trx -> {
              trx.sql("select * from FOO where X = ?");

              trx.add(123);

              return trx.queryFirst(Foo::new);
            }
        ),

        new Foo(1, "FOO", LocalDate.of(2020, 12, 1))
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
        close()
        """
    );
  }

  @Test(description = """
  queryFirst:
  - prepared statement
  - result more than one
  """)
  public void queryFirst02() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", 1, "2", "FOO", "3", LocalDate.of(2020, 12, 1)),
                Map.of("1", 2, "2", "BAR", "3", LocalDate.of(2024, 12, 1))
            ),

            trx -> {
              trx.sql("select * from FOO where X = ?");

              trx.add(123);

              return trx.queryFirst(Foo::new);
            }
        ),

        new Foo(1, "FOO", LocalDate.of(2020, 12, 1))
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
        close()
        """
    );
  }

  @Test(description = """
  queryFirst:
  - prepared statement
  - no result
  """)
  public void queryFirst03() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql("select * from FOO where X = ?");

              trx.add(123);

              return trx.queryFirst(Foo::new);
            }
        ),

        null
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
  }

  @Test(description = """
  queryFirst:
  - statement
  - result with exactly one
  """)
  public void queryFirst04() {
    assertEquals(
        statement(
            List.of(
                Map.of("1", 1, "2", "FOO", "3", LocalDate.of(2020, 12, 1))
            ),

            trx -> {
              trx.sql("select * from FOO");

              return trx.queryFirst(Foo::new);
            }
        ),

        new Foo(1, "FOO", LocalDate.of(2020, 12, 1))
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
        close()
        """
    );
  }

  @Test(description = """
  queryFirst:
  - statement
  - result more than one
  """)
  public void queryFirst05() {
    assertEquals(
        statement(
            List.of(
                Map.of("1", 1, "2", "FOO", "3", LocalDate.of(2020, 12, 1)),
                Map.of("1", 2, "2", "BAR", "3", LocalDate.of(2024, 12, 1))
            ),

            trx -> {
              trx.sql("select * from FOO");

              return trx.queryFirst(Foo::new);
            }
        ),

        new Foo(1, "FOO", LocalDate.of(2020, 12, 1))
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
        close()
        """
    );
  }

  @Test(description = """
  queryFirst:
  - statement
  - no result
  """)
  public void queryFirst06() {
    assertEquals(
        statement(
            List.of(),

            trx -> {
              trx.sql("select * from FOO");

              return trx.queryFirst(Foo::new);
            }
        ),

        null
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
  }

  @Test(description = """
  queryFirst:
  - disallow page
  """)
  public void queryFirst07() {
    TestingConnection conn;
    conn = new TestingConnection();

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("select ID from FOO");

      trx.paginate(Sql.Page.of(1, 15));

      trx.queryFirst(Foo::new);

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Cannot paginate a query that is expected to return at most 1 row");
    } finally {
      trx.close();
    }
  }

  @Test(description = """
  queryOptionalInt:
  - prepared statement
  - present
  """)
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
        close()
        """
    );
  }

  @Test(description = """
  queryOptionalInt:
  - prepared statement
  - absent
  """)
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
  }

  @Test(description = """
  queryOptionalInt:
  - statement
  - present
  """)
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
        close()
        """
    );
  }

  @Test(description = """
  queryOptionalInt:
  - statement
  - absent
  """)
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
  }

  @Test(description = """
  queryOptionalInt:
  - disallow page
  """)
  public void queryOptionalInt05() {
    TestingConnection conn;
    conn = new TestingConnection();

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("select ID from FOO");

      trx.paginate(Sql.Page.of(1, 15));

      trx.queryOptionalInt();

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Cannot paginate a query that is expected to return at most 1 row");
    } finally {
      trx.close();
    }
  }

  @Test(description = """
  queryOptionalLong:
  - prepared statement
  - present
  """)
  public void queryOptionalLong01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("1", Long.MAX_VALUE)
    );

    stmt.queries(query);

    conn.preparedStatements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("select max(SEQ) from FOO where X = ?");

      trx.add(123);

      OptionalLong maybe;
      maybe = trx.queryOptionalLong();

      assertEquals(maybe.isPresent(), true);
      assertEquals(maybe.getAsLong(), Long.MAX_VALUE);
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select max(SEQ) from FOO where X = ?, 2)
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
        getLong(1)
        close()
        """
    );
  }

  @Test(description = "trx.sql(sql).args(args).queryOne(Record::new)")
  public void querySingle01() {
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

      Foo result;
      result = trx.querySingle(Foo::new);

      assertEquals(result, new Foo(567, "BAR", LocalDate.of(2024, 12, 1)));
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

  @Test(description = "trx.sql(sql).queryOne(Record::new)")
  public void querySingle02() {
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

      Foo result;
      result = trx.querySingle(Foo::new);

      assertEquals(result, new Foo(567, "BAR", LocalDate.of(2024, 12, 1)));
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

  @Test(description = """
  querySingle:
  - disallow page
  """)
  public void querySingle03() {
    TestingConnection conn;
    conn = new TestingConnection();

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("select A, B, C from FOO");

      trx.paginate(Sql.Page.of(1, 15));

      trx.querySingle(Foo::new);

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Cannot paginate a query that is expected to return at most 1 row");
    } finally {
      trx.close();
    }
  }

  @Test
  public void rollback01() {
    TestingConnection conn;
    conn = new TestingConnection();

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.rollback();
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        rollback()
        setAutoCommit(true)
        close()
        """
    );
  }

  @Test(description = """
  rollback throws
  """)
  public void rollback02() {
    TestingConnection conn;
    conn = new TestingConnection();

    SQLException exception;
    exception = new SQLException();

    conn.rollbackException(exception);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.rollback();

      Assert.fail();
    } catch (Sql.DatabaseException e) {
      SQLException cause;
      cause = e.getCause();

      assertSame(cause, exception);
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        rollback()
        setAutoCommit(true)
        close()
        """
    );
  }

  @Test(description = """
  rollbackAndSuppress: rollback does not throw
  """)
  public void rollbackAndSuppress01() {
    TestingConnection conn;
    conn = new TestingConnection();

    Throwable cause;
    cause = new IOException();

    SqlTransaction trx;
    trx = trx(conn);

    try {

      throw cause;

    } catch (Throwable t) {
      Throwable result;
      result = trx.rollbackAndSuppress(t);

      assertSame(result, cause);

      Throwable[] suppressed;
      suppressed = result.getSuppressed();

      assertEquals(suppressed.length, 0);
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        rollback()
        setAutoCommit(true)
        close()
        """
    );
  }

  @Test(description = """
  rollbackAndSuppress: rollback does throw SQLException
  """)
  public void rollbackAndSuppress02() {
    TestingConnection conn;
    conn = new TestingConnection();

    SQLException rollback;
    rollback = new SQLException();

    conn.rollbackException(rollback);

    Throwable cause;
    cause = new IOException();

    SqlTransaction trx;
    trx = trx(conn);

    try {

      throw cause;

    } catch (Throwable t) {
      Throwable result;
      result = trx.rollbackAndSuppress(t);

      assertSame(result, cause);

      Throwable[] suppressed;
      suppressed = result.getSuppressed();

      assertEquals(suppressed.length, 1);
      assertSame(suppressed[0], rollback);
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        rollback()
        setAutoCommit(true)
        close()
        """
    );
  }

  @Test(description = """
  rollbackAndWrap: rollback does not throw
  """)
  public void rollbackAndWrap01() {
    TestingConnection conn;
    conn = new TestingConnection();

    Throwable cause;
    cause = new IOException();

    SqlTransaction trx;
    trx = trx(conn);

    try {

      throw cause;

    } catch (Throwable t) {
      RollbackWrapperException result;
      result = trx.rollbackAndWrap(t);

      assertSame(result.getCause(), cause);

      Throwable[] suppressed;
      suppressed = result.getSuppressed();

      assertEquals(suppressed.length, 0);
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        rollback()
        setAutoCommit(true)
        close()
        """
    );
  }

  @Test(description = """
  rollbackAndWrap: rollback does throw SQLException
  """)
  public void rollbackAndWrap02() {
    TestingConnection conn;
    conn = new TestingConnection();

    SQLException rollback;
    rollback = new SQLException();

    conn.rollbackException(rollback);

    Throwable cause;
    cause = new IOException();

    SqlTransaction trx;
    trx = trx(conn);

    try {

      throw cause;

    } catch (Throwable t) {
      RollbackWrapperException result;
      result = trx.rollbackAndWrap(t);

      assertSame(result.getCause(), cause);

      Throwable[] suppressed;
      suppressed = result.getSuppressed();

      assertEquals(suppressed.length, 1);
      assertSame(suppressed[0], rollback);
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        rollback()
        setAutoCommit(true)
        close()
        """
    );
  }

  @Test
  public void scriptUpdate01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingStatement stmt;
    stmt = new TestingStatement();

    stmt.batches(new int[] {1});

    conn.statements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("""
      insert into FOO (A, B) values (1, 5)
      """);

      int[] result;
      result = trx.scriptUpdate();

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
  public void scriptUpdate02() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingStatement stmt;
    stmt = new TestingStatement();

    stmt.batches(new int[] {1});

    conn.statements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("""
      insert into FOO (A, B) values (1, 5)

      insert into BAR (X, Y) values ('A', 'B')
      """);

      int[] result;
      result = trx.scriptUpdate();

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

  @Test(description = "trx.sql(insert into FOO values (123, 456)).updateWithGeneratedKeys(keys)")
  public void updateWithGeneratedKeys01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingStatement stmt;
    stmt = new TestingStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("1", 23)
    );

    stmt.generatedKeys(query);

    stmt.updates(1);

    conn.statements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("insert into FOO (A, B) values (123, 'bar')");

      Sql.GeneratedKeys.OfInt generatedKeys;
      generatedKeys = Sql.createGeneratedKeysOfInt();

      int update;
      update = trx.updateWithGeneratedKeys(generatedKeys);

      assertEquals(update, 1);

      assertEquals(generatedKeys.size(), 1);
      assertEquals(generatedKeys.getAsInt(0), 23);
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
        executeUpdate(insert into FOO (A, B) values (123, 'bar'), 1)
        getGeneratedKeys()
        close()
        """
    );

    assertEquals(
        query.toString(),

        """
        next()
        getInt(1)
        next()
        close()
        """
    );
  }

  @Test(description = "trx.sql(insert into FOO values (?)).add(some param).updateWithGeneratedKeys(keys)")
  public void updateWithGeneratedKeys02() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("1", 23)
    );

    stmt.generatedKeys(query);

    stmt.updates(1);

    conn.preparedStatements(stmt);

    SqlTransaction trx;
    trx = trx(conn);

    try {
      trx.sql("insert into FOO (A, B) values (?, ?)");

      trx.add(123).add("bar");

      Sql.GeneratedKeys.OfInt generatedKeys;
      generatedKeys = Sql.createGeneratedKeysOfInt();

      int update;
      update = trx.updateWithGeneratedKeys(generatedKeys);

      assertEquals(update, 1);

      assertEquals(generatedKeys.size(), 1);
      assertEquals(generatedKeys.getAsInt(0), 23);
    } finally {
      trx.close();
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(insert into FOO (A, B) values (?, ?), 1)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        stmt.toString(),

        """
        setInt(1, 123)
        setString(2, bar)
        executeUpdate()
        getGeneratedKeys()
        close()
        """
    );

    assertEquals(
        query.toString(),

        """
        next()
        getInt(1)
        next()
        close()
        """
    );
  }

  private <T> T preparedStatement(List<Map<String, Object>> rows, Function<SqlTransaction, T> trxConfig) {
    connection = new TestingConnection();

    preparedStatement = new TestingPreparedStatement();

    resultSet = new TestingResultSet(rows);

    preparedStatement.queries(resultSet);

    connection.preparedStatements(preparedStatement);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      return trxConfig.apply(trx);
    } finally {
      trx.close();
    }
  }

  private <T> T statement(List<Map<String, Object>> rows, Function<SqlTransaction, T> trxConfig) {
    connection = new TestingConnection();

    statement = new TestingStatement();

    resultSet = new TestingResultSet(rows);

    statement.queries(resultSet);

    connection.statements(statement);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      return trxConfig.apply(trx);
    } finally {
      trx.close();
    }
  }

  private SqlTransaction trx(Connection connection) {
    SqlDialect dialect;
    dialect = SqlDialect.TESTING;

    return new SqlTransaction(dialect, connection);
  }

}