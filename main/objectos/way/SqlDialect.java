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
import java.sql.SQLException;

sealed abstract class SqlDialect
    permits
    SqlDialectH2,
    SqlDialectMySQL,
    SqlDialectTesting {

  static SqlDialect of(DatabaseMetaData data) throws SQLException {
    String productName;
    productName = data.getDatabaseProductName();

    return switch (productName) {
      case "H2" -> new SqlDialectH2();

      case "MySQL" -> new SqlDialectMySQL();

      case "ObjectosWay" -> new SqlDialectTesting();

      default -> throw new UnsupportedOperationException(
          "Unsupported dialect with databaseProductName=" + productName
      );
    };
  }

  SqlDialect() {}

  public void count(StringBuilder sqlBuilder) {
    boolean newLine;
    newLine = shouldAppendNewLine(sqlBuilder);

    String original;
    original = sqlBuilder.toString();

    sqlBuilder.setLength(0);

    sqlBuilder.append("select count(*) from (");
    sqlBuilder.append(System.lineSeparator());
    sqlBuilder.append(original);
    if (newLine) {
      sqlBuilder.append(System.lineSeparator());
    }
    sqlBuilder.append(") x");
    sqlBuilder.append(System.lineSeparator());
  }

  public final String paginate(String sql, Sql.Page page) {
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

  @Override
  public final String toString() {
    return getClass().getName();
  }

  void metaQueryTables(SqlMeta.QueryTablesConfig q) {}

  SqlMigrator.Initialized migratorInitialize(SqlMigrator migrator) {
    throw new UnsupportedOperationException();
  }

  void migratorHistory(SqlMigrator migrator, int rank, String name, boolean success) {
    throw new UnsupportedOperationException();
  }

  abstract void paginate(StringBuilder builder, int offset, int size);

  private boolean shouldAppendNewLine(CharSequence sqlBuilder) {
    int length;
    length = sqlBuilder.length();

    if (length == 0) {
      return false;
    }

    int lastIndex;
    lastIndex = length - 1;

    char last;
    last = sqlBuilder.charAt(lastIndex);

    return !Character.isWhitespace(last);
  }

}