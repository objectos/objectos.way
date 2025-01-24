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
import org.testng.annotations.Test;

public class SqlTransactionTestUpdate extends SqlTransactionTestSupport {

  @Override
  public void addIf01() {
    // TODO
  }

  @Override
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

  @Override
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

  @Override
  public void querySingleInt01() {}

}