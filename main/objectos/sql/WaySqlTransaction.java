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
package objectos.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import objectos.lang.object.Check;

final class WaySqlTransaction implements SqlTransaction {

  private static final Pattern TWO_DASHES = Pattern.compile("^--.*$", Pattern.MULTILINE);

  private final Dialect dialect;

  private final Connection connection;

  WaySqlTransaction(Dialect dialect, Connection connection) {
    this.dialect = dialect;

    this.connection = connection;
  }

  @Override
  public final void close() throws SQLException {
    connection.close();
  }

  @Override
  public final void queryPage(String sql, ResultSetHandler handler, Page page, Object... args) throws SQLException {
    Check.notNull(sql, "sql == null");
    Check.notNull(handler, "handler == null");
    Check.notNull(page, "page == null");
    Check.notNull(args, "args == null");

    StringBuilder sqlBuilder;
    sqlBuilder = new StringBuilder(sql.length());

    Object[] values;
    values = new Object[args.length];

    int valuesIndex;
    valuesIndex = 0;

    int argsIndex;
    argsIndex = 0;

    String[] fragments;
    fragments = TWO_DASHES.split(sql);

    String fragment;
    fragment = fragments[0];

    sqlBuilder.append(fragment);

    int placeholders;
    placeholders = placeholders(fragment);

    while (valuesIndex < placeholders) {
      values[valuesIndex++] = args[argsIndex++];
    }

    for (int i = 1; i < fragments.length; i++) {
      fragment = fragments[i];

      placeholders = placeholders(fragment);

      switch (placeholders) {
        case 0 -> sqlBuilder.append(fragment.trim());

        case 1 -> {
          if (argsIndex < args.length) {
            Object arg;
            arg = args[argsIndex++];

            if (arg == null) {
              continue;
            }

            sqlBuilder.append(fragment);

            values[valuesIndex++] = arg;
          } else {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        default -> throw new IllegalArgumentException("""
        A fragment must not contain more than one placeholder:
        \t%s
        """.formatted(fragment));
      }
    }

    if (shouldAppendNewLine(sqlBuilder)) {
      sqlBuilder.append(System.lineSeparator());
    }

    dialect.paginate(sqlBuilder, page);

    String sqlToPrepare;
    sqlToPrepare = sqlBuilder.toString();

    try (PreparedStatement stmt = connection.prepareStatement(sqlToPrepare)) {
      for (int idx = 0; idx < valuesIndex;) {
        Object value;
        value = values[idx++];

        set(stmt, idx, value);
      }

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          handler.handle(rs);
        }
      }
    }
  }

  private int placeholders(String fragment) {
    int count;
    count = 0;

    int question;
    question = 0;

    while (true) {
      question = fragment.indexOf('?', question);

      if (question < 0) {
        break;
      }

      count++;

      question++;
    }

    return count;
  }

  private void set(PreparedStatement stmt, int index, Object value) throws SQLException {
    switch (value) {
      case Integer i -> stmt.setInt(index, i.intValue());

      case String s -> stmt.setString(index, s);

      default -> throw new IllegalArgumentException("Unexpected type: " + value.getClass());
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