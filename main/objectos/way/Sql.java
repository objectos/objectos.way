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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;
import objectos.lang.object.Check;
import objectos.notes.Note3;
import objectos.notes.NoteSink;
import objectos.way.Sql.Source.Option;
import objectos.way.SqlSource.Builder;

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

  // types

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
   * Maps rows from a {@code ResultSet} to objects of type {@code T}.
   */
  @FunctionalInterface
  public interface RowMapper<T> {

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
    T mapRow(ResultSet rs, int startingParameterIndex) throws SQLException;

  }

  /**
   * A source for SQL connections. It typically wraps a {@link DataSource}
   * instance.
   */
  public interface Source {

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
     * @throws UncheckedSqlException
     *         if a database access error occurs
     */
    Transaction beginTransaction(Transaction.IsolationLevel level) throws UncheckedSqlException;

  }

  /**
   * A connection to a running transaction in a database.
   */
  public sealed interface Transaction permits SqlTransaction {

    /**
     * Transaction isolation level.
     */
    public enum IsolationLevel {

      READ_UNCOMMITED(Connection.TRANSACTION_READ_UNCOMMITTED),

      READ_COMMITED(Connection.TRANSACTION_READ_COMMITTED),

      REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

      SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

      public final int jdbcValue;

      private IsolationLevel(int level) {
        this.jdbcValue = level;
      }

    }

    /**
     * Commits this transaction.
     *
     * @throws UncheckedSqlException
     *         if the underlying {@link Connection#commit()} method throws
     */
    void commit() throws UncheckedSqlException;

    /**
     * Undoes all changes made in this transaction.
     *
     * @throws UncheckedSqlException
     *         if the underlying {@link Connection#rollback()} method throws
     */
    void rollback() throws UncheckedSqlException;

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
     * } catch (Throwable t) {
     *   logger.log("Operation failed", trx.rollbackAndSuppress(t));
     * } finally {
     *   trx.close();
     * }</pre>
     *
     * @param error
     *        a throwable instance
     *
     * @return the specified throwable which may or may not contain a new
     *         suppressed exception (from the rollback operation)
     */
    Throwable rollbackAndSuppress(Throwable error);

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
    void close() throws UncheckedSqlException;

    int count(String sql, Object... args) throws UncheckedSqlException;

    int[] executeUpdateText(String sqlText) throws UncheckedSqlException;

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
     * @throws UncheckedSqlException
     *         if a database access error occurs
     */
    void processQuery(QueryProcessor processor, String sql, Object... args) throws UncheckedSqlException;

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
     * @throws UncheckedSqlException
     *         if a database access error occurs
     */
    void processQuery(QueryProcessor processor, Page page, String sql, Object... args) throws UncheckedSqlException;

    default void processQuery(QueryProcessor processor, PageProvider pageProvider, String sql, Object... args) throws UncheckedSqlException {
      Page page;
      page = pageProvider.page(); // implicit null-check

      processQuery(processor, page, sql, args);
    }

    /**
     * Sets the SQL statement of this transaction to the specified value.
     *
     * <p>
     * Invoking this method additionally:
     *
     * <ul>
     * <li>clears any previously set arguments;
     *
     * @param value
     *        the raw SQL statement
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
    <T> List<T> query(RowMapper<T> mapper) throws UncheckedSqlException;

    /**
     * Executes the current SQL statement as a row-retrieving query.
     */
    <T> T queryOne(RowMapper<T> mapper) throws UncheckedSqlException;

    /**
     * Executes the current SQL statement as a row-retrieving query.
     */
    <T> T queryOneOrNull(RowMapper<T> mapper) throws UncheckedSqlException;

    /**
     * Executes the current SQL statement as an update operation.
     */
    int update();

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

  public static final class UncheckedSqlException extends RuntimeException {

    private static final long serialVersionUID = 9207295421842688968L;

    UncheckedSqlException(SQLException cause) {
      super(cause);
    }

    @Override
    public final SQLException getCause() {
      return (SQLException) super.getCause();
    }

  }

  private Sql() {}

  /**
   * Creates a new {@code Source} instance from the specified data source.
   *
   * @param dataSource
   *        the data source
   *
   * @return a new {@code Source} instance
   *
   * @throws SQLException
   *         if a database access error occurs
   */
  public static Source createSource(DataSource dataSource, Source.Option... options) throws SQLException {
    Check.notNull(dataSource, "dataSource == null");
    Check.notNull(options, "options == null");

    SqlSource.Builder builder;
    builder = new SqlSource.Builder(dataSource);

    for (int idx = 0; idx < options.length; idx++) {
      Option o;
      o = options[idx];

      Check.notNull(o, "options[", idx, "] == null");

      SqlOption option;
      option = (SqlOption) o;

      option.acceptSqlSourceBuilder(builder);
    }

    return builder.build();
  }

  public static Page createPage(int number, int size) {
    Check.argument(number > 0, "number must be positive");
    Check.argument(size > 0, "size must be positive");

    record SqlPage(int number, int size) implements Sql.Page {}

    return new SqlPage(number, size);
  }

  static <R extends Record> RowMapper<R> createRowMapper(Class<R> recordType) {
    return RecordRowMapper.of(recordType);
  }

  public static Sql.Source.Option noteSink(NoteSink noteSink) {
    Check.notNull(noteSink, "noteSink == null");

    return new SqlOption() {
      @Override
      final void acceptSqlSourceBuilder(Builder builder) {
        builder.noteSink = noteSink;
      }
    };
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

      case Integer i -> stmt.setInt(index, i.intValue());

      case LocalDate ld -> stmt.setObject(index, ld);

      case LocalDateTime dt -> stmt.setObject(index, dt);

      case Long i -> stmt.setLong(index, i.longValue());

      case String s -> stmt.setString(index, s);

      case Null x -> stmt.setNull(index, x.sqlType);

      default -> throw new IllegalArgumentException("Unexpected type: " + value.getClass());
    }
  }

  private static final class RecordRowMapper<R extends Record> implements RowMapper<R> {

    private final Class<?>[] types;

    private final Constructor<R> constructor;

    private final Object[] values;

    RecordRowMapper(Class<?>[] types, Constructor<R> constructor) {
      this.types = types;

      this.constructor = constructor;

      this.values = new Object[types.length];
    }

    static <R extends Record> RecordRowMapper<R> of(Class<R> recordType) {
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

      return new RecordRowMapper<R>(types, constructor);
    }

    @Override
    public final R mapRow(ResultSet rs, int startingIndex) throws SQLException {
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

}