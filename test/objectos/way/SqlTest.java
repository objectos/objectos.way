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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SqlTest {

  @Test
  public void rollbackAndClose01() {
    TestingConnection connection;
    connection = new TestingConnection();

    SqlTransaction trx;
    trx = trx(connection);

    Sql.rollbackAndClose(trx);

    assertEquals(
        connection.toString(),

        """
        rollback()
        close()
        """
    );
  }

  @Test
  public void rollbackAndClose02() {
    TestingConnection connection;
    connection = new TestingConnection();

    SQLException error;
    error = new SQLException();

    connection.rollbackException(error);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      Sql.rollbackAndClose(trx);

      Assert.fail();
    } catch (Sql.DatabaseException expected) {
      SQLException cause;
      cause = expected.getCause();

      assertSame(cause, error);
    }

    assertEquals(
        connection.toString(),

        """
        rollback()
        close()
        """
    );
  }

  @Test
  public void rollbackAndClose03() {
    TestingConnection connection;
    connection = new TestingConnection();

    SQLException error;
    error = new SQLException();

    connection.closeException(error);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      Sql.rollbackAndClose(trx);

      Assert.fail();
    } catch (Sql.DatabaseException expected) {
      SQLException cause;
      cause = expected.getCause();

      assertSame(cause, error);
    }

    assertEquals(
        connection.toString(),

        """
        rollback()
        close()
        """
    );
  }

  @Test
  public void rollbackAndClose04() {
    TestingConnection connection;
    connection = new TestingConnection();

    SQLException rollback;
    rollback = new SQLException();

    connection.rollbackException(rollback);

    SQLException close;
    close = new SQLException();

    connection.closeException(close);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      Sql.rollbackAndClose(trx);

      Assert.fail();
    } catch (Sql.DatabaseException expected) {
      SQLException cause;
      cause = expected.getCause();

      assertSame(cause, rollback);

      Throwable[] suppressed;
      suppressed = rollback.getSuppressed();

      assertEquals(suppressed.length, 1);
      assertSame(suppressed[0], close);
    }

    assertEquals(
        connection.toString(),

        """
        rollback()
        close()
        """
    );
  }

  @Test
  public void set() throws SQLException {
    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    Sql.set(stmt, 1, "ABC");
    Sql.set(stmt, 2, Boolean.TRUE);
    Sql.set(stmt, 3, Integer.valueOf(123));
    Sql.set(stmt, 4, Long.valueOf(567L));
    Sql.set(stmt, 5, Double.valueOf(4.56));
    Sql.set(stmt, 6, LocalDate.of(2024, 9, 25));
    Sql.set(stmt, 7, LocalDateTime.of(2024, 9, 25, 13, 0));

    assertEquals(
        stmt.toString(),

        """
        setString(1, ABC)
        setBoolean(2, true)
        setInt(3, 123)
        setLong(4, 567)
        setDouble(5, 4.56)
        setObject(6, 2024-09-25)
        setObject(7, 2024-09-25T13:00)
        """
    );
  }

  private SqlTransaction trx(Connection connection) {
    return new SqlTransaction(SqlDialect.TESTING, connection);
  }

}