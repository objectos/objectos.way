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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class SqlMeta implements Sql.Meta {

  record TableRecord(
      String catalog,
      String schema,
      String name,
      String type,
      String remarks,
      String typeCatalog,
      String typeSchema,
      String typeName,
      String selfReferencingColumnName,
      String refGeneration
  ) implements Sql.MetaTable {

    TableRecord(ResultSet rs, int idx) throws SQLException {
      this(
          rs.getString(idx++),
          rs.getString(idx++),
          rs.getString(idx++),
          rs.getString(idx++),
          rs.getString(idx++),
          rs.getString(idx++),
          rs.getString(idx++),
          rs.getString(idx++),
          rs.getString(idx++),
          rs.getString(idx++)
      );
    }

  }

  private final SqlDialect dialect;

  private final DatabaseMetaData delegate;

  SqlMeta(SqlDialect dialect, DatabaseMetaData delegate) {
    this.dialect = dialect;

    this.delegate = delegate;
  }

  // ##################################################################
  // # BEGIN: Tables
  // ##################################################################

  final class QueryTablesConfig implements QueryTables {

    String catalog;

    String schemaPattern;

    String tableNamePattern;

    String[] types;

    @Override
    public final void schemaName(String value) {
      schemaPattern = Objects.requireNonNull(value, "value == null");
    }

    @Override
    public final void tableName(String value) {
      tableNamePattern = Objects.requireNonNull(value, "value == null");
    }

    final ResultSet execute() throws SQLException {
      return delegate.getTables(catalog, schemaPattern, tableNamePattern, types);
    }

  }

  @Override
  public final List<Sql.MetaTable> queryTables(Consumer<QueryTables> config) {
    final QueryTablesConfig q;
    q = new QueryTablesConfig();

    config.accept(q);

    dialect.metaQueryTables(q);

    try (ResultSet rs = q.execute()) {
      return query(rs, TableRecord::new);
    } catch (SQLException e) {
      throw new Sql.DatabaseException(e);
    }
  }

  // ##################################################################
  // # END: Tables
  // ##################################################################

  private <T> List<T> query(ResultSet rs, Sql.Mapper<T> mapper) throws SQLException {
    final UtilList<T> list;
    list = new UtilList<>();

    while (rs.next()) {
      final T instance;
      instance = mapper.map(rs, 1);

      if (instance == null) {
        throw new Sql.MappingException("Mapper produced a null value");
      }

      list.add(instance);
    }

    return list.toUnmodifiableList();
  }

}