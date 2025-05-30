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
import java.sql.SQLException;
import java.time.LocalDate;
import objectos.way.Sql.RollbackWrapperException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SqlSessionTest {

  @Test(description = "sql.sql().format()")
  public void format01() {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingStatement stmt;
    stmt = new TestingStatement();

    stmt.updates(1);

    conn.statements(stmt);

    SqlSession sql;
    sql = sql(conn);

    try {
      sql.sql("insert into BAR (X, Y) values (%1$d, '%2$s')");

      sql.format(123, LocalDate.of(2024, 9, 26));

      int result;
      result = sql.update();

      assertEquals(result, 1);
    } finally {
      sql.close();
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
  public void rollback01() {
    TestingConnection conn;
    conn = new TestingConnection();

    SqlSession sql;
    sql = sql(conn);

    try {
      sql.rollback();
    } finally {
      sql.close();
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

    SqlSession sql;
    sql = sql(conn);

    try {
      sql.rollback();

      Assert.fail();
    } catch (Sql.DatabaseException e) {
      Throwable cause;
      cause = e.getCause();

      assertSame(cause, exception);
    } finally {
      sql.close();
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

    SqlSession sql;
    sql = sql(conn);

    try {

      throw cause;

    } catch (Throwable t) {
      Throwable result;
      result = sql.rollbackAndSuppress(t);

      assertSame(result, cause);

      Throwable[] suppressed;
      suppressed = result.getSuppressed();

      assertEquals(suppressed.length, 0);
    } finally {
      sql.close();
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

    SqlSession sql;
    sql = sql(conn);

    try {

      throw cause;

    } catch (Throwable t) {
      Throwable result;
      result = sql.rollbackAndSuppress(t);

      assertSame(result, cause);

      Throwable[] suppressed;
      suppressed = result.getSuppressed();

      assertEquals(suppressed.length, 1);
      assertSame(suppressed[0], rollback);
    } finally {
      sql.close();
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

    SqlSession sql;
    sql = sql(conn);

    try {

      throw cause;

    } catch (Throwable t) {
      RollbackWrapperException result;
      result = sql.rollbackAndWrap(t);

      assertSame(result.getCause(), cause);

      Throwable[] suppressed;
      suppressed = result.getSuppressed();

      assertEquals(suppressed.length, 0);
    } finally {
      sql.close();
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

    SqlSession sql;
    sql = sql(conn);

    try {

      throw cause;

    } catch (Throwable t) {
      RollbackWrapperException result;
      result = sql.rollbackAndWrap(t);

      assertSame(result.getCause(), cause);

      Throwable[] suppressed;
      suppressed = result.getSuppressed();

      assertEquals(suppressed.length, 1);
      assertSame(suppressed[0], rollback);
    } finally {
      sql.close();
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

  private SqlSession sql(TestingConnection connection) {
    TestingDatabaseMetaData metaData;
    metaData = TestingDatabaseMetaData.TESTING;

    SqlDialect dialect;
    dialect = metaData.toSqlDialect();

    return new SqlSession(dialect, connection);
  }

}