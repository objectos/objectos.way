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
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

final class SqlTransaction implements Sql.Transaction {

  private enum State {

    START,

    SQL,

    SQL_COUNT,

    SQL_GENERATED,

    SQL_PAGINATED,

    SQL_SCRIPT,

    SQL_TEMPLATE,

    PREPARED,

    PREPARED_BATCH,

    PREPARED_COUNT,

    PREPARED_GENERATED,

    PREPARED_GENERATED_BATCH,

    PREPARED_PAGINATED,

    ERROR;

  }

  private Object aux;

  private final Connection connection;

  private final SqlDialect dialect;

  private Object main;

  private int parameterIndex;

  private State state = State.START;

  SqlTransaction(Connection connection, SqlDialect dialect) {
    this.connection = connection;

    this.dialect = dialect;
  }

  static SqlTransaction of(Connection connection) {
    try {
      DatabaseMetaData data;
      data = connection.getMetaData();

      SqlDialect dialect;
      dialect = SqlDialect.of(data);

      return new SqlTransaction(connection, dialect);
    } catch (SQLException e) {
      throw new Sql.DatabaseException(e);
    }
  }

  @Override
  public final Sql.Meta meta() throws Sql.DatabaseException {
    try {
      final DatabaseMetaData metaData;
      metaData = connection.getMetaData();

      return new SqlMeta(metaData);
    } catch (SQLException e) {
      throw new Sql.DatabaseException(e);
    }
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
    Throwable rethrow;
    rethrow = null;

    if (main instanceof Statement stmt) {

      try {
        if (!stmt.isClosed()) {
          stmt.close();
        }
      } catch (Throwable e) {
        rethrow = suppressIfNecessary(rethrow, e);
      }

    }

    try {
      connection.close();
    } catch (Throwable e) {
      rethrow = suppressIfNecessary(rethrow, e);
    }

    if (rethrow == null) {
      return;
    }

    if (rethrow instanceof Error error) {
      throw error;
    }

    if (rethrow instanceof RuntimeException runtime) {
      throw runtime;
    }

    throw new Sql.DatabaseException(rethrow);
  }

  private Throwable suppressIfNecessary(Throwable rethrow, Throwable e) {
    if (rethrow != null) {
      rethrow.addSuppressed(e);
    } else {
      rethrow = e;
    }

    return rethrow;
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
              main = connection.createStatement();

              sqlScript(';', value);
            } catch (SQLException e) {
              throw stateAndWrap(e);
            }
          }

          case SqlKind.TEMPLATE -> {
            state = State.SQL_TEMPLATE;

            main = SqlTemplate.parse(value);
          }

          case null -> throw new NullPointerException("kind == null");
        }

      }

      case SQL,
           SQL_COUNT,
           SQL_GENERATED,
           SQL_PAGINATED,
           SQL_SCRIPT,
           SQL_TEMPLATE -> throw illegalState();

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

  private void sqlScript(char sep, String value) throws SQLException {
    enum Script {

      NORMAL,

      DASH1,

      COMMENT_EOL,

      COMMENT_TRAD,

      COMMENT_TRAD_STAR,

      SLASH1,

      STMT,

      STRING,

      STRING_QUOTE;

    }

    Script script = Script.NORMAL, comment = null;

    int idx = 0, len = value.length(), stmt = 0;

    while (idx < len) {
      final char c;
      c = value.charAt(idx);

      switch (script) {
        case NORMAL -> {
          if (Ascii.isWhitespace(c)) {
            script = Script.NORMAL;
          }

          else if (c == '-') {
            script = Script.DASH1;

            stmt = idx;
          }

          else if (c == '/') {
            script = Script.SLASH1;

            comment = Script.NORMAL;

            stmt = idx;
          }

          else {
            script = Script.STMT;

            stmt = idx;
          }
        }

        case DASH1 -> {
          if (c == '-') {
            script = Script.COMMENT_EOL;
          }

          else {
            script = Script.STMT;
          }
        }

        case COMMENT_EOL -> {
          if (Ascii.isLineTerminator(c)) {
            script = Script.NORMAL;
          }

          else {
            // consume comment
          }
        }

        case SLASH1 -> {
          if (c == '*') {
            script = Script.COMMENT_TRAD;
          }

          else if (comment == Script.NORMAL) {
            // not a comment start and we came from NORMAL state
            // => assume we are in a statement albeit malformed most likely
            script = Script.STMT;
          }

          else {
            script = comment;
          }
        }

        case COMMENT_TRAD -> {
          if (c == '*') {
            script = Script.COMMENT_TRAD_STAR;
          }

          else {
            // consume comment
          }
        }

        case COMMENT_TRAD_STAR -> {
          if (c == '/') {
            script = comment;
          }

          else {
            script = Script.COMMENT_TRAD;
          }
        }

        case STMT -> {
          if (c == sep) {
            script = Script.NORMAL;

            String sql;
            sql = value.substring(stmt, idx);

            ((Statement) main).addBatch(sql);
          }

          else if (c == '\'') {
            script = Script.STRING;
          }

          else if (c == '/') {
            comment = script;

            script = Script.SLASH1;
          }

          else {
            // consume char
          }
        }

        case STRING -> {
          if (c == '\'') {
            script = Script.STRING_QUOTE;
          }

          else {
            // consume string
          }
        }

        case STRING_QUOTE -> {
          if (c == '\'') {
            script = Script.STRING;
          }

          else {
            script = Script.STMT;

            idx--;
          }
        }
      }

      idx++;
    }

    switch (script) {
      case STMT -> {
        String sql;
        sql = value.substring(stmt, idx);

        ((Statement) main).addBatch(sql);
      }

      default -> {}
    }
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
           SQL_PAGINATED,
           SQL_TEMPLATE -> throw illegalState();

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
  public final void with(Sql.GeneratedKeys<?> value) {
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
           SQL_PAGINATED,
           SQL_TEMPLATE -> throw illegalState();

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
  public final void with(Sql.Page page) {
    Objects.requireNonNull(page, "page == null");

    switch (state) {
      case SQL -> {
        state = State.SQL_PAGINATED;

        main = paginate(page);
      }

      case START,
           SQL_COUNT,
           SQL_GENERATED,
           SQL_PAGINATED,
           SQL_SCRIPT,
           SQL_TEMPLATE -> throw illegalState();

      case PREPARED,
           PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH,
           PREPARED_PAGINATED -> throw illegalState();

      case ERROR -> throw illegalState();
    }
  }

  private String paginate(Sql.Page page) {
    final String sql;
    sql = sql();

    final StringBuilder builder;
    builder = new StringBuilder(sql);

    if (shouldAppendNewLine(builder)) {
      builder.append(System.lineSeparator());
    }

    int offset;
    offset = 0;

    final int pageNumber;
    pageNumber = page.number();

    if (pageNumber > 1) {
      offset = (pageNumber - 1) * page.size();
    }

    final int size;
    size = page.size();

    paginate(builder, offset, size);

    return builder.toString();
  }

  private void paginate(StringBuilder builder, int offset, int size) {
    switch (dialect) {
      case H2 -> {
        if (offset > 0) {
          builder.append("offset ");

          builder.append(offset);

          builder.append(" rows");

          builder.append(System.lineSeparator());
        }

        builder.append("fetch first ");

        builder.append(size);

        builder.append(" rows only");

        builder.append(System.lineSeparator());
      }

      case MYSQL, TESTING -> {
        builder.append("limit ");

        builder.append(size);

        builder.append(System.lineSeparator());

        if (offset > 0) {
          builder.append("offset ");

          builder.append(offset);

          builder.append(System.lineSeparator());
        }
      }
    }
  }

  @Override
  public final void param(Object value) {
    Objects.requireNonNull(value, "value == null");

    state = switch (state) {
      case SQL -> param0Create(value, Statement.NO_GENERATED_KEYS, State.PREPARED);

      case SQL_COUNT -> param0Create(value, Statement.NO_GENERATED_KEYS, State.PREPARED_COUNT);

      case SQL_GENERATED -> param0Create(value, Statement.RETURN_GENERATED_KEYS, State.PREPARED_GENERATED);

      case SQL_PAGINATED -> param0Create(value, Statement.NO_GENERATED_KEYS, State.PREPARED_PAGINATED);

      case SQL_SCRIPT -> throw new Sql.InvalidOperationException("""
      The 'add' operation cannot be executed on a SQL script.
      """);

      case SQL_TEMPLATE -> {
        final SqlTemplate tmpl;
        tmpl = sqlTemplate();

        tmpl.add(value);

        yield state;
      }

      case PREPARED,
           PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH,
           PREPARED_PAGINATED -> {
        try {
          final PreparedStatement stmt;
          stmt = prepared();

          set(stmt, parameterIndex++, value);

          yield state;
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }
      }

      case START -> throw illegalState();

      case ERROR -> throw illegalState();
    };
  }

  private State param0Create(Object value, int generatedKeys, State next) {
    try {
      final PreparedStatement stmt;
      stmt = createPrepared(generatedKeys);

      set(stmt, parameterIndex++, value);

      return next;
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  @Override
  public final void param(Object value, int sqlType) {
    state = switch (state) {
      case SQL -> param0Create(value, sqlType, Statement.NO_GENERATED_KEYS, State.PREPARED);

      case SQL_COUNT -> param0Create(value, sqlType, Statement.NO_GENERATED_KEYS, State.PREPARED_COUNT);

      case SQL_GENERATED -> param0Create(value, sqlType, Statement.RETURN_GENERATED_KEYS, State.PREPARED_GENERATED);

      case SQL_PAGINATED -> param0Create(value, sqlType, Statement.NO_GENERATED_KEYS, State.PREPARED_PAGINATED);

      case SQL_SCRIPT -> throw new Sql.InvalidOperationException("""
      The 'addNullable' operation cannot be executed on a SQL script.
      """);

      case SQL_TEMPLATE -> {
        final Object nullable;
        nullable = nullable(value, sqlType);

        final SqlTemplate tmpl;
        tmpl = sqlTemplate();

        tmpl.add(nullable);

        yield state;
      }

      case PREPARED,
           PREPARED_BATCH,
           PREPARED_COUNT,
           PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH,
           PREPARED_PAGINATED -> {
        try {
          final PreparedStatement stmt;
          stmt = prepared();

          param0(stmt, value, sqlType);

          yield state;
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }
      }

      case START -> throw illegalState();

      case ERROR -> throw illegalState();
    };
  }

  private State param0Create(Object value, int sqlType, int generatedKeys, State next) {
    try {
      final PreparedStatement stmt;
      stmt = createPrepared(generatedKeys);

      param0(stmt, value, sqlType);

      return next;
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  private void param0(PreparedStatement stmt, Object value, int sqlType) throws SQLException {
    final int index;
    index = parameterIndex++;

    if (value == null) {
      stmt.setNull(index, sqlType);
    } else {
      set(stmt, index, value);
    }
  }

  @Override
  public final void paramIf(Object value, boolean condition) {
    state = switch (state) {
      case START -> throw illegalState();

      case SQL, PREPARED -> throw new Sql.InvalidOperationException("""
      The 'addIf' operation cannot be executed on a plain SQL statement.
      """);

      case SQL_COUNT, PREPARED_COUNT -> throw new Sql.InvalidOperationException("""
      The 'addIf' operation cannot be executed on a SQL count statement.
      """);

      case SQL_GENERATED, PREPARED_GENERATED, PREPARED_GENERATED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'addIf' operation cannot be executed on a SQL statement returning generated keys.
      """);

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new Sql.InvalidOperationException("""
      The 'addIf' operation cannot be executed on a paginated SQL statement.
      """);

      case SQL_SCRIPT -> throw new Sql.InvalidOperationException("""
      The 'addIf' operation cannot be executed on a SQL script.
      """);

      case SQL_TEMPLATE -> {
        final SqlTemplate tmpl;
        tmpl = sqlTemplate();

        tmpl.addIf(value, condition);

        yield state;
      }

      case PREPARED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'addIf' operation cannot be executed on a SQL batch statement.
      """);

      case ERROR -> throw illegalState();
    };
  }

  private SqlTemplate sqlTemplate() {
    return (SqlTemplate) main;
  }

  @Override
  public final void addBatch() {
    state = switch (state) {
      case SQL -> throw new Sql.InvalidOperationException("""
      The 'addBatch' operation cannot be executed on a plain SQL statement with no parameters values set.
      """);

      case SQL_COUNT, PREPARED_COUNT -> throw new Sql.InvalidOperationException("""
      The 'addBatch' operation cannot be executed on a SQL count statement.
      """);

      case SQL_GENERATED -> throw new Sql.InvalidOperationException("""
      The 'addBatch' operation cannot be executed on a SQL statement:

      1) Returning generated keys; and
      2) With no parameter values set.
      """);

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new Sql.InvalidOperationException("""
      The 'addBatch' operation cannot be executed on a paginated SQL statement.
      """);

      case SQL_SCRIPT -> throw new Sql.InvalidOperationException("""
      The 'addBatch' operation cannot be executed on a SQL script.
      """);

      case SQL_TEMPLATE -> throw new Sql.InvalidOperationException("""
      The 'addBatch' operation cannot be executed on a SQL template.
      """);

      case PREPARED,
           PREPARED_BATCH -> addBatch0(State.PREPARED_BATCH);

      case PREPARED_GENERATED,
           PREPARED_GENERATED_BATCH -> addBatch0(State.PREPARED_GENERATED_BATCH);

      case START -> throw illegalState();

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
    try {
      return batchUpdate0();
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  private int[] batchUpdate0() throws SQLException {
    int[] result;

    state = switch (state) {
      case SQL, PREPARED -> throw new Sql.InvalidOperationException("""
      The 'batchUpdate' operation cannot be executed on a plain SQL statement with no batches defined.
      """);

      case SQL_COUNT, PREPARED_COUNT -> throw new Sql.InvalidOperationException("""
      The 'batchUpdate' operation cannot be executed on a SQL count statement.
      """);

      case SQL_GENERATED, PREPARED_GENERATED -> throw new Sql.InvalidOperationException("""
      The 'batchUpdate' operation cannot be executed on a SQL statement with no batches defined.
      """);

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new Sql.InvalidOperationException("""
      The 'batchUpdate' operation cannot be executed on a paginated SQL statement.
      """);

      case SQL_SCRIPT -> {
        try (Statement stmt = statement()) {
          result = stmt.executeBatch();
        }

        yield State.START;
      }

      case SQL_TEMPLATE -> throw new Sql.InvalidOperationException("""
      The 'batchUpdate' operation cannot be executed on a SQL template.
      """);

      case PREPARED_BATCH -> {
        try (PreparedStatement stmt = prepared()) {
          result = stmt.executeBatch();
        }

        yield State.START;
      }

      case PREPARED_GENERATED_BATCH -> {
        try (PreparedStatement stmt = prepared()) {
          final Sql.SqlGeneratedKeys<?> generatedKeys;
          generatedKeys = generatedKeys();

          result = stmt.executeBatch();

          generatedKeys.accept(stmt);
        }

        yield State.START;
      }

      case START -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return result;
  }

  @Override
  public final Sql.BatchUpdate batchUpdateWithResult() {
    try {
      final int[] result;
      result = batchUpdate0();

      return new SqlBatchUpdateSuccess(result);
    } catch (SQLException e) {
      state = State.START;

      return SqlBatchUpdateFailed.create(dialect, e);
    }
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
  public final <T> List<T> query(Sql.Mapper<T> mapper) throws Sql.DatabaseException {
    Objects.requireNonNull(mapper, "mapper == null");

    UtilList<T> list;
    list = new UtilList<>();

    state = switch (state) {
      case SQL, SQL_PAGINATED -> {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql())) {
          query(mapper, list, rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case SQL_COUNT, PREPARED_COUNT -> throw new Sql.InvalidOperationException("""
      The 'query' operation cannot be executed on a SQL count statement.
      """);

      case SQL_GENERATED, PREPARED_GENERATED, PREPARED_GENERATED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'query' operation cannot be executed on a SQL statement returning generated keys.
      """);

      case SQL_SCRIPT -> throw new Sql.InvalidOperationException("""
      The 'query' operation cannot be executed on a SQL script.
      """);

      case SQL_TEMPLATE -> query(mapper, list, createTemplate(Statement.NO_GENERATED_KEYS));

      case PREPARED, PREPARED_PAGINATED -> query(mapper, list, prepared());

      case PREPARED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'query' operation cannot be executed on a SQL batch statement.
      """);

      case START -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return list.toUnmodifiableList();
  }

  private <T> State query(Sql.Mapper<T> mapper, List<T> list, PreparedStatement stmt) {
    try (stmt; ResultSet rs = stmt.executeQuery()) {
      query(mapper, list, rs);
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }

    return State.START;
  }

  private <T> void query(Sql.Mapper<T> mapper, List<T> list, ResultSet rs) throws SQLException {
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
  public final <T> Optional<T> queryOptional(Sql.Mapper<T> mapper) throws Sql.DatabaseException {
    Objects.requireNonNull(mapper, "mapper == null");

    Optional<T> result;

    state = switch (state) {
      case SQL -> {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql())) {
          result = queryOptional(mapper, rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case SQL_COUNT, PREPARED_COUNT -> throw new Sql.InvalidOperationException("""
      The 'queryOptional' operation cannot be executed on a SQL count statement.
      """);

      case SQL_GENERATED, PREPARED_GENERATED, PREPARED_GENERATED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'queryOptional' operation cannot be executed on a SQL statement returning generated keys.
      """);

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new Sql.InvalidOperationException("""
      The 'queryOptional' operation cannot be executed on a paginated SQL statement.
      """);

      case SQL_SCRIPT -> throw new Sql.InvalidOperationException("""
      The 'queryOptional' operation cannot be executed on a SQL script.
      """);

      case SQL_TEMPLATE -> {
        result = queryOptional(mapper, createTemplate(Statement.NO_GENERATED_KEYS));

        yield State.START;
      }

      case PREPARED -> {
        result = queryOptional(mapper, prepared());

        yield State.START;
      }

      case PREPARED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'queryOptional' operation cannot be executed on a SQL batch statement.
      """);

      case START -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return result;
  }

  private <T> Optional<T> queryOptional(Sql.Mapper<T> mapper, PreparedStatement stmt) {
    try (stmt; ResultSet rs = stmt.executeQuery()) {
      return queryOptional(mapper, rs);
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  private <T> Optional<T> queryOptional(Sql.Mapper<T> mapper, ResultSet rs) throws SQLException {
    Optional<T> result;
    result = Optional.empty();

    if (rs.next()) {
      T first = mapper.map(rs, 1);

      if (rs.next()) {
        throw new Sql.TooManyRowsException();
      }

      result = Optional.of(first);
    }

    return result;
  }

  @Override
  public final OptionalInt queryOptionalInt() throws Sql.DatabaseException {
    OptionalInt result;

    state = switch (state) {
      case SQL, SQL_COUNT -> {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql())) {
          result = queryOptionalInt(rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case SQL_GENERATED, PREPARED_GENERATED, PREPARED_GENERATED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'queryOptionalInt' operation cannot be executed on a SQL statement returning generated keys.
      """);

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new Sql.InvalidOperationException("""
      The 'queryOptionalInt' operation cannot be executed on a paginated SQL statement.
      """);

      case SQL_SCRIPT -> throw new Sql.InvalidOperationException("""
      The 'queryOptionalInt' operation cannot be executed on a SQL script.
      """);

      case SQL_TEMPLATE -> {
        result = queryOptionalInt(createTemplate(Statement.NO_GENERATED_KEYS));

        yield State.START;
      }

      case PREPARED, PREPARED_COUNT -> {
        result = queryOptionalInt(prepared());

        yield State.START;
      }

      case PREPARED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'queryOptionalInt' operation cannot be executed on a SQL batch statement.
      """);

      case START -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return result;
  }

  private OptionalInt queryOptionalInt(PreparedStatement stmt) {
    try (stmt; ResultSet rs = stmt.executeQuery()) {
      return queryOptionalInt(rs);
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  private OptionalInt queryOptionalInt(ResultSet rs) throws SQLException {
    OptionalInt result;
    result = OptionalInt.empty();

    if (rs.next()) {
      int value;
      value = rs.getInt(1);

      if (rs.next()) {
        throw new Sql.TooManyRowsException();
      }

      result = OptionalInt.of(value);
    }

    return result;
  }

  @Override
  public final OptionalLong queryOptionalLong() throws Sql.DatabaseException {
    OptionalLong result;

    state = switch (state) {
      case SQL, SQL_COUNT -> {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql())) {
          result = queryOptionalLong(rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case SQL_GENERATED, PREPARED_GENERATED, PREPARED_GENERATED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'queryOptionalLong' operation cannot be executed on a SQL statement returning generated keys.
      """);

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new Sql.InvalidOperationException("""
      The 'queryOptionalLong' operation cannot be executed on a paginated SQL statement.
      """);

      case SQL_SCRIPT -> throw new Sql.InvalidOperationException("""
      The 'queryOptionalLong' operation cannot be executed on a SQL script.
      """);

      case SQL_TEMPLATE -> {
        result = queryOptionalLong(createTemplate(Statement.NO_GENERATED_KEYS));

        yield State.START;
      }

      case PREPARED, PREPARED_COUNT -> {
        result = queryOptionalLong(prepared());

        yield State.START;
      }

      case PREPARED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'queryOptionalLong' operation cannot be executed on a SQL batch statement.
      """);

      case START -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return result;
  }

  private OptionalLong queryOptionalLong(PreparedStatement stmt) {
    try (stmt; ResultSet rs = stmt.executeQuery()) {
      return queryOptionalLong(rs);
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  private OptionalLong queryOptionalLong(ResultSet rs) throws SQLException {
    OptionalLong result;
    result = OptionalLong.empty();

    if (rs.next()) {
      long value;
      value = rs.getLong(1);

      if (rs.next()) {
        throw new Sql.TooManyRowsException();
      }

      result = OptionalLong.of(value);
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
          result = querySingle(mapper, rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case SQL_COUNT, PREPARED_COUNT -> throw new Sql.InvalidOperationException("""
      The 'querySingle' operation cannot be executed on a SQL count statement.
      """);

      case SQL_GENERATED, PREPARED_GENERATED, PREPARED_GENERATED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'querySingle' operation cannot be executed on a SQL statement returning generated keys.
      """);

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new Sql.InvalidOperationException("""
      The 'querySingle' operation cannot be executed on a paginated SQL statement.
      """);

      case SQL_SCRIPT -> throw new Sql.InvalidOperationException("""
      The 'querySingle' operation cannot be executed on a SQL script.
      """);

      case SQL_TEMPLATE -> {
        result = querySingle(mapper, createTemplate(Statement.NO_GENERATED_KEYS));

        yield State.START;
      }

      case PREPARED -> {
        result = querySingle(mapper, prepared());

        yield State.START;
      }

      case PREPARED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'querySingle' operation cannot be executed on a SQL batch statement.
      """);

      case START -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return result;
  }

  private <T> T querySingle(Sql.Mapper<T> mapper, PreparedStatement stmt) {
    try (stmt; ResultSet rs = stmt.executeQuery()) {
      return querySingle(mapper, rs);
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  private <T> T querySingle(Sql.Mapper<T> mapper, ResultSet rs) throws SQLException {
    T result;

    if (!rs.next()) {
      throw new Sql.NoSuchRowException();
    }

    result = mapper.map(rs, 1);

    if (rs.next()) {
      throw new Sql.TooManyRowsException();
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

      case SQL_GENERATED, PREPARED_GENERATED, PREPARED_GENERATED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'querySingleInt' operation cannot be executed on a SQL statement returning generated keys.
      """);

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new Sql.InvalidOperationException("""
      The 'querySingleInt' operation cannot be executed on a paginated SQL statement.
      """);

      case SQL_SCRIPT -> throw new Sql.InvalidOperationException("""
      The 'querySingleInt' operation cannot be executed on a SQL script.
      """);

      case SQL_TEMPLATE -> {
        result = querySingleInt(createTemplate(Statement.NO_GENERATED_KEYS));

        yield State.START;
      }

      case PREPARED, PREPARED_COUNT -> {
        result = querySingleInt(prepared());

        yield State.START;
      }

      case PREPARED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'querySingleInt' operation cannot be executed on a SQL batch statement.
      """);

      case START -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return result;
  }

  private int querySingleInt(PreparedStatement stmt) {
    try (stmt; ResultSet rs = stmt.executeQuery()) {
      return querySingleInt0(rs);
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  private int querySingleInt0(ResultSet rs) throws SQLException {
    if (!rs.next()) {
      throw new Sql.NoSuchRowException();
    }

    int result;
    result = rs.getInt(1);

    if (rs.next()) {
      throw new Sql.TooManyRowsException();
    }

    return result;
  }

  @Override
  public final long querySingleLong() throws Sql.DatabaseException {
    long result;

    state = switch (state) {
      case SQL, SQL_COUNT -> {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql())) {
          result = querySingleLong(rs);
        } catch (SQLException e) {
          throw stateAndWrap(e);
        }

        yield State.START;
      }

      case SQL_GENERATED, PREPARED_GENERATED, PREPARED_GENERATED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'querySingleLong' operation cannot be executed on a SQL statement returning generated keys.
      """);

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new Sql.InvalidOperationException("""
      The 'querySingleLong' operation cannot be executed on a paginated SQL statement.
      """);

      case SQL_SCRIPT -> throw new Sql.InvalidOperationException("""
      The 'querySingleLong' operation cannot be executed on a SQL script.
      """);

      case SQL_TEMPLATE -> {
        result = querySingleLong(createTemplate(Statement.NO_GENERATED_KEYS));

        yield State.START;
      }

      case PREPARED, PREPARED_COUNT -> {
        result = querySingleLong(prepared());

        yield State.START;
      }

      case PREPARED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'querySingleLong' operation cannot be executed on a SQL batch statement.
      """);

      case START -> throw illegalState();

      case ERROR -> throw illegalState();
    };

    return result;
  }

  private long querySingleLong(PreparedStatement stmt) {
    try (stmt; ResultSet rs = stmt.executeQuery()) {
      return querySingleLong(rs);
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  private long querySingleLong(ResultSet rs) throws SQLException {
    if (!rs.next()) {
      throw new Sql.NoSuchRowException();
    }

    long result;
    result = rs.getLong(1);

    if (rs.next()) {
      throw new Sql.TooManyRowsException();
    }

    return result;
  }

  @Override
  public final int update() {
    try {
      return update0();
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  private int update0() throws SQLException {
    int result;

    state = switch (state) {
      case START -> throw illegalState();

      case SQL -> {
        try (Statement stmt = connection.createStatement()) {
          result = stmt.executeUpdate(sql());
        }

        yield State.START;
      }

      case SQL_COUNT, PREPARED_COUNT -> throw new Sql.InvalidOperationException("""
      The 'update' operation cannot be executed on a SQL count statement.
      """);

      case SQL_GENERATED -> {
        try (Statement stmt = connection.createStatement()) {
          final Sql.SqlGeneratedKeys<?> generatedKeys;
          generatedKeys = generatedKeys();

          result = stmt.executeUpdate(sql(), Statement.RETURN_GENERATED_KEYS);

          generatedKeys.accept(stmt);
        }

        yield State.START;
      }

      case SQL_PAGINATED, PREPARED_PAGINATED -> throw new Sql.InvalidOperationException("""
      The 'update' operation cannot be executed on a paginated SQL statement.
      """);

      case SQL_SCRIPT -> throw new Sql.InvalidOperationException("""
      The 'update' operation cannot be executed on a SQL script.
      """);

      case SQL_TEMPLATE -> throw new Sql.InvalidOperationException("""
      The 'update' operation cannot be executed on a SQL template.
      """);

      case PREPARED -> {
        try (PreparedStatement stmt = prepared()) {
          result = stmt.executeUpdate();
        }

        yield State.START;
      }

      case PREPARED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'update' operation cannot be executed on a SQL batch statement.
      """);

      case PREPARED_GENERATED -> {
        try (PreparedStatement stmt = prepared()) {
          final Sql.SqlGeneratedKeys<?> generatedKeys;
          generatedKeys = generatedKeys();

          result = stmt.executeUpdate();

          generatedKeys.accept(stmt);
        }

        yield State.START;
      }

      case PREPARED_GENERATED_BATCH -> throw new Sql.InvalidOperationException("""
      The 'update' operation cannot be executed on a SQL batch statement returning generated keys.
      """);

      case ERROR -> throw illegalState();
    };

    return result;
  }

  @Override
  public final Sql.Update updateWithResult() {
    try {
      final int count;
      count = update0();

      return new SqlUpdateSuccess(count);
    } catch (SQLException e) {
      state = State.START;

      return SqlUpdateFailed.create(dialect, e);
    }
  }

  private PreparedStatement createPrepared(int generatedKeys) throws SQLException {
    final PreparedStatement stmt;
    stmt = connection.prepareStatement(sql(), generatedKeys);

    main = stmt;

    return stmt;
  }

  private PreparedStatement createTemplate(int generatedKeys) {
    try {
      final SqlTemplate tmpl;
      tmpl = sqlTemplate();

      final String sql;
      sql = tmpl.sql();

      final PreparedStatement stmt;
      stmt = connection.prepareStatement(sql, generatedKeys);

      final List<Object> arguments;
      arguments = tmpl.arguments();

      if (arguments != null) {
        int index = 1;

        for (Object arg : arguments) {
          set(stmt, index++, arg);
        }
      }

      return stmt;
    } catch (SQLException e) {
      throw stateAndWrap(e);
    }
  }

  private Sql.SqlGeneratedKeys<?> generatedKeys() {
    return (Sql.SqlGeneratedKeys<?>) aux;
  }

  private IllegalStateException illegalState() {
    return new IllegalStateException(state.name());
  }

  private record Null(int sqlType) {}

  private Object nullable(Object value, int sqlType) {
    if (value == null) {
      return new Null(sqlType);
    } else {
      return value;
    }
  }

  private PreparedStatement prepared() {
    return (PreparedStatement) main;
  }

  private void set(PreparedStatement stmt, int index, Object value) throws SQLException {
    switch (value) {
      case Null x -> stmt.setNull(index, x.sqlType);

      default -> stmt.setObject(index, value);
    }
  }

  private Sql.DatabaseException stateAndWrap(SQLException e) {
    state = State.ERROR;

    return new Sql.DatabaseException(e);
  }

}