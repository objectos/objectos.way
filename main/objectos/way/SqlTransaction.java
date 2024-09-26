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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import objectos.lang.object.Check;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;
import objectos.way.Sql.RowMapper;
import objectos.way.Sql.Transaction;
import objectos.way.Sql.UncheckedSqlException;

final class SqlTransaction implements Sql.Transaction {

  private final SqlDialect dialect;

  private final Connection connection;

  private String sql;

  private GrowableList<Object> arguments;

  private List<List<Object>> batches;

  SqlTransaction(SqlDialect dialect, Connection connection) {
    this.dialect = dialect;

    this.connection = connection;
  }

  @Override
  public final void commit() throws Sql.UncheckedSqlException {
    try {
      connection.commit();
    } catch (SQLException e) {
      throw new Sql.UncheckedSqlException(e);
    }
  }

  @Override
  public final void rollback() throws Sql.UncheckedSqlException {
    try {
      connection.rollback();
    } catch (SQLException e) {
      throw new Sql.UncheckedSqlException(e);
    }
  }

  @Override
  public final RuntimeException rollbackUnchecked(Throwable error) {
    Check.notNull(error, "error == null");

    RuntimeException rethrow;

    if (error instanceof RuntimeException unchecked) {
      rethrow = unchecked;
    } else {
      rethrow = new RuntimeException(error);
    }

    try {
      connection.rollback();
    } catch (SQLException e) {
      rethrow.addSuppressed(e);
    }

    return rethrow;
  }

  @Override
  public final void close() throws Sql.UncheckedSqlException {
    try (connection) {
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      throw new Sql.UncheckedSqlException(e);
    }
  }

  @Override
  public final int count(String sql, Object... args) throws Sql.UncheckedSqlException {
    Check.notNull(sql, "sql == null");
    Check.notNull(args, "args == null");

    SqlTemplate template;
    template = SqlTemplate.parse(sql, args);

    return template.count(dialect, connection);
  }

  @Override
  public final int[] executeUpdateText(String sqlText) throws UncheckedSqlException {
    String[] lines;
    lines = sqlText.split("\n"); // implicit null check

    StringBuilder sql;
    sql = new StringBuilder();

    try (Statement stmt = connection.createStatement()) {
      for (String line : lines) {

        if (!line.isBlank()) {
          sql.append(line);
        }

        else if (!sql.isEmpty()) {
          String batch;
          batch = sql.toString();

          sql.setLength(0);

          stmt.addBatch(batch);
        }

      }

      if (!sql.isEmpty()) {
        String batch;
        batch = sql.toString();

        stmt.addBatch(batch);
      }

      return stmt.executeBatch();
    } catch (SQLException e) {
      throw new Sql.UncheckedSqlException(e);
    }
  }

  @Override
  public final void processQuery(Sql.QueryProcessor processor, String sql, Object... args) throws UncheckedSqlException {
    Check.notNull(processor, "processor == null");
    Check.notNull(sql, "sql == null");
    Check.notNull(args, "args == null");

    SqlTemplate template;
    template = SqlTemplate.parse(sql, args);

    template.process(connection, processor);
  }

  @Override
  public final void processQuery(Sql.QueryProcessor processor, Sql.Page page, String sql, Object... args) throws Sql.UncheckedSqlException {
    Check.notNull(processor, "processor == null");
    Check.notNull(page, "page == null");
    Check.notNull(sql, "sql == null");
    Check.notNull(args, "args == null");

    SqlTemplate template;
    template = SqlTemplate.parse(sql, args);

    template.paginate(dialect, page);

    template.process(connection, processor);
  }

  @Override
  public final Transaction sql(String value) {
    sql = Check.notNull(value, "value == null");

    if (arguments != null) {
      arguments.clear();
    }

    if (batches != null) {
      batches.clear();
    }

    return this;
  }

  @Override
  public final Transaction add(Object value) {
    Check.notNull(value, "value == null");

    checkSql();

    if (arguments == null) {
      arguments = new GrowableList<>();
    }

    arguments.add(value);

    return this;
  }

  @Override
  public final Transaction add(Object value, int sqlType) {
    checkSql();

    Object nullable = Sql.nullable(value, sqlType);

    if (arguments == null) {
      arguments = new GrowableList<>();
    }

    arguments.add(nullable);

    return this;
  }

  @Override
  public final Transaction addBatch() {
    checkSql();

    if (!hasArguments()) {
      throw new IllegalStateException("No arguments were defined");
    }

    if (batches == null) {
      batches = new GrowableList<>();
    }

    UnmodifiableList<Object> batch;
    batch = arguments.toUnmodifiableList();

    arguments.clear();

    batches.add(batch);

    return this;
  }

  @Override
  public final int[] batchUpdate() {
    Check.state(hasBatches(), "No batches were defined");
    Check.state(!hasArguments(), "Dangling arguments not added to a batch");

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      for (var batch : batches) {
        setArguments(stmt, batch);

        stmt.addBatch();
      }

      batches.clear();

      return stmt.executeBatch();
    } catch (SQLException e) {
      throw new Sql.UncheckedSqlException(e);
    }
  }

  @Override
  public final <T> List<T> query(Sql.RowMapper<T> mapper) throws UncheckedSqlException {
    checkQuery(mapper);

    GrowableList<T> list;
    list = new GrowableList<>();

    if (hasArguments()) {

      try (PreparedStatement stmt = prepare(); ResultSet rs = stmt.executeQuery()) {
        query0(mapper, list, rs);
      } catch (SQLException e) {
        throw new Sql.UncheckedSqlException(e);
      }

    } else {

      try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        query0(mapper, list, rs);
      } catch (SQLException e) {
        throw new Sql.UncheckedSqlException(e);
      }

    }

    return list.toUnmodifiableList();
  }

  private <T> void query0(Sql.RowMapper<T> mapper, GrowableList<T> list, ResultSet rs) throws SQLException {
    while (rs.next()) {
      T instance;
      instance = mapper.mapRow(rs);

      list.add(instance);
    }
  }

  @Override
  public final <T> T queryOne(Sql.RowMapper<T> mapper) throws UncheckedSqlException {
    checkQuery(mapper);

    T result;

    if (hasArguments()) {

      try (PreparedStatement stmt = prepare(); ResultSet rs = stmt.executeQuery()) {
        result = queryOne0(mapper, rs);
      } catch (SQLException e) {
        throw new Sql.UncheckedSqlException(e);
      }

    } else {

      try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        result = queryOne0(mapper, rs);
      } catch (SQLException e) {
        throw new Sql.UncheckedSqlException(e);
      }

    }

    return result;
  }

  private <T> T queryOne0(Sql.RowMapper<T> mapper, ResultSet rs) throws SQLException {
    T result;

    if (!rs.next()) {
      throw new UnsupportedOperationException("Implement me");
    }

    result = mapper.mapRow(rs);

    if (rs.next()) {
      throw new UnsupportedOperationException("Implement me");
    }

    return result;
  }

  @Override
  public final <T> T queryOneOrNull(RowMapper<T> mapper) throws UncheckedSqlException {
    checkQuery(mapper);

    T result;

    if (hasArguments()) {

      try (PreparedStatement stmt = prepare(); ResultSet rs = stmt.executeQuery()) {
        result = queryOneOrNull0(mapper, rs);
      } catch (SQLException e) {
        throw new Sql.UncheckedSqlException(e);
      }

    } else {

      try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        result = queryOneOrNull0(mapper, rs);
      } catch (SQLException e) {
        throw new Sql.UncheckedSqlException(e);
      }

    }

    return result;
  }

  private <T> T queryOneOrNull0(Sql.RowMapper<T> mapper, ResultSet rs) throws SQLException {
    T result;

    if (!rs.next()) {
      result = null;
    } else {
      result = mapper.mapRow(rs);
    }

    if (rs.next()) {
      throw new UnsupportedOperationException("Implement me");
    }

    return result;
  }

  @Override
  public final int update() {
    checkSql();
    Check.state(!hasBatches(), "One or more batches were defined");

    int result;

    if (hasArguments()) {

      try (PreparedStatement stmt = prepare()) {
        result = stmt.executeUpdate();
      } catch (SQLException e) {
        throw new Sql.UncheckedSqlException(e);
      }

    } else {

      try (Statement stmt = connection.createStatement()) {
        result = stmt.executeUpdate(sql);
      } catch (SQLException e) {
        throw new Sql.UncheckedSqlException(e);
      }

    }

    return result;
  }

  private void checkSql() {
    Check.state(sql != null, "No SQL statement was defined");
  }

  private void checkQuery(RowMapper<?> mapper) {
    Check.notNull(mapper, "mapper == null");

    Check.state(sql != null, "No SQL statement was defined");

    Check.state(!hasBatches(), "Cannot execute query: one or more batches were defined");
  }

  private boolean hasArguments() {
    return arguments != null && !arguments.isEmpty();
  }

  private boolean hasBatches() {
    return batches != null && !batches.isEmpty();
  }

  private PreparedStatement prepare() throws SQLException {
    PreparedStatement stmt;
    stmt = connection.prepareStatement(sql);

    // we assume this method was called after hasArguments() returned true
    // so arguments is guaranteed to be non-null
    setArguments(stmt, arguments);

    arguments.clear();

    return stmt;
  }

  private void setArguments(PreparedStatement stmt, List<Object> arguments) throws SQLException {
    for (int idx = 0, size = arguments.size(); idx < size; idx++) {
      Object argument;
      argument = arguments.get(idx);

      Sql.set(stmt, idx + 1, argument);
    }
  }

}