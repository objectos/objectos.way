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

class SqlDialect {

  SqlDialect() {}

  static SqlDialect of(DatabaseMetaData data) {
    return new SqlDialect();
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

  public void paginate(StringBuilder sqlBuilder, Sql.Page page) {
    if (shouldAppendNewLine(sqlBuilder)) {
      sqlBuilder.append(System.lineSeparator());
    }

    sqlBuilder.append("limit ");
    sqlBuilder.append(page.size());
    sqlBuilder.append(System.lineSeparator());

    int pageNumber;
    pageNumber = page.number();

    if (pageNumber > 1) {
      int offset;
      offset = (pageNumber - 1) * page.size();

      sqlBuilder.append("offset ");
      sqlBuilder.append(offset);
      sqlBuilder.append(System.lineSeparator());
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