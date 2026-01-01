/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import objectos.way.Sql.InvalidOperationException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

public abstract class SqlTransactionTestSupport {

  record Foo(Integer a, String b, LocalDate c) {
    Foo(ResultSet rs, int idx) throws SQLException {
      this(rs.getInt(idx++), rs.getString(idx++), rs.getObject(idx++, LocalDate.class));
    }
  }

  record String2(String a, String b) {
    String2(ResultSet rs, int idx) throws SQLException {
      this(rs.getString(idx++), rs.getString(idx++));
    }
  }

  TestingConnection connection;

  TestingPreparedStatement preparedStatement;

  TestingStatement statement;

  TestingResultSet resultSet;

  @BeforeMethod
  public void reset() {
    connection = null;

    preparedStatement = null;

    statement = null;

    resultSet = null;
  }

  public abstract void addBatch01();

  public abstract void addIf01();

  public abstract void addNullable01();

  public abstract void batchUpdate01();

  public abstract void batchUpdateWithResult01();

  public abstract void close01();

  public abstract void query01();

  public abstract void queryOptional01();

  public abstract void queryOptionalInt01();

  public abstract void queryOptionalLong01();

  public abstract void querySingle01();

  public abstract void querySingleInt01();

  public abstract void querySingleLong01();

  public abstract void update01();

  final void assertEmpty(Object o) {
    if (o != null) {
      assertEquals(o.toString(), "");
    }
  }

  final int[] batch(int... batch) {
    return batch;
  }

  final int[][] batches(int[]... batches) {
    return batches;
  }

  final int[] updates(int... updates) {
    return updates;
  }

  final int[] batchPrepared(List<Map<String, Object>> rows, int[][] batches, Function<SqlTransaction, int[]> trxConfig) {
    connection = new TestingConnection();

    preparedStatement = new TestingPreparedStatement();

    resultSet = new TestingResultSet(rows);

    preparedStatement.batches(batches);

    preparedStatement.generatedKeys(resultSet);

    connection.preparedStatements(preparedStatement);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      return trxConfig.apply(trx);
    } finally {
      trx.close();
    }
  }

  final int[] batchStatement(List<Map<String, Object>> rows, int[][] batches, Function<SqlTransaction, int[]> trxConfig) {
    connection = new TestingConnection();

    statement = new TestingStatement();

    resultSet = new TestingResultSet(rows);

    statement.batches(batches);

    statement.generatedKeys(resultSet);

    connection.statements(statement);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      return trxConfig.apply(trx);
    } finally {
      trx.close();
    }
  }

  final int updatePrepared(List<Map<String, Object>> rows, int[] updates, ToIntFunction<SqlTransaction> trxConfig) {
    connection = new TestingConnection();

    preparedStatement = new TestingPreparedStatement();

    resultSet = new TestingResultSet(rows);

    preparedStatement.generatedKeys(resultSet);

    preparedStatement.updates(updates);

    connection.preparedStatements(preparedStatement);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      return trxConfig.applyAsInt(trx);
    } finally {
      trx.close();
    }
  }

  final Sql.Update updatePrepared(SQLException error, Function<SqlTransaction, Sql.Update> trxConfig) {
    connection = new TestingConnection();

    preparedStatement = new TestingPreparedStatement();

    preparedStatement.updateError(error);

    connection.preparedStatements(preparedStatement);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      return trxConfig.apply(trx);
    } finally {
      trx.close();
    }
  }

  final int updateStatement(List<Map<String, Object>> rows, int[] updates, ToIntFunction<SqlTransaction> trxConfig) {
    connection = new TestingConnection();

    statement = new TestingStatement();

    resultSet = new TestingResultSet(rows);

    statement.generatedKeys(resultSet);

    statement.updates(updates);

    connection.statements(statement);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      return trxConfig.applyAsInt(trx);
    } finally {
      trx.close();
    }
  }

  final <T> T preparedStatement(List<Map<String, Object>> rows, Function<SqlTransaction, T> trxConfig) {
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

  final <T> T statement(List<Map<String, Object>> rows, Function<SqlTransaction, T> trxConfig) {
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

  final void iae(Consumer<SqlTransaction> trxConfig, String expectedMessage) {
    connection = new TestingConnection();

    statement = new TestingStatement();

    connection.statements(statement);

    preparedStatement = new TestingPreparedStatement();

    connection.preparedStatements(preparedStatement);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      trxConfig.accept(trx);

      Assert.fail("It should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), expectedMessage);
    } finally {
      trx.close();
    }
  }

  final void ise(Consumer<SqlTransaction> trxConfig, String expectedMessage) {
    connection = new TestingConnection();

    statement = new TestingStatement();

    connection.statements(statement);

    preparedStatement = new TestingPreparedStatement();

    connection.preparedStatements(preparedStatement);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      trxConfig.accept(trx);

      Assert.fail("It should have thrown IllegalArgumentException");
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), expectedMessage);
    } finally {
      trx.close();
    }
  }

  final void invalidOperation(Consumer<SqlTransaction> trxConfig, String expectedMessage) {
    connection = new TestingConnection();

    statement = new TestingStatement();

    connection.statements(statement);

    preparedStatement = new TestingPreparedStatement();

    connection.preparedStatements(preparedStatement);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      trxConfig.accept(trx);

      Assert.fail("It should have thrown InvalidOperationException");
    } catch (InvalidOperationException expected) {
      assertEquals(expected.getMessage(), expectedMessage);
    } finally {
      trx.close();
    }
  }

  final SqlTransaction trx(Connection connection) {
    return SqlTransaction.of(connection);
  }

}
