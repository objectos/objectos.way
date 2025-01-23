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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.OptionalLong;
import objectos.way.Sql.GeneratedKeys;

final class SqlTransaction implements Sql.Transaction {

  private enum State {

    START,

    SQL,

    SQL_COUNT,

    SQL_GENERATED,

    SQL_PAGINATED,

    SQL_SCRIPT,

    PREPARED,

    PREPARED_BATCH,

    PREPARED_COUNT,

    PREPARED_GENERATED,

    PREPARED_GENERATED_BATCH,

    PREPARED_PAGINATED,

    ERROR;

  }

  private final SqlDialect dialect;

  private final Connection connection;

  private Object main;

  private Object aux;

  private int parameterIndex;

  private State state = State.START;

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
  public final void sql(String value) {
    sql(Sql.Kind.STATEMENT, value);
  }

  @Override
  public final void sql(Sql.Kind kind, String value) {
    Objects.requireNonNull(value, "value == null");

    switch (state) {
      case START -> {

        switch (kind) {
          case SqlKind.STATEMENT -> {
            state = State.SQL;

            main = value;
          }

          case SqlKind.COUNT -> {
            state = State.SQL_COUNT;

            main = sqlCount(value);
          }

          case SqlKind.SCRIPT -> {
            state = State.SQL_SCRIPT;

            try {
              main = sqlScript(value);
            } catch (SQLException e) {
              throw stateAndWrap(e);
            }
          }

          case null -> throw new NullPointerException("kind == null");
        }

      }

      case SQL,
           SQL_COUNT,
           SQL_GENERATED,
           SQL_PAGINATED,
           SQL_SCRIPT -> throw illegalState();

      case PREPARED,
           PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH,
           PREPARED_PAGINATED -> throw illegalState();

      case ERROR -> throw illegalState();
    }

    aux = null;

    parameterIndex = 1;
  }

  private String sql() {
    return (String) main;
  }

  private String sqlCount(String sql) {
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

    return builder.toString();
  }

  private Statement sqlScript(String value) throws SQLException {
    final String[] lines;
    lines = value.split("\n"); // implicit null check

    final StringBuilder sql;
    sql = new StringBuilder();

    final Statement stmt;
    stmt = connection.createStatement();

    for (String line : lines) {

      if (!line.isBlank()) {
        sql.append(line);
      }

      else if (!sql.isEmpty()) {
        final String batch;
        batch = sql.toString();

        sql.setLength(0);

        stmt.addBatch(batch);
      }

    }

    if (!sql.isEmpty()) {
      final String batch;
      batch = sql.toString();

      stmt.addBatch(batch);
    }

    return stmt;
  }

  @Override
  public final void format(Object... args) {
    switch (state) {
      case SQL -> {
        main = String.format(sql(), args);
      }

      case START,
           SQL_COUNT,
           SQL_GENERATED,
           SQL_SCRIPT,
           SQL_PAGINATED -> throw illegalState();

      case PREPARED,
           PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH,
           PREPARED_PAGINATED -> throw illegalState();

      case ERROR -> throw illegalState();
    }
  }

  @Override
  public final void with(GeneratedKeys<?> value) {
    Objects.requireNonNull(value, "value == null");

    state = switch (state) {
      case SQL -> {
        aux = value;

        yield State.SQL_GENERATED;
      }

      case START,
           SQL_COUNT,
           SQL_GENERATED,
           SQL_SCRIPT,
           SQL_PAGINATED -> throw illegalState();

      case PREPARED,
           PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH,
           PREPARED_PAGINATED -> throw illegalState();

      case ERROR -> throw illegalState();
    };
  }

  @Override
  public final void add(Object value) {
    Objects.requireNonNull(value, "value == null");

    state = switch (state) {
      case SQL -> add0Create(value, Statement.NO_GENERATED_KEYS, State.PREPARED);

      case SQL_COUNT -> add0Create(value, Statement.NO_GENERATED_KEYS, State.PREPARED_COUNT);

      case SQL_GENERATED -> add0Create(value, Statement.RETURN_GENERATED_KEYS, State.PREPARED_GENERATED);

      case SQL_PAGINATED -> add0Create(value, Statement.NO_GENERATED_KEYS, State.PREPARED_PAGINATED);

      case PREPARED,
           PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH,
           PREPARED_PAGINATED -> {
        try {
          final PreparedStatement stmt;
          stmt = prepared();

          Sql.set(stmt, parameterIndex++, value);

          yield state;
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }
      }

      case START, SQL_SCRIPT, ERROR -> throw illegalState();
    };
  }

  private State add0Create(Object value, int generatedKeys, State next) {
    try {
      final PreparedStatement stmt;
      stmt = createPrepared(generatedKeys);

      Sql.set(stmt, parameterIndex++, value);

      return next;
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  @Override
  public final void addIf(Object value, boolean condition) {
    //    checkSql();
    //
    //    Object maybe;
    //    maybe = SqlMaybe.get(value, condition);
    //
    //    template = true;
    //
    //    return addValue(maybe);
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void add(Object value, int sqlType) {
    state = switch (state) {
      case SQL -> setNullable0Create(value, sqlType, Statement.NO_GENERATED_KEYS, State.PREPARED);

      case SQL_COUNT -> setNullable0Create(value, sqlType, Statement.NO_GENERATED_KEYS, State.PREPARED_COUNT);

      case SQL_GENERATED -> setNullable0Create(value, sqlType, Statement.RETURN_GENERATED_KEYS, State.PREPARED_GENERATED);

      case SQL_PAGINATED -> setNullable0Create(value, sqlType, Statement.NO_GENERATED_KEYS, State.PREPARED_PAGINATED);

      case PREPARED,
           PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH,
           PREPARED_PAGINATED -> {
        try {
          final PreparedStatement stmt;
          stmt = prepared();

          setNullable0(stmt, value, sqlType);

          yield state;
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }
      }

      case START, SQL_SCRIPT, ERROR -> throw illegalState();
    };
  }

  private State setNullable0Create(Object value, int sqlType, int generatedKeys, State next) {
    try {
      final PreparedStatement stmt;
      stmt = createPrepared(generatedKeys);

      setNullable0(stmt, value, sqlType);

      return next;
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  private void setNullable0(PreparedStatement stmt, Object value, int sqlType) throws SQLException {
    final int index;
    index = parameterIndex++;

    if (value == null) {
      stmt.setNull(index, sqlType);
    } else {
      Sql.set(stmt, index, value);
    }
  }

  @Override
  public final void addBatch() {
    state = switch (state) {
      case PREPARED,
           PREPARED_BATCH -> addBatch0(State.PREPARED_BATCH);

      case PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH -> addBatch0(State.PREPARED_GENERATED_BATCH);

      case START,
           SQL,
           SQL_COUNT,
           SQL_GENERATED,
           SQL_PAGINATED,
           SQL_SCRIPT -> throw illegalState();

      case PREPARED_COUNT,
           PREPARED_PAGINATED -> throw illegalState();

      case ERROR -> throw illegalState();
    };
  }

  private State addBatch0(State next) {
    try {
      final PreparedStatement prepared;
      prepared = prepared();

      prepared.addBatch();

      parameterIndex = 1;

      return next;
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  @Override
  public final int[] batchUpdate() {
    int[] result;

    state = switch (state) {
      case SQL_SCRIPT -> {
        try (Statement stmt = statement()) {
          result = stmt.executeBatch();
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case PREPARED_BATCH -> {
        try (PreparedStatement stmt = prepared()) {
          result = stmt.executeBatch();
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case PREPARED_GENERATED_BATCH -> {
        throw new UnsupportedOperationException("Implement me");
      }

      case START -> throw illegalState();

      case SQL,
           SQL_COUNT,
           SQL_GENERATED,
           SQL_PAGINATED -> throw illegalState();

      case PREPARED,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_PAGINATED -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return result;
  }

  private Statement statement() {
    return (Statement) main;
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
  public final void paginate(Sql.Page page) {
    Objects.requireNonNull(page, "page == null");

    switch (state) {
      case SQL -> {
        state = State.SQL_PAGINATED;

        main = dialect.paginate(sql(), page);
      }

      case START,
           SQL_COUNT,
           SQL_GENERATED,
           SQL_PAGINATED,
           SQL_SCRIPT -> throw illegalState();

      case PREPARED,
           PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH,
           PREPARED_PAGINATED -> throw illegalState();

      case ERROR -> throw illegalState();
    }
  }

  @Override
  public final <T> List<T> query(Sql.Mapper<T> mapper) throws Sql.DatabaseException {
    Objects.requireNonNull(mapper, "mapper == null");

    UtilList<T> list;
    list = new UtilList<>();

    state = switch (state) {
      case SQL, SQL_PAGINATED -> {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql())) {
          query0(mapper, list, rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case PREPARED, PREPARED_PAGINATED -> {
        try (PreparedStatement stmt = prepared(); ResultSet rs = stmt.executeQuery()) {
          query0(mapper, list, rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case START -> throw illegalState();

      case SQL_COUNT,
           SQL_GENERATED,
           SQL_SCRIPT -> throw illegalState();

      case PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return list.toUnmodifiableList();
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
  public final <T> T queryFirst(Sql.Mapper<T> mapper) throws Sql.DatabaseException {
    Objects.requireNonNull(mapper, "mapper == null");

    T result;

    state = switch (state) {
      case SQL -> {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql())) {
          result = queryFirst0(mapper, rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case PREPARED -> {
        try (PreparedStatement stmt = prepared(); ResultSet rs = stmt.executeQuery()) {
          result = queryFirst0(mapper, rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new UnsupportedOperationException("""
      This is a paginated SQL query. To prevent this exception from being thrown:

      1) Invoke the `query` method instead. Or
      2) Do not paginate this query.
      """);

      case START,
           SQL_COUNT,
           SQL_GENERATED,
           SQL_SCRIPT -> throw illegalState();

      case PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return result;
  }

  private <T> T queryFirst0(Sql.Mapper<T> mapper, ResultSet rs) throws SQLException {
    T result;

    if (!rs.next()) {
      result = null;
    } else {
      result = mapper.map(rs, 1);
    }

    return result;
  }

  @Override
  public final <T> T querySingle(Sql.Mapper<T> mapper) throws Sql.DatabaseException {
    Objects.requireNonNull(mapper, "mapper == null");

    T result;

    state = switch (state) {
      case SQL -> {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql())) {
          result = querySingle0(mapper, rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case PREPARED -> {
        try (PreparedStatement stmt = prepared(); ResultSet rs = stmt.executeQuery()) {
          result = querySingle0(mapper, rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new UnsupportedOperationException("""
      This is a paginated SQL query. To prevent this exception from being thrown:

      1) Invoke the `query` method instead. Or
      2) Do not paginate this query.
      """);

      case START,
           SQL_COUNT,
           SQL_GENERATED,
           SQL_SCRIPT -> throw illegalState();

      case PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH -> throw illegalState();

      case ERROR -> throw illegalState();
    };

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
  public final int querySingleInt() throws Sql.DatabaseException {
    int result;

    state = switch (state) {
      case SQL, SQL_COUNT -> {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql())) {
          result = querySingleInt0(rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case PREPARED, PREPARED_COUNT -> {
        try (PreparedStatement stmt = prepared(); ResultSet rs = stmt.executeQuery()) {
          result = querySingleInt0(rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new UnsupportedOperationException("""
      This is a paginated SQL query. To prevent this exception from being thrown:

      1) Invoke the `query` method instead. Or
      2) Do not paginate this query.
      """);

      case START,
           SQL_GENERATED,
           SQL_SCRIPT -> throw illegalState();

      case PREPARED_BATCH,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return result;
  }

  private int querySingleInt0(ResultSet rs) throws SQLException {
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

  @Override
  public final long querySingleLong() throws Sql.DatabaseException {
    long result;

    state = switch (state) {
      case SQL, SQL_COUNT -> {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql())) {
          result = querySingleLong0(rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case PREPARED, PREPARED_COUNT -> {
        try (PreparedStatement stmt = prepared(); ResultSet rs = stmt.executeQuery()) {
          result = querySingleLong0(rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new UnsupportedOperationException("""
      This is a paginated SQL query. To prevent this exception from being thrown:

      1) Invoke the `query` method instead. Or
      2) Do not paginate this query.
      """);

      case START,
           SQL_GENERATED,
           SQL_SCRIPT -> throw illegalState();

      case PREPARED_BATCH,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return result;
  }

  private long querySingleLong0(ResultSet rs) throws SQLException {
    if (!rs.next()) {
      return 0L;
    }

    long result;
    result = rs.getLong(1);

    if (rs.next()) {
      throw new UnsupportedOperationException("Implement me");
    }

    return result;
  }

  @Override
  public final OptionalInt queryOptionalInt() throws Sql.DatabaseException {
    OptionalInt result;

    state = switch (state) {
      case SQL -> {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql())) {
          result = queryOptionalInt0(rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case PREPARED -> {
        try (PreparedStatement stmt = prepared(); ResultSet rs = stmt.executeQuery()) {
          result = queryOptionalInt0(rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new UnsupportedOperationException("""
      This is a paginated SQL query. To prevent this exception from being thrown:

      1) Invoke the `query` method instead. Or
      2) Do not paginate this query.
      """);

      case START,
           SQL_COUNT,
           SQL_GENERATED,
           SQL_SCRIPT -> throw illegalState();

      case PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH -> throw illegalState();

      case ERROR -> throw illegalState();
    };

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

    return result;
  }

  @Override
  public final OptionalLong queryOptionalLong() throws Sql.DatabaseException {
    OptionalLong result;

    state = switch (state) {
      case SQL -> {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql())) {
          result = queryOptionalLong0(rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case PREPARED -> {
        try (PreparedStatement stmt = prepared(); ResultSet rs = stmt.executeQuery()) {
          result = queryOptionalLong0(rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new UnsupportedOperationException("""
      This is a paginated SQL query. To prevent this exception from being thrown:

      1) Invoke the `query` method instead. Or
      2) Do not paginate this query.
      """);

      case START,
           SQL_COUNT,
           SQL_GENERATED,
           SQL_SCRIPT -> throw illegalState();

      case PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return result;
  }

  private OptionalLong queryOptionalLong0(ResultSet rs) throws SQLException {
    OptionalLong result;

    if (!rs.next()) {
      result = OptionalLong.empty();
    } else {
      long value;
      value = rs.getLong(1);

      result = OptionalLong.of(value);
    }

    return result;
  }

  @Override
  public final int update() {
    int result;

    state = switch (state) {
      case SQL -> {
        try (Statement stmt = connection.createStatement()) {
          result = stmt.executeUpdate(sql());
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case SQL_GENERATED -> {
        try (Statement stmt = connection.createStatement()) {
          final Sql.SqlGeneratedKeys<?> generatedKeys;
          generatedKeys = generatedKeys();

          result = stmt.executeUpdate(sql(), Statement.RETURN_GENERATED_KEYS);

          generatedKeys.accept(stmt);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case PREPARED -> {
        try (PreparedStatement stmt = prepared()) {
          result = stmt.executeUpdate();
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case PREPARED_GENERATED -> {
        try (PreparedStatement stmt = prepared()) {
          final Sql.SqlGeneratedKeys<?> generatedKeys;
          generatedKeys = generatedKeys();

          result = stmt.executeUpdate();

          generatedKeys.accept(stmt);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case START,
           SQL_COUNT,
           SQL_PAGINATED,
           SQL_SCRIPT -> throw illegalState();

      case PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED_BATCH,
           PREPARED_PAGINATED -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return result;
  }

  private PreparedStatement createPrepared(int generatedKeys) throws SQLException {
    final PreparedStatement stmt;
    stmt = connection.prepareStatement(sql(), generatedKeys);

    main = stmt;

    return stmt;
  }

  private Sql.SqlGeneratedKeys<?> generatedKeys() {
    return (Sql.SqlGeneratedKeys<?>) aux;
  }

  private PreparedStatement prepared() {
    return (PreparedStatement) main;
  }

  private IllegalStateException illegalState() {
    return new IllegalStateException(state.name());
  }

  private Sql.DatabaseException stateAndWrap(SQLException e) {
    state = State.ERROR;

    return new Sql.DatabaseException(e);
  }

}