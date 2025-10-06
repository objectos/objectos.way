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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

sealed interface CssEngineValue {

  record CustomProp(String name, String value) implements CssEngineValue {}

  static CssEngineValue customProp(String name, String value) {
    return new CustomProp(name, value);
  }

  record ThemeSkip(String ns) implements CssEngineValue {}

  static CssEngineValue themeSkip(String ns) {
    return new ThemeSkip(ns);
  }

  static final class ThemeVar implements CssEngineValue {
    final String ns;
    final String id;
    final String value;

    ThemeVar(String ns, String id, String value) {
      this.ns = ns;
      this.id = id;
      this.value = value;
    }

    @Override
    public final boolean equals(Object obj) {
      return obj == this || obj instanceof ThemeVar that
          && Objects.equals(ns, that.ns)
          && Objects.equals(id, that.id)
          && Objects.equals(value, that.value);
    }

    @Override
    public final int hashCode() {
      return Objects.hash(ns, id, value);
    }
  }

  static CssEngineValue themeVar(String ns, String id, String value) {
    return new ThemeVar(ns, id, value);
  }

  public static List<CssEngineValue> parse(String text) {
    final List<CssEngineValue> values;
    values = new ArrayList<>();

    parse(text, values);

    return values;
  }

  public static void parse(String text, List<CssEngineValue> values) {
    final CssEngineValueParser parser;
    parser = new CssEngineValueParser(text, values);

    parser.parse();
  }

}
