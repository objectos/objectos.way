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

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import org.testng.annotations.BeforeMethod;

public abstract class SqlTransactionTestSupport {

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

  final SqlTransaction trx(Connection connection) {
    SqlDialect dialect;
    dialect = SqlDialect.TESTING;

    return new SqlTransaction(dialect, connection);
  }

}
