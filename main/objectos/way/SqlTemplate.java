/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

import java.util.List;
import java.util.stream.Collectors;

final class SqlTemplate {

  static final class Fragment {

    final String value;

    final int placeholders;

    int consumed;

    boolean remove;

    public Fragment(String value, int placeholders) {
      this.value = value;

      this.placeholders = placeholders;
    }

    public boolean exhausted() {
      return placeholders == consumed;
    }

  }

  final List<Fragment> fragments;

  private int fragmentIndex;

  private List<Object> arguments;

  private SqlTemplate(List<Fragment> fragments) {
    this.fragments = fragments;
  }

  public static SqlTemplate parse(String sql) {
    enum Parser {

      START_OF_LINE,

      DASH1,

      DASH2,

      NORMAL;

    }

    Parser parser;
    parser = Parser.START_OF_LINE;

    final StringBuilder sb;
    sb = new StringBuilder();

    final UtilList<Fragment> fragments;
    fragments = new UtilList<>();

    int placeholders = 0;

    for (int idx = 0, len = sql.length(); idx < len; idx++) {
      char c;
      c = sql.charAt(idx);

      switch (parser) {
        case START_OF_LINE -> {
          if (c == '-') {
            parser = Parser.DASH1;
          }

          else if (c == '\n') {
            parser = Parser.START_OF_LINE;

            sb.append(c);
          }

          else {
            parser = Parser.NORMAL;

            sb.append(c);
          }
        }

        case DASH1 -> {
          if (c == '-') {
            parser = Parser.DASH2;

            if (!sb.isEmpty()) {
              final String value;
              value = sb.toString();

              final Fragment fragment;
              fragment = new Fragment(value, placeholders);

              fragments.add(fragment);

              sb.setLength(0);

              placeholders = 0;
            }
          }

          else if (c == '\n') {
            parser = Parser.START_OF_LINE;

            sb.append('-');
          }

          else {
            parser = Parser.NORMAL;

            sb.append(c);
          }
        }

        case DASH2 -> {
          if (c == '\n') {
            parser = Parser.START_OF_LINE;
          }

          else {
            parser = Parser.DASH2;

            // ignore all content until EOL
          }
        }

        case NORMAL -> {
          if (c == '\n') {
            parser = Parser.START_OF_LINE;

            sb.append(c);
          }

          else if (c == '?') {
            parser = Parser.NORMAL;

            placeholders++;

            sb.append(c);
          }

          else {
            parser = Parser.NORMAL;

            sb.append(c);
          }
        }
      }
    }

    if (!sb.isEmpty()) {
      final String value;
      value = sb.toString();

      final Fragment fragment;
      fragment = new Fragment(value, placeholders);

      fragments.add(fragment);
    }

    return new SqlTemplate(
        fragments.toUnmodifiableList()
    );
  }

  final void add(Object value) {
    final int size;
    size = fragments.size();

    while (fragmentIndex < size) {
      final Fragment fragment;
      fragment = fragments.get(fragmentIndex);

      if (fragment.placeholders == 0 || fragment.exhausted()) {
        fragmentIndex++;

        continue;
      }

      fragment.consumed++;

      if (arguments == null) {
        arguments = Util.createList();
      }

      arguments.add(value);

      if (fragment.exhausted()) {
        fragmentIndex++;
      }

      return;
    }

    throw new IllegalStateException("""
    This SQL template has no more placeholders available.
    """);
  }

  final void addIf(Object value, boolean condition) {
    final int size;
    size = fragments.size();

    while (fragmentIndex < size) {
      final Fragment fragment;
      fragment = fragments.get(fragmentIndex);

      if (fragment.placeholders == 0) {
        fragmentIndex++;

        continue;
      }

      if (fragmentIndex == 0) {
        throw new UnsupportedOperationException("first fragment");
      }

      if (fragment.placeholders > 1) {
        throw new IllegalArgumentException(
            """
            The 'addIf' operation cannot not be used with a fragment containing more than one placeholder:

            %s
            """.formatted(fragment.value.trim())
        );
      }

      if (condition) {
        if (arguments == null) {
          arguments = Util.createList();
        }

        arguments.add(value);
      } else {
        fragment.consumed++;

        fragment.remove = true;
      }

      fragmentIndex++;

      return;
    }

    throw new IllegalStateException("""
    This SQL template has no more placeholders available.
    """);
  }

  final List<Object> arguments() {
    return arguments;
  }

  final String sql() {
    return fragments.stream()
        .filter(frag -> !frag.remove)
        .map(frag -> frag.value)
        .collect(Collectors.joining());
  }

}