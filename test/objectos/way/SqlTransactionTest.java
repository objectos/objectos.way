/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SqlTransactionTest {

  @Test
  public void batchUpdate01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    stmt.batches(new int[] {1, 1});

    conn.statements(stmt);

    try (SqlTransaction trx = trx(conn)) {
      int[] result;
      result = trx.batchUpdate(
          "insert into FOO (FOO_ID, FOO_NAME, BAR) VALUES (?, ?, ?)",
          trx.values(1, "A", true),
          trx.values(2, "B", false)
      );

      assertEquals(result, new int[] {1, 1});
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(insert into FOO (FOO_ID, FOO_NAME, BAR) VALUES (?, ?, ?))
        close()
        """
    );

    assertEquals(
        stmt.toString(),

        """
        setInt(1, 1)
        setString(2, A)
        setBoolean(3, true)
        addBatch()
        setInt(1, 2)
        setString(2, B)
        setBoolean(3, false)
        addBatch()
        executeBatch()
        close()
        """
    );
  }

  @Test
  public void count01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("1", "567")
    );

    stmt.queries(query);

    conn.statements(stmt);

    try (SqlTransaction trx = trx(conn)) {
      int count;
      count = trx.count("""
      select A, B
      from FOO
      where C = ?
      """, 123);

      assertEquals(count, 567);
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select count(*) from ( select A, B from FOO where C = ? ) x)
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
        close()
        """
    );
  }

  @Test(description = """
  No optional fragments
  """)
  public void processAll01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    TestingResultSet query;
    query = new TestingResultSet();

    stmt.queries(query);

    conn.statements(stmt);

    try (SqlTransaction trx = trx(conn)) {
      trx.processQuery(this::row, """
      select A, B
      from FOO
      where ID = ?
      """, 123);
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select A, B from FOO where ID = ?)
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
        close()
        """
    );
  }

  @Test(description = """
  SQL template without optional fragments
  """)
  public void processSinglePage01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("A", "Hello", "B", "World!")
    );

    stmt.queries(query);

    conn.statements(stmt);

    try (SqlTransaction trx = trx(conn)) {
      trx.processQuery(this::row, page(15), """
      select A, B
      from FOO
      where C = ?
      """, 123);
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select A, B from FOO where C = ? limit 15)
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
        next()
        close()
        """
    );
  }

  @Test(description = """
  SQL template with 1 optional fragment
  => fragment removed
  """)
  public void processSinglePage02() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("A", "Hello", "B", "World!")
    );

    stmt.queries(query);

    conn.statements(stmt);

    try (SqlTransaction trx = trx(conn)) {
      trx.processQuery(this::row, page(15), """
      select A, B
      from FOO
      where C = ?
      --
      and D = ?
      --
      """, 123, null);
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select A, B from FOO where C = ? limit 15)
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
        next()
        close()
        """
    );
  }

  @Test(description = """
  SQL template with 1 optional fragment
  => fragment included
  """)
  public void processSinglePage03() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("A", "Hello", "B", "World!")
    );

    stmt.queries(query);

    conn.statements(stmt);

    try (SqlTransaction trx = trx(conn)) {
      trx.processQuery(this::row, page(15), """
      select A, B
      from FOO
      where C = ?
      --
      and D = ?
      --
      """, 123, "abc");
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select A, B from FOO where C = ? and D = ? limit 15)
        close()
        """
    );

    assertEquals(
        stmt.toString(),

        """
        setInt(1, 123)
        setString(2, abc)
        executeQuery()
        close()
        """
    );

    assertEquals(
        query.toString(),

        """
        next()
        next()
        close()
        """
    );
  }

  @Test(description = """
  SQL template pagination
  """)
  public void processSinglePage04() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("A", "Hello", "B", "World!")
    );

    stmt.queries(query);

    conn.statements(stmt);

    try (SqlTransaction trx = trx(conn)) {
      trx.processQuery(this::row, Sql.createPage(2, 15), """
      select A, B
      from FOO
      where C = ?
      """, 123);
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select A, B from FOO where C = ? limit 15 offset 15)
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
        next()
        close()
        """
    );
  }

  @Test(description = """
  SQL template with two fragments
  """)
  public void processSinglePage05() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    TestingResultSet query;
    query = new TestingResultSet(
        Map.of("A", "Hello", "B", "World!")
    );

    stmt.queries(query);

    conn.statements(stmt);

    try (SqlTransaction trx = trx(conn)) {
      trx.processQuery(this::row, page(15), """
      select A, B
      from FOO
      where C = ?
      --
      and D is not null
      --
      and E = ?
      """, 123, null);
    }

    assertEquals(
        conn.toString(),

        """
        prepareStatement(select A, B from FOO where C = ? and D is not null limit 15)
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
        next()
        close()
        """
    );
  }

  @Test
  public void rollback01() {
    TestingConnection conn;
    conn = new TestingConnection();

    try (SqlTransaction trx = trx(conn)) {
      trx.rollback();
    }

    assertEquals(
        conn.toString(),

        """
        rollback()
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

    try (SqlTransaction trx = trx(conn)) {
      trx.rollback();

      Assert.fail();
    } catch (Sql.UncheckedSqlException e) {
      SQLException cause;
      cause = e.getCause();

      assertSame(cause, exception);
    }

    assertEquals(
        conn.toString(),

        """
        rollback()
        close()
        """
    );
  }

  @Test(description = """
  rethrow is java.lang.Error
  """)
  public void rollbackAndRethrow01() {
    TestingConnection conn;
    conn = new TestingConnection();

    Throwable rethrow;
    rethrow = new Error();

    try (SqlTransaction trx = trx(conn)) {
      trx.rollbackAndRethrow(rethrow);
    } catch (Error expected) {
      assertSame(expected, rethrow);
    } catch (Throwable e) {
      Assert.fail("Not an Error", e);
    }

    assertEquals(
        conn.toString(),

        """
        rollback()
        close()
        """
    );
  }

  @Test(description = """
  rethrow is java.lang.RuntimeException
  """)
  public void rollbackAndRethrow02() {
    TestingConnection conn;
    conn = new TestingConnection();

    Throwable rethrow;
    rethrow = new IllegalArgumentException();

    try (SqlTransaction trx = trx(conn)) {
      trx.rollbackAndRethrow(rethrow);
    } catch (RuntimeException expected) {
      assertSame(expected, rethrow);
    } catch (Throwable e) {
      Assert.fail("Not an RuntimeException", e);
    }

    assertEquals(
        conn.toString(),

        """
        rollback()
        close()
        """
    );
  }

  @Test(description = """
  rethrow is checked
  """)
  public void rollbackAndRethrow03() {
    TestingConnection conn;
    conn = new TestingConnection();

    Throwable rethrow;
    rethrow = new SQLException();

    try (SqlTransaction trx = trx(conn)) {
      trx.rollbackAndRethrow(rethrow);
    } catch (RuntimeException expected) {
      Throwable cause;
      cause = expected.getCause();

      assertSame(cause, rethrow);
    } catch (Throwable e) {
      Assert.fail("Not an RuntimeException", e);
    }

    assertEquals(
        conn.toString(),

        """
        rollback()
        close()
        """
    );
  }

  private SqlTransaction trx(Connection connection) {
    SqlDialect dialect;
    dialect = TestingDatabaseMetaData.MYSQL_5_7.toSqlDialect();

    return new SqlTransaction(dialect, connection);
  }

  private void row(ResultSet rs) throws SQLException {
    while (rs.next()) {
      noop();
    }
  }

  private void noop() {}

  private Sql.Page page(int pageSize) {
    return Sql.createPage(1, pageSize);
  }

}