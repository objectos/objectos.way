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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.testng.annotations.Test;

public class MysqlSqlTransactionTest {

  @Test
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

    try (SqlTransaction trx = new MysqlSqlTransaction(conn)) {
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
  
  private void row(ResultSet rs) throws SQLException {
    // noop
  }
  
  private Page page(int pageSize) {
    return new TestingPage(1, pageSize);
  }
  
}