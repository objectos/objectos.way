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
package objectos.sql;

import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.testng.annotations.Test;

public class WaySqlTransactionTest {

  @Test(description = """
  SQL template without optional fragments
  """)
  public void testCase01() throws SQLException {
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
      trx.queryPage("""
      select A, B
      from FOO
      where C = ?
      """, this::row, page(15), 123);
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
  public void testCase02() throws SQLException {
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
      trx.queryPage("""
      select A, B
      from FOO
      where C = ?
      --
      and D = ?
      --
      """, this::row, page(15), 123, null);
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
  public void testCase03() throws SQLException {
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
      trx.queryPage("""
      select A, B
      from FOO
      where C = ?
      --
      and D = ?
      --
      """, this::row, page(15), 123, "abc");
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
  public void testCase04() throws SQLException {
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
      trx.queryPage("""
      select A, B
      from FOO
      where C = ?
      """, this::row, new TestingPage(2, 15), 123);
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
  public void testCase05() throws SQLException {
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
      trx.queryPage("""
      select A, B
      from FOO
      where C = ?
      --
      and D is not null
      --
      and E = ?
      """, this::row, page(15), 123, null);
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
  
  private SqlTransaction trx(Connection connection) {
    Dialect dialect;
    dialect = new Dialect();
    
    return new WaySqlTransaction(dialect, connection);
  }

  private void row(ResultSet rs) throws SQLException {
    // noop
  }
  
  private Page page(int pageSize) {
    return new TestingPage(1, pageSize);
  }
  
}