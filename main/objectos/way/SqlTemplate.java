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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

final class SqlTemplate {

  private boolean template;

  private Connection connection;

  private List<Object> arguments;

  private static final Pattern TWO_DASHES = Pattern.compile("^--.*$", Pattern.MULTILINE);

  @SuppressWarnings("unused")
  private PreparedStatement prepare(String sql, int generatedKeys) throws SQLException {
    if (!template) {
      PreparedStatement stmt;
      stmt = connection.prepareStatement(sql, generatedKeys);

      // we assume this method was called after hasArguments() returned true
      // so arguments is guaranteed to be non-null
      setArguments(stmt, arguments);

      return stmt;
    }

    StringBuilder sqlBuilder;
    sqlBuilder = new StringBuilder(sql.length());

    List<Object> values;
    values = Util.createList();

    int argsIndex;
    argsIndex = 0;

    String[] fragments;
    fragments = TWO_DASHES.split(sql);

    String fragment;
    fragment = fragments[0];

    sqlBuilder.append(fragment);

    int placeholders;
    placeholders = placeholders(fragment);

    while (values.size() < placeholders) {
      Object arg;
      arg = arguments.get(argsIndex++);

      values.add(arg);
    }

    for (int i = 1; i < fragments.length; i++) {
      fragment = fragments[i];

      placeholders = placeholders(fragment);

      switch (placeholders) {
        case 0 -> sqlBuilder.append(fragment);

        case 1 -> {
          if (argsIndex >= arguments.size()) {
            throw new IllegalArgumentException(
                "Missing value for fragment: " + fragment
            );
          }

          Object arg;
          arg = arguments.get(argsIndex++);

          if (arg instanceof SqlMaybe maybe) {
            if (maybe.absent()) {
              continue;
            }

            arg = maybe.value();
          }

          sqlBuilder.append(fragment);

          values.add(arg);
        }

        default -> {
          sqlBuilder.append(fragment);

          for (int j = 0; j < placeholders; j++) {

            if (argsIndex >= arguments.size()) {
              throw new IllegalArgumentException(
                  "Missing value for placeholder " + (j + 1) + " of fragment: " + fragment
              );
            }

            Object arg;
            arg = arguments.get(argsIndex++);

            if (arg instanceof SqlMaybe) {
              throw new IllegalArgumentException(
                  "Conditional value must not be used in a fragment with more than one placeholder: " + fragment
              );
            }

            values.add(arg);

          }
        }
      }
    }

    String sqlToPrepare;
    sqlToPrepare = sqlBuilder.toString();

    PreparedStatement stmt;
    stmt = connection.prepareStatement(sqlToPrepare, generatedKeys);

    setArguments(stmt, values);

    return stmt;
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

  private void setArguments(PreparedStatement stmt, List<Object> arguments) throws SQLException {
    for (int idx = 0, size = arguments.size(); idx < size; idx++) {
      Object argument;
      argument = arguments.get(idx);

      Sql.set(stmt, idx + 1, argument);
    }
  }

}