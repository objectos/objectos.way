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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

final class SqlTemplate {

  private static final Pattern TWO_DASHES = Pattern.compile("^--.*$", Pattern.MULTILINE);

  private final StringBuilder sqlBuilder;

  private final Object[] values;

  private final int valuesIndex;

  public SqlTemplate(StringBuilder sqlBuilder, Object[] values, int valuesIndex) {
    this.sqlBuilder = sqlBuilder;

    this.values = values;

    this.valuesIndex = valuesIndex;
  }

  public static SqlTemplate parse(String sql, Object... args) {
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

    return new SqlTemplate(sqlBuilder, values, valuesIndex);
  }

  private static int placeholders(String fragment) {
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

  final int count(SqlDialect dialect, Connection connection) throws Sql.DatabaseException {
    dialect.count(sqlBuilder);

    String sqlToPrepare;
    sqlToPrepare = sqlBuilder.toString();

    try (PreparedStatement stmt = connection.prepareStatement(sqlToPrepare)) {
      for (int idx = 0; idx < valuesIndex;) {
        Object value;
        value = values[idx++];

        Sql.set(stmt, idx, value);
      }

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1);
        } else {
          return 0;
        }
      }
    } catch (SQLException e) {
      throw new Sql.DatabaseException(e);
    }
  }

  final void process(Connection connection, Sql.QueryProcessor processor) throws Sql.DatabaseException {
    String sqlToPrepare;
    sqlToPrepare = sqlBuilder.toString();

    try (PreparedStatement stmt = connection.prepareStatement(sqlToPrepare)) {
      for (int idx = 0; idx < valuesIndex;) {
        Object value;
        value = values[idx++];

        Sql.set(stmt, idx, value);
      }

      try (ResultSet rs = stmt.executeQuery()) {
        processor.process(rs);
      }
    } catch (SQLException e) {
      throw new Sql.DatabaseException(e);
    }
  }

}