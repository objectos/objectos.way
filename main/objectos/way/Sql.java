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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import javax.sql.DataSource;
import objectos.lang.object.Check;
import objectos.notes.Note3;
import objectos.notes.NoteSink;
import objectos.way.SqlDatabase.Builder;

/**
 * The <strong>Objectos SQL</strong> main class.
 */
public final class Sql {

  // notes

  public static final Note3<DatabaseMetaData, String, String> METADATA;

  static {
    Class<?> s;
    s = Sql.class;

    METADATA = Note3.debug(s, "Database metadata");
  }

  // trx isolation levels

  public static final Transaction.Isolation READ_UNCOMMITED = TransactionIsolation.READ_UNCOMMITED;

  public static final Transaction.Isolation READ_COMMITED = TransactionIsolation.READ_COMMITED;

  public static final Transaction.Isolation REPEATABLE_READ = TransactionIsolation.REPEATABLE_READ;

  public static final Transaction.Isolation SERIALIZABLE = TransactionIsolation.SERIALIZABLE;

  // types

  /**
   * A source for SQL database connections. It typically wraps a
   * {@link DataSource} instance.
   */
  public sealed interface Database permits SqlDatabase {

    /**
     * A {@code Sql.Database} configuration option.
     */
    public sealed interface Option permits SqlOption {}

    /**
     * Begins a transaction with the specified isolation level.
     *
     * @param level
     *        the transaction isolation level
     *
     * @return a connection to the underlying database with a transaction
     *         started with the specified isolation level
     *
     * @throws DatabaseException
     *         if a database access error occurs
     */
    Transaction beginTransaction(Transaction.Isolation level) throws DatabaseException;

  }

  /**
   * Thrown to indicate a database access error.
   */
  public static class DatabaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    DatabaseException(SQLException cause) {
      super(cause);
    }

    @Override
    public final SQLException getCause() {
      return (SQLException) super.getCause();
    }

  }

  public sealed interface GeneratedKeys<T> {

    public sealed interface OfInt extends GeneratedKeys<Integer> {

      int getAsInt(int index);

    }

    T get(int index);

    int size();

  }

  /**
   * Maps a row from a {@code ResultSet} to an object of type {@code T}.
   */
  @FunctionalInterface
  public interface Mapper<T> {

    /**
     * Implementations should not invoke the {@code next()} method on the
     * {@code ResultSet} object.
     *
     * @param rs
     *        the result set object positioned at the row to be mapped
     * @param startingParameterIndex
     *        the starting parameter index (always the value {@code 1})
     *
     * @return the mapped object
     */
    T map(ResultSet rs, int startingParameterIndex) throws SQLException;

  }

  public interface Page {

    /**
     * The page number. Page numbers are always greater than zero.
     * In other words, the first page is page number 1.
     * The second page is page number 2 and so on.
     *
     * @return the page number
     */
    int number();

    /**
     * The number of items to be displayed on each page.
     *
     * @return the number of items to be displayed on each page
     */
    int size();

  }

  /**
   * A provider of {@link Sql.Page} instances.
   */
  @FunctionalInterface
  public interface PageProvider {

    /**
     * The current page.
     *
     * @return the current page
     */
    Page page();

  }

  /**
   * Responsible for processing the results of a query operation.
   */
  @FunctionalInterface
  public interface QueryProcessor {

    /**
     * Process the entire result set.
     *
     * @param rs
     *        the result set to be processed
     *
     * @throws SQLException
     *         if the result set throws
     */
    void process(ResultSet rs) throws SQLException;

  }

  /**
   * A connection to a running transaction in a database.
   */
  public sealed interface Transaction permits SqlTransaction {

    /**
     * The isolation level of a transaction.
     */
    public sealed interface Isolation {}

    /**
     * Commits this transaction.
     *
     * @throws DatabaseException
     *         if the underlying {@link Connection#commit()} method throws
     */
    void commit() throws DatabaseException;

    /**
     * Undoes all changes made in this transaction.
     *
     * @throws DatabaseException
     *         if the underlying {@link Connection#rollback()} method throws
     */
    void rollback() throws DatabaseException;

    /**
     * Rolls back this transaction and returns the specified exception. If the
     * rollback operation throws then the thrown exception is added as a
     * suppressed exception to the specified exception.
     *
     * <p>
     * A typical usage is:
     *
     * <pre>
     * Sql.Transaction sql = source.beginTransaction(Sql.SERIALIZABLE);
     *
     * try {
     *   // code that may throw
     * } catch (KnownException e) {
     *   throw trx.rollbackAndSuppress(e);
     * } catch (Throwable t) {
     *   logger.log("Operation failed", trx.rollbackAndSuppress(t));
     * } finally {
     *   trx.close();
     * }</pre>
     *
     * @param error
     *        a throwable instance
     * @param <T>
     *        the type of the throwable instance
     *
     * @return the specified throwable which may or may not contain a new
     *         suppressed exception (from the rollback operation)
     */
    <T extends Throwable> T rollbackAndSuppress(T error);

    /**
     * Rolls back this transaction, wraps the specified throwable into an
     * unchecked exception and returns the wrapping exception. If the rollback
     * operation throws then the thrown exception is added as a suppressed
     * exception to the wrapping exception.
     *
     * <p>
     * A typical usage is:
     *
     * <pre>
     * Sql.Transaction sql = source.beginTransaction(Sql.SERIALIZABLE);
     *
     * try {
     *   // code that may throw
     * } catch (Throwable t) {
     *   throw trx.rollbackAndWrap(t);
     * } finally {
     *   trx.close();
     * }</pre>
     *
     * @param error
     *        the throwable to be wrapped
     *
     * @return a newly created wrapping exception whose cause is the specified
     *         throwable
     */
    RollbackWrapperException rollbackAndWrap(Throwable error);

    /**
     * Closes the underlying database connection.
     */
    void close() throws DatabaseException;

    int count(String sql, Object... args) throws DatabaseException;

    /**
     * Use the specified processor to process the results of the execution of
     * the specified row-retriving SQL statement. The specified arguments
     * are applied, in order, to the resulting prepared statement prior to
     * sending the query to the database.
     *
     * @param processor
     *        the processor to process the results
     * @param sql
     *        the row-retriving SQL statement to be executed
     * @param args
     *        the arguments of the SQL statement
     *
     * @throws DatabaseException
     *         if a database access error occurs
     */
    void processQuery(QueryProcessor processor, String sql, Object... args) throws DatabaseException;

    /**
     * Use the specified processor to process the results of the execution of
     * a row-retriving SQL statement that is obtained by applying the specified
     * page to the specified SQL statement. The specified arguments
     * are applied, in order, to the resulting prepared statement prior to
     * sending the query to the database.
     *
     * @param processor
     *        the processor to process the results
     * @param page
     *        limit the processing to this page
     * @param sql
     *        the row-retriving SQL statement to which the page will be applied
     * @param args
     *        the arguments of the SQL statement
     *
     * @throws DatabaseException
     *         if a database access error occurs
     */
    void processQuery(QueryProcessor processor, Page page, String sql, Object... args) throws DatabaseException;

    default void processQuery(QueryProcessor processor, PageProvider pageProvider, String sql, Object... args) throws DatabaseException {
      Page page;
      page = pageProvider.page(); // implicit null-check

      processQuery(processor, page, sql, args);
    }

    /**
     * Sets the SQL contents of this transaction to the specified value.
     *
     * <p>
     * Invoking this method additionally:
     *
     * <ul>
     * <li>clears any previously set arguments;
     *
     * @param value
     *        the raw SQL contents
     *
     * @return this object
     */
    Transaction sql(String value);

    /**
     * Replaces the current SQL statement with the result of
     * applying the specified arguments to the SQL statement as if it were a
     * <em>format string<em>.
     *
     * @param args
     *        arguments referenced by the format specifiers in the format
     *        string.
     *
     * @return this object
     */
    Transaction format(Object... args);

    /**
     * Adds the specified value to the SQL statement argument list.
     *
     * @param value
     *        the argument value which must not be {@code null}
     *
     * @return this object
     */
    Transaction add(Object value);

    /**
     * Adds the specified value to the SQL statement argument list.
     *
     * @param value
     *        the argument value which may be {@code null}
     * @param sqlType
     *        the SQL type (as defined in java.sql.Types)
     *
     * @return this object
     */
    Transaction add(Object value, int sqlType);

    Transaction addBatch();

    int[] batchUpdate();

    /**
     * Executes the current SQL statement as a row-retrieving query.
     */
    <T> List<T> query(Mapper<T> mapper) throws DatabaseException;

    /**
     * Executes the current SQL statement as a row-retrieving query which must
     * return a single result (no more and no less).
     */
    <T> T querySingle(Mapper<T> mapper) throws DatabaseException;

    /**
     * Executes the current SQL statement as a row-retrieving query which may
     * return a single result or {@code null} if there were no results.
     */
    <T> T queryNullable(Mapper<T> mapper) throws DatabaseException;

    OptionalInt queryOptionalInt() throws DatabaseException;

    /**
     * Executes the current SQL contents as a script. The SQL contents is
     * assumed to be a blank line separated list of SQL statements. The
     * statements will typically be SQL {@code INSERT} or {@code UPDATE}
     * statements.
     *
     * <p>
     * A typical usage is:
     *
     * <pre>
     * trx.sql("""
     * insert into City (id, name)
     * values (1, 'São Paulo')
     * ,      (2, 'New York')
     * ,      (3, 'Tokyo')
     *
     * insert into Country (id, name)
     * values (1, 'Brazil')
     * ,      (2, 'United States of America')
     * ,      (3, 'Japan')
     * """);
     *
     * int[] result = trx.scriptUpdate();
     *
     * assertEquals(result.length, 2);
     * assertEquals(result[0], 3);
     * assertEquals(result[1], 3);</pre>
     *
     * @return an array of update counts containing one element for each
     *         statement in the script. The elements of the array are ordered
     *         according to the order in which statements were listed in the
     *         script.
     */
    int[] scriptUpdate() throws DatabaseException;

    /**
     * Executes the current SQL statement as an update operation.
     */
    int update() throws DatabaseException;

    /**
     * Executes the current SQL statement as an update operation.
     */
    int updateWithGeneratedKeys(GeneratedKeys<?> generatedKeys) throws DatabaseException;

  }

  static final class MappingException extends RuntimeException {

    private static final long serialVersionUID = -3104952657116253825L;

    MappingException(String message) {
      super(message);
    }

    MappingException(String message, Throwable cause) {
      super(message, cause);
    }

  }

  public static final class RollbackWrapperException extends RuntimeException {

    private static final long serialVersionUID = 6575236786994565106L;

    RollbackWrapperException(Throwable cause) {
      super(cause);
    }

  }

  private Sql() {}

  /**
   * Creates a new {@code Database} instance from the specified data source and
   * configured with the specified options.
   *
   * @param dataSource
   *        the data source
   * @param options
   *        the configuration options
   *
   * @return a new {@code Database} instance
   *
   * @throws DatabaseException
   *         if a database access error occurs
   */
  public static Database createDatabase(DataSource dataSource, Database.Option... options) throws DatabaseException {
    Check.notNull(dataSource, "dataSource == null");
    Check.notNull(options, "options == null");

    try {
      SqlDatabase.Builder builder;
      builder = new SqlDatabase.Builder(dataSource);

      for (int idx = 0; idx < options.length; idx++) {
        Database.Option o;
        o = options[idx];

        Check.notNull(o, "options[", idx, "] == null");

        SqlOption option;
        option = (SqlOption) o;

        option.acceptSqlSourceBuilder(builder);
      }

      return builder.build();
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  public static GeneratedKeys.OfInt createGeneratedKeysOfInt() {
    return new SqlGeneratedKeysOfInt();
  }

  public static Page createPage(int number, int size) {
    Check.argument(number > 0, "number must be positive");
    Check.argument(size > 0, "size must be positive");

    record SqlPage(int number, int size) implements Sql.Page {}

    return new SqlPage(number, size);
  }

  /**
   * Creates a {@link Sql.Mapper} for the specified record type.
   */
  public static <R extends Record> Mapper<R> createRecordMapper(Class<R> recordType) {
    return RecordMapper.of(recordType);
  }

  public static Sql.Database.Option noteSink(NoteSink noteSink) {
    Check.notNull(noteSink, "noteSink == null");

    return new SqlOption() {
      @Override
      final void acceptSqlSourceBuilder(Builder builder) {
        builder.noteSink = noteSink;
      }
    };
  }

  /**
   * Undoes all changes made in the specified transaction and closes its
   * underlying database connection.
   *
   * @param trx
   *        the transaction object
   *
   * @throws DatabaseException
   *         if a database access error occurs
   */
  public static void rollbackAndClose(Sql.Transaction trx) throws DatabaseException {
    Objects.requireNonNull(trx, "trx == null");

    SqlTransaction impl;
    impl = (SqlTransaction) trx;

    impl.rollbackAndClose();
  }

  // utils

  private record Null(int sqlType) {}

  static Object nullable(Object value, int sqlType) {
    if (value == null) {
      return new Null(sqlType);
    } else {
      return value;
    }
  }

  static void set(PreparedStatement stmt, int index, Object value) throws SQLException {
    switch (value) {
      case Boolean b -> stmt.setBoolean(index, b.booleanValue());

      case Double d -> stmt.setDouble(index, d.doubleValue());

      case Float f -> stmt.setFloat(index, f.floatValue());

      case Integer i -> stmt.setInt(index, i.intValue());

      case LocalDate ld -> stmt.setObject(index, ld);

      case LocalDateTime dt -> stmt.setObject(index, dt);

      case Long i -> stmt.setLong(index, i.longValue());

      case String s -> stmt.setString(index, s);

      case Null x -> stmt.setNull(index, x.sqlType);

      default -> throw new IllegalArgumentException("Unexpected type: " + value.getClass());
    }
  }

  // non-public types

  static non-sealed abstract class SqlGeneratedKeys<T> implements Sql.GeneratedKeys<T> {

    final void accept(Statement stmt) throws SQLException {
      try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
        clear();

        accept(generatedKeys);
      }
    }

    abstract void accept(ResultSet rs) throws SQLException;

    abstract void clear();

  }

  static final class SqlGeneratedKeysOfInt extends SqlGeneratedKeys<Integer> implements Sql.GeneratedKeys.OfInt {

    private int[] keys = Util.EMPTY_INT_ARRAY;

    private int size = 0;

    @Override
    public final Integer get(int index) {
      return getAsInt(index);
    }

    @Override
    public final int getAsInt(int index) {
      Objects.checkIndex(index, size);

      return keys[index];
    }

    @Override
    public final int size() {
      return size;
    }

    @Override
    final void accept(ResultSet rs) throws SQLException {
      while (rs.next()) {
        int value;
        value = rs.getInt(1);

        add(value);
      }
    }

    @Override
    final void clear() {
      size = 0;
    }

    private void add(int value) {
      int requiredIndex;
      requiredIndex = size++;

      keys = Util.growIfNecessary(keys, requiredIndex);

      keys[requiredIndex] = value;
    }

  }

  private static final class RecordMapper<R extends Record> implements Mapper<R> {

    private final Class<?>[] types;

    private final Constructor<R> constructor;

    private final Object[] values;

    RecordMapper(Class<?>[] types, Constructor<R> constructor) {
      this.types = types;

      this.constructor = constructor;

      this.values = new Object[types.length];
    }

    static <R extends Record> RecordMapper<R> of(Class<R> recordType) {
      RecordComponent[] components; // early implicit null-check
      components = recordType.getRecordComponents();

      Class<?>[] types;
      types = new Class<?>[components.length];

      for (int idx = 0; idx < components.length; idx++) {
        RecordComponent component;
        component = components[idx];

        types[idx] = component.getType();
      }

      Constructor<R> constructor;

      try {
        constructor = recordType.getDeclaredConstructor(types);

        if (!constructor.canAccess(null)) {
          constructor.setAccessible(true);
        }
      } catch (NoSuchMethodException | SecurityException e) {
        throw new Sql.MappingException("Failed to obtain record canonical constructor", e);
      }

      return new RecordMapper<R>(types, constructor);
    }

    @Override
    public final R map(ResultSet rs, int startingIndex) throws SQLException {
      try {
        for (int idx = 0, len = types.length; idx < len; idx++) {
          Class<?> type;
          type = types[idx];

          Object value;
          value = rs.getObject(idx + 1, type);

          values[idx] = value;
        }

        return constructor.newInstance(values);
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new Sql.MappingException("Failed to create record instance", e);
      }
    }

    @SuppressWarnings("unused")
    final void checkColumnCount(ResultSet rs) throws SQLException {
      ResultSetMetaData meta;
      meta = rs.getMetaData();

      int columnCount;
      columnCount = meta.getColumnCount();

      if (columnCount != types.length) {
        throw new Sql.MappingException("Query returned " + columnCount + " columns but record has only " + types.length + " components");
      }
    }

  }

  enum TransactionIsolation implements Transaction.Isolation {

    READ_UNCOMMITED(Connection.TRANSACTION_READ_UNCOMMITTED),

    READ_COMMITED(Connection.TRANSACTION_READ_COMMITTED),

    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    final int jdbcValue;

    private TransactionIsolation(int level) {
      this.jdbcValue = level;
    }

  }

}