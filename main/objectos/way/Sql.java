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
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import objectos.lang.object.Check;
import objectos.notes.Note3;
import objectos.notes.NoteSink;
import objectos.way.Sql.Source.Option;
import objectos.way.SqlSource.Builder;

/**
 * The Objectos SQL main class.
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
  
  @FunctionalInterface
  public interface ResultSetHandler {

    void handle(ResultSet rs) throws SQLException;

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
  public interface Transaction extends AutoCloseable {

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

    void commit() throws UncheckedSqlException;

    void rollback() throws UncheckedSqlException;
    
    default void rollbackAndRethrow(Throwable rethrow) {
      Check.notNull(rethrow, "rethrow == null");
      
      try {
        rollback();
      } catch (UncheckedSqlException e) {
        SQLException sqlException;
        sqlException = e.getCause();

        rethrow.addSuppressed(sqlException);
      }

      if (rethrow instanceof Error error) {
        throw error;
      }

      if (rethrow instanceof RuntimeException re) {
        throw re;
      }

      throw new RuntimeException(rethrow);
    }

    @Override
    void close() throws UncheckedSqlException;

    int[] batchUpdate(String sql, Object[]... batches) throws UncheckedSqlException;

    int count(String sql, Object... args) throws UncheckedSqlException;
    
    void queryPage(String sql, ResultSetHandler handler, Page page, Object... args) throws UncheckedSqlException;

    default Object[] values(Object... values) {
      return values;
    }
    
  }

  public static class UncheckedSqlException extends RuntimeException {

    private static final long serialVersionUID = 9207295421842688968L;

    public UncheckedSqlException(SQLException cause) {
      super(cause);
    }
    
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

  static void set(PreparedStatement stmt, int index, Object value) throws SQLException {
    switch (value) {
      case Boolean b -> stmt.setBoolean(index, b.booleanValue());

      case Integer i -> stmt.setInt(index, i.intValue());

      case Long i -> stmt.setLong(index, i.longValue());

      case String s -> stmt.setString(index, s);

      default -> throw new IllegalArgumentException("Unexpected type: " + value.getClass());
    }
  }
  
}