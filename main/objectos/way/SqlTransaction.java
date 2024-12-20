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
import java.util.Objects;
import java.util.OptionalInt;
import java.util.regex.Pattern;

final class SqlTransaction implements Sql.Transaction {

  private final SqlDialect dialect;

  private final Connection connection;

  private String sql;

  private List<Object> arguments;

  private List<List<Object>> batches;

  private Sql.Page page;

  private boolean template;

  SqlTransaction(SqlDialect dialect, Connection connection) {
    this.dialect = dialect;

    this.connection = connection;
  }

  @Override
  public final void commit() throws Sql.DatabaseException {
    try {
      connection.commit();
    } catch (SQLException e) {
      throw new Sql.DatabaseException(e);
    }
  }

  @Override
  public final void rollback() throws Sql.DatabaseException {
    try {
      connection.rollback();
    } catch (SQLException e) {
      throw new Sql.DatabaseException(e);
    }
  }

  final void rollbackAndClose() throws Sql.DatabaseException {
    SQLException rethrow;
    rethrow = null;

    try {
      connection.rollback();
    } catch (SQLException e) {
      rethrow = e;
    } finally {

      try {
        connection.close();
      } catch (SQLException e) {
        if (rethrow != null) {
          rethrow.addSuppressed(e);
        } else {
          rethrow = e;
        }
      }

    }

    if (rethrow != null) {
      throw new Sql.DatabaseException(rethrow);
    }
  }

  @Override
  public final <T extends Throwable> T rollbackAndSuppress(T error) {
    Check.notNull(error, "error == null");

    return rollbackAndSuppress0(error);
  }

  private <E extends Throwable> E rollbackAndSuppress0(E error) {
    try {
      connection.rollback();
    } catch (SQLException e) {
      error.addSuppressed(e);
    }

    return error;
  }

  @Override
  public final Sql.RollbackWrapperException rollbackAndWrap(Throwable error) {
    Check.notNull(error, "error == null");

    Sql.RollbackWrapperException wrapper;
    wrapper = new Sql.RollbackWrapperException(error);

    return rollbackAndSuppress0(wrapper);
  }

  @Override
  public final void close() throws Sql.DatabaseException {
    try (connection) {
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      throw new Sql.DatabaseException(e);
    }
  }

  @Override
  public final Sql.Transaction sql(String value) {
    sql = Check.notNull(value, "value == null");

    if (arguments != null) {
      arguments.clear();
    }

    if (batches != null) {
      batches.clear();
    }

    page = null;

    template = false;

    return this;
  }

  @Override
  public final Sql.Transaction format(Object... args) {
    checkSql();

    sql = String.format(sql, args);

    return this;
  }

  @Override
  public final Sql.Transaction add(Object value) {
    Check.notNull(value, "value == null");

    checkSql();

    return addValue(value);
  }

  @Override
  public final Sql.Transaction addIf(Object value, boolean condition) {
    checkSql();

    Object maybe;
    maybe = SqlMaybe.get(value, condition);

    template = true;

    return addValue(maybe);
  }

  @Override
  public final Sql.Transaction add(Object value, int sqlType) {
    checkSql();

    Object nullable;
    nullable = Sql.nullable(value, sqlType);

    return addValue(nullable);
  }

  private Sql.Transaction addValue(Object value) {
    if (arguments == null) {
      arguments = Util.createList();
    }

    arguments.add(value);

    return this;
  }

  @Override
  public final Sql.Transaction addBatch() {
    checkSql();

    if (!hasArguments()) {
      throw new IllegalStateException("No arguments were defined");
    }

    if (batches == null) {
      batches = Util.createList();
    }

    List<Object> batch;
    batch = Util.toUnmodifiableList(arguments);

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
      throw new Sql.DatabaseException(e);
    }
  }

  @Override
  public final int count() throws Sql.DatabaseException {
    checkQuery();

    boolean newLine;
    newLine = shouldAppendNewLine(sql);

    StringBuilder builder;
    builder = new StringBuilder();

    builder.append("select count(*) from (");

    builder.append(System.lineSeparator());

    builder.append(sql);

    if (newLine) {
      builder.append(System.lineSeparator());
    }

    builder.append(") x");

    builder.append(System.lineSeparator());

    String query;
    query = builder.toString();

    int result;

    if (hasArguments()) {

      try (PreparedStatement stmt = prepare(query); ResultSet rs = stmt.executeQuery()) {
        result = count0(rs);
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    } else {

      try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
        result = count0(rs);
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    }

    return result;
  }

  private int count0(ResultSet rs) throws SQLException {
    if (!rs.next()) {
      return 0;
    }

    int result;
    result = rs.getInt(1);

    if (rs.next()) {
      throw new UnsupportedOperationException("Implement me");
    }

    return result;
  }

  private boolean shouldAppendNewLine(CharSequence query) {
    int length;
    length = query.length();

    if (length == 0) {
      return false;
    }

    int lastIndex;
    lastIndex = length - 1;

    char last;
    last = query.charAt(lastIndex);

    return !Character.isWhitespace(last);
  }

  @Override
  public final Sql.Transaction paginate(Sql.Page page) {
    this.page = Objects.requireNonNull(page);

    return this;
  }

  @Override
  public final <T> List<T> query(Sql.Mapper<T> mapper) throws Sql.DatabaseException {
    checkQuery(mapper);

    List<T> list;
    list = Util.createList();

    String sql;
    sql = paginateIfNecessary();

    if (hasArguments()) {

      try (PreparedStatement stmt = prepare(sql); ResultSet rs = stmt.executeQuery()) {
        query0(mapper, list, rs);
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    } else {

      try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        query0(mapper, list, rs);
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    }

    return Util.toUnmodifiableList(list);
  }

  private String paginateIfNecessary() {
    if (page != null) {
      return dialect.paginate(sql, page);
    } else {
      return sql;
    }
  }

  private <T> void query0(Sql.Mapper<T> mapper, List<T> list, ResultSet rs) throws SQLException {
    while (rs.next()) {
      T instance;
      instance = mapper.map(rs, 1);

      if (instance == null) {
        throw new Sql.MappingException("Mapper produced a null value");
      }

      list.add(instance);
    }
  }

  @Override
  public final <T> T querySingle(Sql.Mapper<T> mapper) throws Sql.DatabaseException {
    checkQuery(mapper);
    checkNoPage();

    T result;

    if (hasArguments()) {

      try (PreparedStatement stmt = prepare(sql); ResultSet rs = stmt.executeQuery()) {
        result = querySingle0(mapper, rs);
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    } else {

      try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        result = querySingle0(mapper, rs);
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    }

    return result;
  }

  private <T> T querySingle0(Sql.Mapper<T> mapper, ResultSet rs) throws SQLException {
    T result;

    if (!rs.next()) {
      throw new UnsupportedOperationException("Implement me");
    }

    result = mapper.map(rs, 1);

    if (rs.next()) {
      throw new UnsupportedOperationException("Implement me");
    }

    return result;
  }

  @Override
  public final <T> T queryNullable(Sql.Mapper<T> mapper) throws Sql.DatabaseException {
    checkQuery(mapper);
    checkNoPage();

    T result;

    if (hasArguments()) {

      try (PreparedStatement stmt = prepare(sql); ResultSet rs = stmt.executeQuery()) {
        result = queryNullable0(mapper, rs);
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    } else {

      try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        result = queryNullable0(mapper, rs);
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    }

    return result;
  }

  private <T> T queryNullable0(Sql.Mapper<T> mapper, ResultSet rs) throws SQLException {
    T result;

    if (!rs.next()) {
      result = null;
    } else {
      result = mapper.map(rs, 1);
    }

    if (rs.next()) {
      throw new UnsupportedOperationException("Implement me");
    }

    return result;
  }

  @Override
  public final OptionalInt queryOptionalInt() throws Sql.DatabaseException {
    checkQuery();
    checkNoPage();

    OptionalInt result;

    if (hasArguments()) {

      try (PreparedStatement stmt = prepare(sql); ResultSet rs = stmt.executeQuery()) {
        result = queryOptionalInt0(rs);
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    } else {

      try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        result = queryOptionalInt0(rs);
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    }

    return result;
  }

  private OptionalInt queryOptionalInt0(ResultSet rs) throws SQLException {
    OptionalInt result;

    if (!rs.next()) {
      result = OptionalInt.empty();
    } else {
      int value;
      value = rs.getInt(1);

      result = OptionalInt.of(value);
    }

    if (rs.next()) {
      throw new UnsupportedOperationException("Implement me");
    }

    return result;
  }

  @Override
  public final int[] scriptUpdate() throws Sql.DatabaseException {
    checkSql();
    Check.state(!hasBatches(), "One or more batches were defined");
    Check.state(!hasArguments(), "One or more arguments were added to operation");

    String[] lines;
    lines = sql.split("\n"); // implicit null check

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
      throw new Sql.DatabaseException(e);
    }
  }

  @Override
  public final int update() {
    checkSql();
    Check.state(!hasBatches(), "One or more batches were defined");

    int result;

    if (hasArguments()) {

      try (PreparedStatement stmt = prepare(sql)) {
        result = stmt.executeUpdate();
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    } else {

      try (Statement stmt = connection.createStatement()) {
        result = stmt.executeUpdate(sql);
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    }

    return result;
  }

  @Override
  public final int updateWithGeneratedKeys(Sql.GeneratedKeys<?> generatedKeys) {
    checkSql();
    Check.state(!hasBatches(), "One or more batches were defined");

    Sql.SqlGeneratedKeys<?> impl;
    impl = (Sql.SqlGeneratedKeys<?>) generatedKeys;

    int result;

    if (hasArguments()) {

      try (PreparedStatement stmt = prepare(sql, Statement.RETURN_GENERATED_KEYS)) {
        result = stmt.executeUpdate();

        impl.accept(stmt);
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    } else {

      try (Statement stmt = connection.createStatement()) {
        result = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

        impl.accept(stmt);
      } catch (SQLException e) {
        throw new Sql.DatabaseException(e);
      }

    }

    return result;
  }

  private void checkSql() {
    Check.state(sql != null, "No SQL statement was defined");
  }

  private void checkQuery(Sql.Mapper<?> mapper) {
    Check.notNull(mapper, "mapper == null");

    checkQuery();
  }

  private void checkQuery() {
    Check.state(sql != null, "No SQL statement was defined");

    Check.state(!hasBatches(), "Cannot execute query: one or more batches were defined");
  }

  private void checkNoPage() {
    Check.state(page == null, "Cannot paginate a query that is expected to return at most 1 row");
  }

  private boolean hasArguments() {
    return arguments != null && !arguments.isEmpty();
  }

  private boolean hasBatches() {
    return batches != null && !batches.isEmpty();
  }

  private PreparedStatement prepare(String sql) throws SQLException {
    return prepare(sql, Statement.NO_GENERATED_KEYS);
  }

  private static final Pattern TWO_DASHES = Pattern.compile("^--.*$", Pattern.MULTILINE);

  private PreparedStatement prepare(String sql, int generatedKeys) throws SQLException {
    if (!template) {
      PreparedStatement stmt;
      stmt = connection.prepareStatement(sql, generatedKeys);

      // we assume this method was called after hasArguments() returned true
      // so arguments is guaranteed to be non-null
      setArguments(stmt, arguments);

      return stmt;
    }

    StringBuilder sqlBuilder;
    sqlBuilder = new StringBuilder(sql.length());

    List<Object> values;
    values = Util.createList();

    int argsIndex;
    argsIndex = 0;

    String[] fragments;
    fragments = TWO_DASHES.split(sql);

    String fragment;
    fragment = fragments[0];

    sqlBuilder.append(fragment);

    int placeholders;
    placeholders = placeholders(fragment);

    while (values.size() < placeholders) {
      Object arg;
      arg = arguments.get(argsIndex++);

      values.add(arg);
    }

    for (int i = 1; i < fragments.length; i++) {
      fragment = fragments[i];

      placeholders = placeholders(fragment);

      switch (placeholders) {
        case 0 -> sqlBuilder.append(fragment);

        case 1 -> {
          if (argsIndex >= arguments.size()) {
            throw new IllegalArgumentException(
                "Missing value for fragment: " + fragment
            );
          }

          Object arg;
          arg = arguments.get(argsIndex++);

          if (arg instanceof SqlMaybe maybe) {
            if (maybe.absent()) {
              continue;
            }

            arg = maybe.value();
          }

          sqlBuilder.append(fragment);

          values.add(arg);
        }

        default -> {
          sqlBuilder.append(fragment);

          for (int j = 0; j < placeholders; j++) {

            if (argsIndex >= arguments.size()) {
              throw new IllegalArgumentException(
                  "Missing value for placeholder " + (j + 1) + " of fragment: " + fragment
              );
            }

            Object arg;
            arg = arguments.get(argsIndex++);

            if (arg instanceof SqlMaybe) {
              throw new IllegalArgumentException(
                  "Conditional value must not be used in a fragment with more than one placeholder: " + fragment
              );
            }

            values.add(arg);

          }
        }
      }
    }

    String sqlToPrepare;
    sqlToPrepare = sqlBuilder.toString();

    PreparedStatement stmt;
    stmt = connection.prepareStatement(sqlToPrepare, generatedKeys);

    setArguments(stmt, values);

    return stmt;
  }

  private static int placeholders(String fragment) {
    int count;
    count = 0;

    int question;
    question = 0;

    while (true) {
      question = fragment.indexOf('?', question);

      if (question < 0) {
        break;
      }

      count++;

      question++;
    }

    return count;
  }

  private void setArguments(PreparedStatement stmt, List<Object> arguments) throws SQLException {
    for (int idx = 0, size = arguments.size(); idx < size; idx++) {
      Object argument;
      argument = arguments.get(idx);

      Sql.set(stmt, idx + 1, argument);
    }
  }

}