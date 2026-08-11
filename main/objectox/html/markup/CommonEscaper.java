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
package objectox.html.markup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class CommonEscaper {

  public static final class Options {

    private final StringBuilder chars = new StringBuilder();

    private final List<String> replacements = new ArrayList<>();

    private char max;

    public void add(char c, String replacement) {
      chars.append(c);

      replacements.add(replacement);

      if (c > max) {
        max = c;
      }
    }

    String[] buildTable() {
      final String[] table;
      table = new String[max + 1];

      final int len;
      len = chars.length();

      for (int i = 0; i < len; i++) {
        final char c;
        c = chars.charAt(i);

        table[c] = replacements.get(i);
      }

      return table;
    }

  }

  private final String[] table;

  private CommonEscaper(String[] table) {
    this.table = table;
  }

  public static CommonEscaper create(Consumer<? super Options> opts) {
    final Options o;
    o = new Options();

    opts.accept(o);

    final String[] table;
    table = o.buildTable();

    return new CommonEscaper(table);
  }

  public final String escape(String value) {
    final int len;
    len = value.length();

    final int max;
    max = table.length - 1;

    for (int idx = 0; idx < len; idx++) {
      final char c;
      c = value.charAt(idx);

      if (c > max) {
        continue;
      }

      final String maybe;
      maybe = table[c];

      if (maybe == null) {
        continue;
      }

      return escape(value, idx);
    }

    return value;
  }

  private String escape(String value, int firstIdx) {
    final int len;
    len = value.length();

    final StringBuilder out;
    out = new StringBuilder(len + 16);

    out.append(value, 0, firstIdx);

    final int max;
    max = table.length - 1;

    for (int idx = firstIdx; idx < len; idx++) {
      final char c;
      c = value.charAt(idx);

      if (c > max) {
        out.append(c);

        continue;
      }

      final String replacement;
      replacement = table[c];

      if (replacement == null) {
        out.append(c);
      } else {
        out.append(replacement);
      }
    }

    return out.toString();
  }

}
