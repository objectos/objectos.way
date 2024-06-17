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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

final class SqlDialect {

  private enum Dialect {

    H2,

    MYSQL;

  }

  private final Dialect dialect;

  private SqlDialect(Dialect dialect) {
    this.dialect = dialect;
  }

  static SqlDialect of(DatabaseMetaData data) throws SQLException {
    String productName;
    productName = data.getDatabaseProductName();

    Dialect dialect;
    dialect = switch (productName) {
      case "H2" -> Dialect.H2;

      case "MySQL" -> Dialect.MYSQL;

      default -> throw new UnsupportedOperationException(
          "Unsupported dialect with databaseProductName=" + productName
      );
    };

    return new SqlDialect(dialect);
  }

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

  public void paginate(StringBuilder sql, Sql.Page page) {
    if (shouldAppendNewLine(sql)) {
      sql.append(System.lineSeparator());
    }

    int pageNumber;
    pageNumber = page.number();

    switch (dialect) {
      case H2:
        if (pageNumber > 1) {
          int offset;
          offset = (pageNumber - 1) * page.size();

          sql.append("offset ");
          sql.append(offset);
          sql.append(" rows");
          sql.append(System.lineSeparator());
        }

        sql.append("fetch first ");
        sql.append(page.size());
        sql.append(" rows only");
        sql.append(System.lineSeparator());
        
        break;

      case MYSQL:
      default:
        sql.append("limit ");
        sql.append(page.size());
        sql.append(System.lineSeparator());

        if (pageNumber > 1) {
          int offset;
          offset = (pageNumber - 1) * page.size();

          sql.append("offset ");
          sql.append(offset);
          sql.append(System.lineSeparator());
        }
        
        break;
    }
  }

  private boolean shouldAppendNewLine(StringBuilder sqlBuilder) {
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