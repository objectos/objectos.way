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
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Consumer;
import javax.sql.DataSource;

/**
 * The <strong>Objectos SQL</strong> main class.
 */
public final class Sql {

  // trx isolation levels

  public static final Transaction.Isolation READ_UNCOMMITED = TransactionIsolation.READ_UNCOMMITED;

  public static final Transaction.Isolation READ_COMMITED = TransactionIsolation.READ_COMMITED;

  public static final Transaction.Isolation REPEATABLE_READ = TransactionIsolation.REPEATABLE_READ;

  public static final Transaction.Isolation SERIALIZABLE = TransactionIsolation.SERIALIZABLE;

  // sql kinds

  public static final Kind STATEMENT = Kind.STATEMENT;

  public static final Kind COUNT = Kind.COUNT;

  public static final Kind SCRIPT = Kind.SCRIPT;

  public static final Kind TEMPLATE = Kind.TEMPLATE;

  // types

  /**
   * A source for SQL database connections. It typically wraps a
   * {@link DataSource} instance.
   */
  public sealed interface Database permits SqlDatabase {

    /**
     * Configures the creation of a {@code Sql.Database} instance.
     */
    public sealed interface Config permits SqlDatabaseConfig {

      /**
       * Sets the clock to the specified value. This clock instance will be used
       * to define the current time for, e.g., a migration history entry record.
       *
       * @param value
       *        a clock instance
       */
      void clock(Clock value);

      void dataSource(DataSource value);

      /**
       * Sets the note sink to the specified value.
       *
       * @param value
       *        a note sink instance
       */
      void noteSink(Note.Sink value);

    }

    /**
     * Creates a new {@code Sql.Database} instance with the specified
     * configuration.
     *
     * @param config
     *        configuration options of this new instance
     *
     * @return a newly created database instance
     *
     * @throws DatabaseException
     *         if a database access error occurs
     */
    static Database create(Consumer<Config> config) throws DatabaseException {
      SqlDatabaseConfig builder;
      builder = new SqlDatabaseConfig();

      config.accept(builder);

      return builder.build();
    }

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

    /**
     * Returns a session to the underlying database.
     *
     * @return a session to the underlying database
     *
     * @throws DatabaseException
     *         if a database access error occurs
     */
    Transaction connect() throws DatabaseException;

    void migrate(Consumer<Migrator> config) throws DatabaseException;

  }

  public sealed interface GeneratedKeys<T> {

    public sealed interface OfInt extends GeneratedKeys<Integer> {

      int getAsInt(int index);

    }

    T get(int index);

    int size();

  }

  public sealed interface Kind permits SqlKind {

    Kind STATEMENT = SqlKind.STATEMENT;

    /**
     * Returns the number of rows of the current row-retrieving query SQL
     * statement. This method works by decorating the current SQL statement.
     */
    Kind COUNT = SqlKind.COUNT;

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
     */
    Kind SCRIPT = SqlKind.SCRIPT;

    Kind TEMPLATE = SqlKind.TEMPLATE;

  }

  /**
   * Maps a row from a {@code ResultSet} to an object of type {@code T}.
   */
  @FunctionalInterface
  public interface Mapper<T> {

    /**
     * Implementations must not invoke the {@code next()} method on the
     * {@code ResultSet} object and they must not return {@code null} values.
     *
     * @param rs
     *        the result set object positioned at the row to be mapped
     * @param startingParameterIndex
     *        the starting parameter index (always the value {@code 1})
     *
     * @return the mapped object, never {@code null}
     */
    T map(ResultSet rs, int startingParameterIndex) throws SQLException;

  }

  /**
   * Provides information about the database. It typically wraps a
   * {@link DatabaseMetaData} object.
   */
  public sealed interface Meta permits SqlMeta {

    sealed interface QueryTables permits SqlMeta.QueryTablesConfig {

      void schemaName(String value);

      void tableName(String value);

    }

    default List<MetaTable> queryTables() {
      return queryTables(config -> {});
    }

    List<MetaTable> queryTables(Consumer<QueryTables> config);

  }

  public sealed interface MetaTable permits SqlMeta.TableRecord {

    String catalog();

    String schema();

    String name();

    String type();

    String remarks();

    String typeCatalog();

    String typeSchema();

    String typeName();

    String selfReferencingColumnName();

    String refGeneration();

  }

  public sealed interface Migrator permits SqlMigrator {

    void add(String name, String script);

  }

  /**
   * Represents a single page in a paginated result set.
   */
  public interface Page {

    static Page of(int number, int size) {
      Check.argument(number > 0, "number must be positive");
      Check.argument(size > 0, "size must be positive");

      record SqlPage(int number, int size) implements Sql.Page {}

      return new SqlPage(number, size);
    }

    /**
     * The page number. Page numbers are always greater than zero.
     * In other words, the first page is page number 1.
     * The second page is page number 2 and so on.
     *
     * @return the page number
     */
    int number();

    /**
     * The number of rows to be displayed on each page.
     *
     * @return the number of rows to be displayed on each page
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
   * A running session to a database.
   */
  public sealed interface Transaction permits SqlTransaction {

    static Transaction of(Connection connection) {
      try {
        DatabaseMetaData data;
        data = connection.getMetaData();

        SqlDialect dialect;
        dialect = SqlDialect.of(data);

        return new SqlTransaction(dialect, connection);
      } catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }

    /**
     * The isolation level of a transaction.
     */
    public sealed interface Isolation {}

    /**
     * Returns a newly created {@link Sql.Meta} object.
     */
    Meta meta() throws DatabaseException;

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

    /**
     * Sets the SQL statement to be executed by this transaction instance.
     *
     * @param value
     *        the SQL statement to be executed
     */
    void sql(String value);

    void sql(Sql.Kind kind, String value);

    /**
     * Replaces the current SQL statement with the result of
     * applying the specified arguments to the SQL statement as if it were a
     * <em>format string</em>.
     *
     * @param args
     *        arguments referenced by the format specifiers in the format
     *        string.
     */
    void format(Object... args);

    void with(GeneratedKeys<?> value);

    /**
     * Causes the current SQL statement to be paginated according to the
     * specified {@code Page} object. In other words, when the query is
     * executed, only the rows corresponding to the specified page will be
     * retrieved.
     *
     * @param page
     *        the {@code Page} object defining the page number and the number of
     *        rows per page
     */
    void with(Page page);

    /**
     * Causes the current SQL statement to be paginated according to the
     * {@code Page} object provided by the specified provider.
     * In other words, when the query is executed, only the rows corresponding
     * to the specified page will be retrieved.
     *
     * @param provider
     *        provider of the {@code Page} object defining the page number and
     *        the number of rows per page
     */
    default void with(PageProvider provider) {
      Page page;
      page = provider.page();

      with(page);
    }

    /**
     * Adds the specified value to the SQL statement argument list.
     *
     * @param value
     *        the argument value which must not be {@code null}
     */
    void add(Object value);

    void addIf(Object value, boolean condition);

    /**
     * Adds the specified value to the SQL statement argument list.
     *
     * @param value
     *        the argument value which may be {@code null}
     * @param sqlType
     *        the SQL type (as defined in java.sql.Types)
     */
    void add(Object value, int sqlType);

    void addBatch();

    int[] batchUpdate() throws DatabaseException;

    BatchUpdate batchUpdateWithResult();

    /**
     * Executes the current SQL statement as a row-retrieving query.
     */
    <T> List<T> query(Mapper<T> mapper) throws DatabaseException;

    /**
     * Executes the current SQL statement as a row-retrieving query and returns
     * the first result or {@code null} if there were no results.
     *
     * @param mapper
     *        maps rows from the result set to objects
     *
     * @return the first result or {@code null} if there were no results
     */
    <T> Optional<T> queryOptional(Mapper<T> mapper) throws DatabaseException, TooManyRowsException;

    OptionalInt queryOptionalInt() throws DatabaseException, TooManyRowsException;

    OptionalLong queryOptionalLong() throws DatabaseException, TooManyRowsException;

    /**
     * Executes the current SQL statement as a row-retrieving query and returns
     * the first result or throws an exception if there were no results or if
     * there were more than one result.
     *
     * @param mapper
     *        maps rows from the result set to objects
     */
    <T> T querySingle(Mapper<T> mapper) throws DatabaseException;

    int querySingleInt() throws DatabaseException;

    long querySingleLong() throws DatabaseException;

    /**
     * Executes the current SQL statement as an update operation.
     */
    int update() throws DatabaseException;

    Update updateWithResult();

  }

  //
  // Result-like hierarchy
  //

  /**
   * Represents the result of a {@code batchUpdateWithResult} operation.
   */
  public sealed interface BatchUpdate {}

  public sealed interface BatchUpdateFailed extends BatchUpdate permits SqlBatchUpdateFailed {

    List<Cause> causes();

    long[] largeCounts();

    int[] counts();

  }

  public sealed interface BatchUpdateSuccess extends BatchUpdate permits SqlBatchUpdateSuccess {

    int[] counts();

  }

  /**
   * Represents the result of a {@code updateWithResult} operation.
   */
  public sealed interface Update {}

  public sealed interface UpdateFailed extends Update permits SqlUpdateFailed {

    List<Cause> causes();

  }

  public sealed interface UpdateSuccess extends Update permits SqlUpdateSuccess {

    int count();

  }

  public sealed interface Cause
      permits
      SqlCause,
      IntegrityConstraintViolation,
      OtherDatabaseError {

    SQLException unwrap();

  }

  public sealed interface IntegrityConstraintViolation extends Cause permits SqlCause.IntegrityConstraintViolation {}

  public sealed interface OtherDatabaseError extends Cause permits SqlCause.OtherDatabaseError {}

  //
  // Exceptions
  //

  /**
   * Thrown to indicate a database access error.
   */
  public static class DatabaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    DatabaseException() {}

    DatabaseException(Throwable cause) {
      super(cause);
    }

  }

  /**
   * Thrown to indicate that the requested operation is invalid.
   */
  public static final class InvalidOperationException extends RuntimeException {

    private static final long serialVersionUID = 813148817151165616L;

    InvalidOperationException(String message) {
      super(message);
    }

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

  public static final class MigrationFailedException extends DatabaseException {

    private static final long serialVersionUID = 6604970910171861408L;

    MigrationFailedException(Throwable cause) {
      super(cause);
    }

  }

  public static final class NoSuchRowException extends DatabaseException {

    private static final long serialVersionUID = 2389101948080888160L;

    NoSuchRowException() {}

  }

  public static final class RollbackWrapperException extends RuntimeException {

    private static final long serialVersionUID = 6575236786994565106L;

    RollbackWrapperException(Throwable cause) {
      super(cause);
    }

  }

  public static final class TooManyRowsException extends DatabaseException {

    private static final long serialVersionUID = 2389101948080888160L;

    TooManyRowsException() {}

  }

  private Sql() {}

  public static GeneratedKeys.OfInt createGeneratedKeysOfInt() {
    return new SqlGeneratedKeysOfInt();
  }

  /**
   * Creates a {@link Sql.Mapper} for the specified record type.
   */
  public static <R extends Record>
      Mapper<R> createRecordMapper(Class<R> recordType) {
    return RecordMapper.of(recordType);
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

      case LocalTime time -> stmt.setObject(index, time);

      case Long i -> stmt.setLong(index, i.longValue());

      case String s -> stmt.setString(index, s);

      case Null x -> stmt.setNull(index, x.sqlType);

      default -> stmt.setObject(index, value);
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

          if (type == int.class) {
            value = rs.getInt(idx + 1);
          }

          else if (type == boolean.class) {
            value = rs.getBoolean(idx + 1);
          }

          else {
            value = rs.getObject(idx + 1, type);
          }

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